package dev.ckateptb.minecraft.mmoprofiles.extraperms.listener;

import dev.ckateptb.common.tableclothcontainer.annotation.Component;
import dev.ckateptb.minecraft.mmoprofiles.extraperms.MMOProfilesExtraPerms;
import dev.ckateptb.minecraft.mmoprofiles.extraperms.config.Config;
import fr.phoenixdevt.profiles.event.PlayerIdDispatchEvent;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@CustomLog
@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final Config config;
    private final MMOProfilesExtraPerms plugin;

    private Boolean confirmed = null;

    private boolean isConfirmed() {
        if (this.confirmed == null) {
            this.confirmed = this.config.isConfirm();
        }
        return this.confirmed;
    }

    private Boolean warn = null;

    private boolean isWarn() {
        if (this.warn == null) {
            this.warn = this.config.isWarn();
        }
        return this.warn;
    }

    private LuckPerms luckPerms;

    private LuckPerms getLuckPerms() {
        if (this.luckPerms == null) {
            this.luckPerms = LuckPermsProvider.get();
        }
        return this.luckPerms;
    }

    private UserManager userManager;

    private UserManager getUserManager() {
        if (this.userManager == null) {
            this.userManager = this.getLuckPerms().getUserManager();
        }
        return this.userManager;
    }

    private String serverName;

    private String getServerName() {
        if (this.serverName == null) {
            this.serverName = this.getLuckPerms().getServerName();
        }
        return this.serverName;
    }

    private GroupManager groupManager;

    private GroupManager getGroupManager() {
        if (this.groupManager == null) {
            this.groupManager = this.getLuckPerms().getGroupManager();
        }
        return this.groupManager;
    }

    @EventHandler
    public void on(PlayerIdDispatchEvent event) {
        if (!this.isConfirmed()) return;
        UserManager userManager = this.getUserManager();
        Player player = event.getPlayer();
        UUID fakeId = event.getFakeId();
        if (fakeId == null) return;
        UUID initialId = event.getInitialId();
        String playerName = player.getName();
        String serverName = this.getServerName();
        GroupManager groupManager = this.getGroupManager();
        boolean warn = this.isWarn();
        log.info("Found a MMOProfile proxy for player {} mapping uuid {} => {}", playerName, initialId, fakeId);
        (userManager.isLoaded(fakeId) ?
                CompletableFuture.completedFuture(userManager.getUser(fakeId)) :
                userManager.loadUser(fakeId))
                .whenComplete((fakeUser, ignore) ->
                        this.recursiveUnwrap(playerName, fakeId, warn, groupManager, serverName,
                                        fakeUser.data().toCollection(), new HashSet<>()
                                )
                                .collect(Collectors.toSet())
                                .forEach(node -> player.addAttachment(this.plugin, node.getKey(), node.getValue()))
                );
    }

    public Stream<Node> recursiveUnwrap(String playerName, UUID fakeId, boolean warn, GroupManager groupManager,
                                        String serverName, Collection<Node> nodes, Set<String> groups) {
        return nodes.stream().filter(node -> {
            ImmutableContextSet contexts = node.getContexts();
            Set<String> servers = contexts.getValues("server");
            if (servers.isEmpty()) return true;
            return servers.contains(serverName);
        }).flatMap(node -> {
            String key = node.getKey();
            if (key.startsWith("group.")) {
                String[] split = key.split("\\.");
                if (split.length > 1) {
                    String groupName = split[1].toLowerCase();
                    if (groups.contains(groupName)) return Stream.empty();
                    groups.add(groupName);
                    Group group = groupManager.getGroup(groupName);
                    if (group != null) {
                        return this.recursiveUnwrap(playerName, fakeId, warn, groupManager, serverName,
                                group.data().toCollection(), groups);
                    } else if (warn) {
                        log.warn("Player {} profile {} has unknown group! " +
                                "You can disable this message in " +
                                "MMOProfilesExtraPerms config.", playerName, fakeId);
                    }
                }
            }
            return Stream.of(node);
        });
    }
}
