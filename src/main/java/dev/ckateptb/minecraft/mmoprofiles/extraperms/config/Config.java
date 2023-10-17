package dev.ckateptb.minecraft.mmoprofiles.extraperms.config;

import dev.ckateptb.common.tableclothconfig.hocon.HoconConfig;
import dev.ckateptb.common.tableclothcontainer.annotation.Component;
import dev.ckateptb.common.tableclothcontainer.annotation.PostConstruct;
import dev.ckateptb.minecraft.mmoprofiles.extraperms.MMOProfilesExtraPerms;
import dev.ckateptb.minecraft.varflex.internal.org.spongepowered.configurate.objectmapping.meta.Comment;
import dev.ckateptb.minecraft.varflex.internal.org.spongepowered.configurate.objectmapping.meta.Setting;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;

@Getter
@Component
public class Config extends HoconConfig {
    @Comment("""
            Please read before continuing
            https://gitlab.com/phoenix-dvpmt/mmoprofiles/-/wikis/Proxy-Based%20Profiles
            You need to be sure to only use this on the game server.
            synced-data.permissions is set to true
            MySQL is enabled and working
            default-profile is set to false
            unique-profile is set to false
            no-gui-on-login is set to true
            proxy_based_profiles is enabled
            kick_if_no_profile is enabled
            unselect_profile_on_login is false
            enable apply-bukkit-attachment-permissions in LuckPerms config
            """)
    @Setting("I confirm that I configured everything correctly")
    private boolean confirm = false;
    @Setting("Alert me about unknown player profile permission groups")
    private boolean warn = true;

    @SneakyThrows
    @PostConstruct
    public void init() {
        this.load();
        this.save();
    }

    @Override
    public File getFile() {
        return MMOProfilesExtraPerms.getPlugin().getDataFolder().toPath().resolve("config.json").toFile();
    }
}
