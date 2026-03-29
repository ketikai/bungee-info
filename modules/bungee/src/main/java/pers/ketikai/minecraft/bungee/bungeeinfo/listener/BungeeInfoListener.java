package pers.ketikai.minecraft.bungee.bungeeinfo.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pers.ketikai.minecraft.bungee.bungeeinfo.BungeeInfo;
import pers.ketikai.minecraft.bungee.bungeeinfo.packet.BungeePacketChannel;
import pers.ketikai.minecraft.protocol.bungeeinfo.api.exception.PacketException;
import pers.ketikai.minecraft.protocol.bungeeinfo.info.BungeeServerInfo;
import pers.ketikai.minecraft.protocol.bungeeinfo.util.Entry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.LogManager.getLogManager;

public class BungeeInfoListener implements Listener {

    private static final Logger log = getLogManager().getLogger(BungeeInfoListener.class.getName());

    @EventHandler
    public void on(ServerConnectedEvent event) {
        BungeePacketChannel channel = BungeeInfo.channel();
        try {
            ServerInfo info = event.getServer().getInfo();
            channel.send(BungeeServerInfo.ID, new BungeeServerInfo(
                    info.getName(), info.getName(), info.getMotd()
            ), Entry.of("sender", event.getServer()));
        } catch (PacketException e) {
            log.throwing("", "", e);
        }
    }
}
