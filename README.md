<p align="center">
<h3 align="center">MMOProfilesExtraPerms</h3>

------

<p align="center">
The concept of a permission vendor in MMOProfiles differs from the standard implementation due to the limitations of UUID spoofing. So we'll fix that..
</p>

<p align="center">
<img alt="License" src="https://img.shields.io/github/license/CKATEPTb-minecraft/MMOProfilesExtraPerms">
<a href="https://docs.gradle.org/7.5/release-notes.html"><img src="https://img.shields.io/badge/Gradle-7.5-brightgreen.svg?colorB=469C00&logo=gradle"></a>
<a href="https://discord.gg/P7FaqjcATp" target="_blank"><img alt="Discord" src="https://img.shields.io/discord/925686623222505482?label=discord"></a>
</p>

------

# Versioning

We use [Semantic Versioning 2.0.0](https://semver.org/spec/v2.0.0.html) to manage our releases.

# Features

- [X] Fix MMOProfiles LuckPerms permission sync
- [X] Recursive groups unwrap

# How To Install Plugin

* Download plugin and deps[link] (https://github.com/CKATEPTb-minecraft/MMOProfilesExtraPerms/blob/development/binary/MMOProfilesExtraPerms.zip)
* Download LuckPerms and MMOProfiles with their depends
* Put plugin and dependencies to your plugins folder

# How To Use Plugin

* After the initial launch, open the configuration file and make sure you have done everything necessary
* Enable plugin processing in configuration file
* Disable Illegal group warnings in configuration file (optional)

# Why

MMOProfile does not provide for working with permissions, similar to that in bukkit, which is why users could not fully unleash the potential of the plugin. In other words,
The permissions that were assigned to the profile created in MMOProfiles did not work. The plugin I provided solves this problem without any extra effort.

# How Does Its Work

Every time a player connects to the server by selecting a profile, LuckPerms transfer permissions to Player#attachment.
