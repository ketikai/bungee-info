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

package pers.ketikai.minecraft.spigot.bungeeinfo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.ketikai.minecraft.protocol.bungeeinfo.codec.GsonPacketCodec;
import pers.ketikai.minecraft.protocol.bungeeinfo.info.BungeeServerInfo;
import pers.ketikai.minecraft.protocol.bungeeinfo.util.Entry;
import pers.ketikai.minecraft.spigot.bungeeinfo.listener.BungeeInfoListener;
import pers.ketikai.minecraft.spigot.bungeeinfo.packet.SpigotPacketChannel;
import pers.ketikai.minecraft.spigot.bungeeinfo.placeholder.BungeeInfoPlaceholder;
import pers.ketikai.minecraft.tags.bungeeinfo.Tags;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class BungeeInfo extends JavaPlugin {

    private static volatile BungeeServerInfo bungeeServerInfo;
    private static volatile SpigotPacketChannel channel;

    @Nullable
    public static BungeeServerInfo getBungeeServerInfo() {
        return bungeeServerInfo;
    }

    public static void setBungeeServerInfo(@Nullable BungeeServerInfo bungeeServerInfo) {
        BungeeInfo.bungeeServerInfo = bungeeServerInfo;
    }

    @NotNull
    public static void dumpReadme(@NotNull File dataFolder) {
        File configFile = new File(dataFolder, "README.md");
        if (!configFile.exists()) {
            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                throw new RuntimeException("无法创建配置目录");
            }
            Path configFilePath = configFile.toPath();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(BungeeInfo.class.getClassLoader().getResourceAsStream("README.md")), StandardCharsets.UTF_8))) {
                try (BufferedWriter writer = Files.newBufferedWriter(configFilePath, StandardOpenOption.CREATE_NEW)) {
                    while (reader.ready()) {
                        writer.write(reader.readLine());
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @NotNull
    public static SpigotPacketChannel channel() {
        return Objects.requireNonNull(channel);
    }

    @Override
    public void onLoad() {
        File dataFolder = getDataFolder();
        dumpReadme(dataFolder);
        setBungeeServerInfo(null);
    }

    @Override
    public void onEnable() {
        channel = new SpigotPacketChannel(this, Tags.ID,
                Entry.of(BungeeServerInfo.ID, new GsonPacketCodec<>(BungeeServerInfo.class))
        );
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new BungeeInfoListener(), this);
        new BungeeInfoPlaceholder(Tags.ID, Tags.ID, Tags.VERSION).register();
    }
}
