/*
 *     Copyright (C) 2024 ideal-state
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package pers.ketikai.minecraft.bungee.bungeeinfo.packet;

import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pers.ketikai.minecraft.protocol.bungeeinfo.api.PacketChannel;
import pers.ketikai.minecraft.protocol.bungeeinfo.api.exception.PacketException;
import pers.ketikai.minecraft.protocol.bungeeinfo.util.Entry;
import pers.ketikai.minecraft.tags.bungeeinfo.Tags;

import java.util.Objects;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public enum BungeePacketListener implements Listener {

    INSTANCE;

    private static final Logger log = LogManager.getLogManager().getLogger(BungeePacketListener.class.getName());

    @SuppressWarnings("unchecked")
    @EventHandler
    public void on(PluginMessageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!Objects.equals(event.getTag(), Tags.ID)) {
            return;
        }
        Connection sender = event.getSender();
        if (!(sender instanceof Server)) {
            return;
        }
        Connection receiver = event.getReceiver();
        if (!(receiver instanceof ProxiedPlayer)) {
            return;
        }
        event.setCancelled(true);
        byte[] message = event.getData();
        try {
            PacketChannel.of(Tags.ID).receive(message, Entry.of("player", receiver));
        } catch (PacketException e) {
            log.throwing("", "", e);
        }
    }
}
