package pers.ketikai.minecraft.spigot.bungeeinfo.placeholder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.ketikai.minecraft.protocol.bungeeinfo.info.BungeeServerInfo;
import pers.ketikai.minecraft.spigot.bungeeinfo.BungeeInfo;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public class BungeeInfoPlaceholder extends PlaceholderExpansion {

    public static final String ARGUMENTS_DELIMITER = "_";
    public static final String ARGUMENTS_DELIMITER_1 = ":";

    @NotNull
    @Getter
    private final String identifier;

    @NotNull
    @Getter
    private final String author;

    @NotNull
    @Getter
    private final String version;

    @Nullable
    private String[] argumentsOf(@NotNull String params) {
        Objects.requireNonNull(params);
        if (params.isEmpty()) {
            return null;
        }
        return params.replace(ARGUMENTS_DELIMITER_1, ARGUMENTS_DELIMITER).split(ARGUMENTS_DELIMITER, -1);
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        String[] arguments = argumentsOf(params);
        if (arguments == null) {
            return super.onRequest(offlinePlayer, params);
        }
        if (arguments.length != 1) {
            return null;
        }
        BungeeServerInfo info = BungeeInfo.getBungeeServerInfo();
        if (info == null) {
            return null;
        }
        switch (arguments[0]) {
            case "id":
                return info.getId();
            case "name":
                return info.getName();
            case "motd":
                return info.getMotd();
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        return super.onPlaceholderRequest(player, params);
    }
}
