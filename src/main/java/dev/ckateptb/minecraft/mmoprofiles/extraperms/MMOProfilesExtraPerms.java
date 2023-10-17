package dev.ckateptb.minecraft.mmoprofiles.extraperms;

import dev.ckateptb.common.tableclothcontainer.IoC;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

public class MMOProfilesExtraPerms extends JavaPlugin {
    private static Logger logger;
    @Getter
    private static MMOProfilesExtraPerms plugin;

    public static Logger log() {
        return logger;
    }

    public MMOProfilesExtraPerms() {
        plugin = this;
        logger = this.getSLF4JLogger();
        IoC.registerBean(this, MMOProfilesExtraPerms.class);
        IoC.scan(MMOProfilesExtraPerms.class);
    }
}