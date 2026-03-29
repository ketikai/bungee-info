package pers.ketikai.minecraft.bungee.bungeeinfo;

import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pers.ketikai.minecraft.bungee.bungeeinfo.listener.BungeeInfoListener;
import pers.ketikai.minecraft.bungee.bungeeinfo.packet.BungeePacketChannel;
import pers.ketikai.minecraft.protocol.bungeeinfo.codec.GsonPacketCodec;
import pers.ketikai.minecraft.protocol.bungeeinfo.info.BungeeServerInfo;
import pers.ketikai.minecraft.protocol.bungeeinfo.util.Entry;
import pers.ketikai.minecraft.tags.bungeeinfo.Tags;

import java.util.Objects;

public class BungeeInfo extends Plugin {
    private static volatile BungeePacketChannel channel;

    @NotNull
    public static BungeePacketChannel channel() {
        return Objects.requireNonNull(channel);
    }

    @Override
    public void onEnable() {
        channel = new BungeePacketChannel(this, Tags.ID,
                Entry.of(BungeeServerInfo.ID, new GsonPacketCodec<>(BungeeServerInfo.class))
        );
        getProxy().getPluginManager().registerListener(this, new BungeeInfoListener());
    }
}
