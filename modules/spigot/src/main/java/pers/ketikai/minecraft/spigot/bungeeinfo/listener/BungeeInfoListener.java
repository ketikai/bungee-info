package pers.ketikai.minecraft.spigot.bungeeinfo.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import pers.ketikai.minecraft.protocol.bungeeinfo.info.BungeeServerInfo;
import pers.ketikai.minecraft.spigot.bungeeinfo.BungeeInfo;
import pers.ketikai.minecraft.spigot.bungeeinfo.packet.event.SpigotPacketReceivedEvent;

public class BungeeInfoListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void on(SpigotPacketReceivedEvent event) {
        Object payload = event.getPayload();
        if (!(payload instanceof BungeeServerInfo)) {
            return;
        }
        BungeeInfo.setBungeeServerInfo((BungeeServerInfo) payload);
    }
}
