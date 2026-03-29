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

package pers.ketikai.minecraft.bungee.bungeeinfo.packet.event;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BungeePacketReceivedEvent extends Event {

    private final ProxiedPlayer player;
    private final Object payload;

    public BungeePacketReceivedEvent(@NotNull ProxiedPlayer player, @NotNull Object payload) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(payload);
        this.player = player;
        this.payload = payload;
    }

    @NotNull
    public ProxiedPlayer getPlayer() {
        return player;
    }

    @NotNull
    public Object getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BungeePacketReceivedEvent that = (BungeePacketReceivedEvent) o;
        return Objects.equals(getPlayer(), that.getPlayer()) && Objects.equals(getPayload(), that.getPayload());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayer(), getPayload());
    }

    @Override
    public String toString() {
        return "BungeePacketReceivedEvent{" +
                "player=" + player +
                ", payload=" + payload +
                '}';
    }
}
