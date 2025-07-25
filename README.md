<!--suppress ALL -->
<p align="center">
    <img src="images/banner.png" alt="HuskSync" />
    <a href="https://github.com/WiIIiam278/HuskSync/actions/workflows/ci.yml">
        <img src="https://img.shields.io/github/actions/workflow/status/WiIIiam278/HuskSync/ci.yml?branch=master&logo=github"/>
    </a>
    <a href="https://repo.william278.net/#/releases/net/william278/husksync/">
        <img src="https://repo.william278.net/api/badge/latest/releases/net/william278/husksync/husksync-common?color=00fb9a&name=Maven&prefix=v" />
    </a>
    <a href="https://discord.gg/tVYhJfyDWG">
        <img src="https://img.shields.io/discord/818135932103557162.svg?label=&logo=discord&logoColor=fff&color=7389D8&labelColor=6A7EC2" />
    </a> 
    <br/>
    <b>
        <a href="https://www.spigotmc.org/resources/husksync.97144/">Spigot</a>
    </b> —
    <b>
        <a href="https://william278.net/docs/husksync/setup">Setup</a>
    </b> — 
    <b>
        <a href="https://william278.net/docs/husksync/">Docs</a>
    </b> — 
    <b>
        <a href="https://github.com/WiIIiam278/HuskSync/issues">Issues</a>
    </b>
</p>
<br/>

**HuskSync** is a modern, cross-server player data synchronization system that enables the comprehensive synchronization of your user's data across multiple proxied servers. It does this by making use of Redis and a MySQL/Mongo/PostgreSQL to optimally cache data while players change servers.

## Features
**⭐ Seamless synchronization** &mdash; Utilises optimised Redis caching when players change server to sync player data super quickly for a seamless experience.

**⭐ Complete player synchronization** &mdash; Sync inventories, Ender Chests, health, hunger, effects, advancements, statistics, locked maps & [more](https://william278.net/docs/husksync/sync-features)—no data left behind!

**⭐ Backup, restore & rotate** &mdash; Something gone wrong? Restore players back to a previous data state. Rotate and manage data snapshots in-game!

**⭐ Import existing data** &mdash; Import your MySQLPlayerDataBridge data—or from your existing world data! No server reset needed!

**⭐ Works great with Plan** &mdash; Stay in touch with your community through HuskSync analytics on your Plan web panel.

**⭐ Extensible API & open-source** &mdash; Need more? Extend the plugin with the Developer API. Or, submit a pull request through our code bounty system!

**Ready?** [It's syncing time!](https://william278.net/docs/husksync/setup)

## Compatibility
HuskSync supports the following [compatible versions](https://william278.net/docs/husksync/compatibility) of Minecraft. Since v3.7, you must download the correct version of HuskSync for your server:

|    Minecraft    | Latest HuskSync | Java Version | Platforms     | Support Status                |
|:---------------:|:---------------:|:------------:|:--------------|:------------------------------|
|    1.21.7/8     |    _latest_     |      21      | Paper         | ✅ **Active Release**          |
|     1.21.6      |      3.8.5      |      21      | Paper         | 🗃️ Archived (July 2025)      |
|     1.21.5      |    _latest_     |      21      | Paper         | ✅ **January 2026** (Non-LTS)  |
|     1.21.4      |    _latest_     |      21      | Paper, Fabric | ✅ **November 2025** (Non-LTS) |
|     1.21.3      |      3.7.1      |      21      | Paper, Fabric | 🗃️ Archived (December 2024)  |
|     1.21.1      |    _latest_     |      21      | Paper, Fabric | ✅ **November 2025** (LTS)     |
|     1.20.6      |      3.6.8      |      17      | Paper         | 🗃️ Archived (October 2024)   |
|     1.20.4      |      3.6.8      |      17      | Paper         | 🗃️ Archived (July 2024)      |
|     1.20.1      |    _latest_     |      17      | Paper, Fabric | ✅ **November 2025** (LTS)     |
| 1.17.1 - 1.19.4 |      3.6.8      |      17      | Paper         | 🗃️ Archived                  |
|     1.16.5      |      3.2.1      |      16      | Paper         | 🗃️ Archived                  |

HuskSync is primarily developed against the latest release. Old Minecraft versions are allocated a support channel based on popularity, mod support, etc:

* Long Term Support (LTS) &ndash; Supported for up to 12-18 months
* Non-Long Term Support (Non-LTS) &ndash; Supported for 3-6 months

Verify your purchase on Discord and [Download HuskSync](https://william278.net/project/husksync/download) for your server.

## Setup
Requires a [MySQL/MariaDB/Mongo/PostgreSQL database](https://william278.net/docs/husksync/database), a [Redis (v5.0+) server]((https://william278.net/docs/husksync/redis)) and a network of [compatible Spigot or Fabric Minecraft servers](https://william278.net/docs/husksync/compatibility).

1. Place the plugin jar file in the `/plugins` or `/mods` directory of each Spigot/Fabric server. You do not need to install HuskSync as a proxy plugin.
2. Start, then stop every server to let HuskSync generate the config file.
3. Navigate to the HuskSync config file on each server and fill in both your database and Redis server credentials.
4. Start every server again and synchronization will begin.

## Development
To build HuskSync, simply run the following in the root of the repository (building requires Java 21). Builds will be output in `/target`:

```bash
./gradlew clean build
```

HuskSync uses `essential-multi-version` (Fabric) and `preprocessor` (Bukkit) to target multiple versions of Minecraft in one codebase - [check here](https://github.com/WiIIiam278/PreProcessor?tab=readme-ov-file#code-example) for a preprocessor comment logic reference.

### License
HuskSync is licensed under the Apache 2.0 license.

- [License](https://github.com/WiIIiam278/HuskSync/blob/master/LICENSE)

Contributions to the project are welcome&mdash;feel free to open a pull request with new features, improvements and/or fixes!

### Support
Due to its complexity, official binaries and customer support for HuskSync is provided through a paid model. This means that support is only available to users who have purchased a license to the plugin from Spigot, Polymart, or BuiltByBit and have provided proof of purchase. Please join our Discord server if you have done so and need help!

### Translations
Translations of the plugin locales are welcome to help make the plugin more accessible. Please submit a pull request with your translations as a `.yml` file.

- [Locales Directory](https://github.com/WiIIiam278/HuskSync/tree/master/common/src/main/resources/locales)
- [English Locales](https://github.com/WiIIiam278/HuskSync/tree/master/common/src/main/resources/locales/en-gb.yml)

## Links
- [Docs](https://william278.net/docs/husksync/) &mdash; Read the plugin documentation!
- [Spigot](https://www.spigotmc.org/resources/husksync.97144/) &mdash; View the Spigot resource page (Also: [Polymart](https://polymart.org/resource/husksync.1634), [Craftaro](https://craftaro.com/marketplace/product/husksync.758), [BuiltByBit](https://builtbybit.com/resources/husksync.34956/))
- [Issues](https://github.com/WiIIiam278/HuskSync/issues) &mdash; File a bug report or feature request
- [Discord](https://discord.gg/tVYhJfyDWG) &mdash; Get help, ask questions (Purchase required)
- [bStats](https://bstats.org/plugin/bukkit/HuskSync%20-%20Bukkit/13140) &mdash; View plugin metrics

---
&copy; [William278](https://william278.net/), 2025. Licensed under the Apache-2.0 License.
