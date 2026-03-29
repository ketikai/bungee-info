package pers.ketikai.minecraft.protocol.bungeeinfo.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
public class BungeeServerInfo {

    public static final short ID = 0x10;

    @NotNull
    private String id;

    @NotNull
    private String name;

    @NotNull
    private String motd;
}
