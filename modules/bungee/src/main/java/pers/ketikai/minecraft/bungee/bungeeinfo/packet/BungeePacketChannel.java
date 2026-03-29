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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pers.ketikai.minecraft.bungee.bungeeinfo.packet.event.BungeePacketReceivedEvent;
import pers.ketikai.minecraft.protocol.bungeeinfo.api.PacketChannel;
import pers.ketikai.minecraft.protocol.bungeeinfo.api.PacketChannelAdapter;
import pers.ketikai.minecraft.protocol.bungeeinfo.api.PacketCodec;
import pers.ketikai.minecraft.protocol.bungeeinfo.api.exception.PacketException;
import pers.ketikai.minecraft.protocol.bungeeinfo.util.Entry;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public final class BungeePacketChannel implements PacketChannelAdapter {

    private static final Logger log = Logger.getLogger(BungeePacketChannel.class.getName());

    private final String name;

    @SafeVarargs
    public BungeePacketChannel(@NotNull Plugin plugin, @NotNull String name, Entry<Short, PacketCodec>... codecs) {
        Objects.requireNonNull(plugin);
        Objects.requireNonNull(name);
        this.name = name;
        ProxyServer server = plugin.getProxy();
        server.registerChannel(name);
        server.getPluginManager().registerListener(plugin, BungeePacketListener.INSTANCE);
        PacketChannel channel = PacketChannel.of(name);
        channel.setSender((packet, context) -> {
            log.fine(String.format("[%s] Sending packet %s", name, Arrays.toString(packet)));
            Object sender = context.get("sender");
            if (sender instanceof ProxiedPlayer) {
                ((ProxiedPlayer) sender).getReconnectServer().sendData(name, packet, true);
            } else {
                for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
                    info.sendData(name, packet, true);
                }
            }
        });
        channel.setReceiver((payload, context) -> {
            log.fine(String.format("[%s] Receiving packet %s", name, payload));
            Object player = context.get("player");
            ProxyServer.getInstance().getPluginManager().callEvent(new BungeePacketReceivedEvent((ProxiedPlayer) player, payload));
        });
        for (Entry<Short, PacketCodec> codec : codecs) {
            if (codec == null) {
                continue;
            }
            Short key = codec.getKey();
            if (key == null) {
                continue;
            }
            PacketCodec value = codec.getValue();
            if (value == null) {
                continue;
            }
            channel.register(key, value);
        }
    }

    @SafeVarargs
    @Override
    public final void send(short id, @NotNull Object payload, @NotNull Entry<String, Object>... context) throws PacketException {
        PacketChannelAdapter channel = PacketChannel.of(name);
        channel.send(id, payload, context);
    }

    @SafeVarargs
    @Override
    public final void receive(byte @NotNull [] packet, @NotNull Entry<String, Object>... context) throws PacketException {
        PacketChannelAdapter channel = PacketChannel.of(name);
        channel.receive(packet, context);
    }

    @Override
    public @NotNull String getName() {
        PacketChannelAdapter channel = PacketChannel.of(name);
        return channel.getName();
    }
}
