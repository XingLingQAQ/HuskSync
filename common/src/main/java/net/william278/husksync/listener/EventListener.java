package net.william278.husksync.listener;

import net.william278.husksync.HuskSync;
import net.william278.husksync.config.Settings;
import net.william278.husksync.data.ItemData;
import net.william278.husksync.data.DataSaveCause;
import net.william278.husksync.player.OnlineUser;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public abstract class EventListener {

    /**
     * The plugin instance
     */
    protected final HuskSync plugin;

    /**
     * Set of UUIDs current awaiting item synchronization. Events will be cancelled for these users
     */
    private final HashSet<UUID> usersAwaitingSync;

    /**
     * Whether the plugin is currently being disabled
     */
    private boolean disabling;

    protected EventListener(@NotNull HuskSync plugin) {
        this.plugin = plugin;
        this.usersAwaitingSync = new HashSet<>();
        this.disabling = false;
    }

    public final void handlePlayerJoin(@NotNull OnlineUser user) {
        if (user.isDead()) {
            return;
        }
        usersAwaitingSync.add(user.uuid);
        CompletableFuture.runAsync(() -> {
            try {
                // Hold reading data for the network latency threshold, to ensure the source server has set the redis key
                Thread.sleep(Math.max(0, plugin.getSettings().getIntegerValue(Settings.ConfigOption.SYNCHRONIZATION_NETWORK_LATENCY_MILLISECONDS)));
            } catch (InterruptedException e) {
                plugin.getLoggingAdapter().log(Level.SEVERE, "An exception occurred handling a player join", e);
            } finally {
                plugin.getRedisManager().getUserServerSwitch(user).thenAccept(changingServers -> {
                    if (!changingServers) {
                        // Fetch from the database if the user isn't changing servers
                        setUserFromDatabase(user).thenAccept(succeeded -> handleSynchronisationCompletion(user, succeeded));
                    } else {
                        final int TIME_OUT_MILLISECONDS = 3200;
                        CompletableFuture.runAsync(() -> {
                            final AtomicInteger currentMilliseconds = new AtomicInteger(0);
                            final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                            // Set the user as soon as the source server has set the data to redis
                            executor.scheduleAtFixedRate(() -> {
                                if (user.isOffline()) {
                                    executor.shutdown();
                                    return;
                                }
                                if (disabling || currentMilliseconds.get() > TIME_OUT_MILLISECONDS) {
                                    executor.shutdown();
                                    setUserFromDatabase(user)
                                            .thenAccept(succeeded -> handleSynchronisationCompletion(user, succeeded));
                                    return;
                                }
                                plugin.getRedisManager().getUserData(user).thenAccept(redisUserData ->
                                        redisUserData.ifPresent(redisData -> {
                                            user.setData(redisData, plugin.getSettings(), plugin.getEventCannon(),
                                                            plugin.getLoggingAdapter(), plugin.getMinecraftVersion())
                                                    .thenAccept(succeeded -> handleSynchronisationCompletion(user, succeeded)).join();
                                            executor.shutdown();
                                        })).join();
                                currentMilliseconds.addAndGet(200);
                            }, 0, 200L, TimeUnit.MILLISECONDS);
                        });
                    }
                });
            }
        });
    }

    /**
     * Set a user's data from the database
     *
     * @param user The user to set the data for
     * @return Whether the data was successfully set
     */
    private CompletableFuture<Boolean> setUserFromDatabase(@NotNull OnlineUser user) {
        return plugin.getDatabase().getCurrentUserData(user).thenApply(databaseUserData -> {
            if (databaseUserData.isPresent()) {
                return user.setData(databaseUserData.get().userData(), plugin.getSettings(), plugin.getEventCannon(),
                        plugin.getLoggingAdapter(), plugin.getMinecraftVersion()).join();
            }
            return true;
        });
    }

    /**
     * Handle a player's synchronization completion
     *
     * @param user      The {@link OnlineUser} to handle
     * @param succeeded Whether the synchronization succeeded
     */
    private void handleSynchronisationCompletion(@NotNull OnlineUser user, boolean succeeded) {
        if (succeeded) {
            plugin.getLocales().getLocale("synchronisation_complete").ifPresent(user::sendActionBar);
            usersAwaitingSync.remove(user.uuid);
            plugin.getDatabase().ensureUser(user).join();
            plugin.getEventCannon().fireSyncCompleteEvent(user);
        } else {
            plugin.getLocales().getLocale("synchronisation_failed")
                    .ifPresent(user::sendMessage);
            plugin.getDatabase().ensureUser(user).join();
        }
    }

    public final void handlePlayerQuit(@NotNull OnlineUser user) {
        // Players quitting have their data manually saved by the plugin disable hook
        if (disabling) {
            return;
        }
        // Don't sync players awaiting synchronization
        if (usersAwaitingSync.contains(user.uuid)) {
            return;
        }
        plugin.getRedisManager().setUserServerSwitch(user).thenRun(() -> user.getUserData().thenAccept(
                userData -> plugin.getRedisManager().setUserData(user, userData).thenRun(
                        () -> plugin.getDatabase().setUserData(user, userData, DataSaveCause.DISCONNECT).join())));
        usersAwaitingSync.remove(user.uuid);
    }

    public final void handleWorldSave(@NotNull List<OnlineUser> usersInWorld) {
        if (disabling || !plugin.getSettings().getBooleanValue(Settings.ConfigOption.SYNCHRONIZATION_SAVE_ON_WORLD_SAVE)) {
            return;
        }
        CompletableFuture.runAsync(() -> usersInWorld.forEach(user ->
                plugin.getDatabase().setUserData(user, user.getUserData().join(), DataSaveCause.WORLD_SAVE).join()));
    }

    public final void handlePluginDisable() {
        disabling = true;

        plugin.getOnlineUsers().stream().filter(user -> !usersAwaitingSync.contains(user.uuid)).forEach(user ->
                plugin.getDatabase().setUserData(user, user.getUserData().join(), DataSaveCause.SERVER_SHUTDOWN).join());

        plugin.getDatabase().close();
        plugin.getRedisManager().close();
    }

    public final void handleMenuClose(@NotNull OnlineUser user, @NotNull ItemData menuInventory) {
        if (disabling) {
            return;
        }
        plugin.getDataEditor().closeInventoryMenu(user, menuInventory);
    }

    public final boolean cancelMenuClick(@NotNull OnlineUser user) {
        if (disabling) {
            return true;
        }
        return plugin.getDataEditor().cancelInventoryEdit(user);
    }

    public final boolean cancelPlayerEvent(@NotNull OnlineUser user) {
        return disabling || usersAwaitingSync.contains(user.uuid);
    }

}