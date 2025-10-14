package lol.sylvie.rosemarylib.state;

import com.mojang.serialization.Codec;
import lombok.Getter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerContainer<T extends PlayerContainer<T, P>, P extends PlayerData<P>> extends ServerContainer<T> {
    @Getter
    public HashMap<UUID, P> players = new HashMap<>();

    public PlayerContainer(String modId, Codec<T> codec) {
        super(modId, codec);
    }

    public abstract P getDefaultPlayerState(UUID playerId);

    public P getPlayerState(MinecraftServer server, UUID player) {
        T serverState = getServerState(server);
        return serverState.players.computeIfAbsent(player, this::getDefaultPlayerState);
    }


    public P getPlayerState(ServerPlayerEntity player) {
        return getPlayerState(player.getEntityWorld().getServer(), player.getUuid());
    }

    public P getPlayerState(LivingEntity entity) {
        if (!(entity instanceof ServerPlayerEntity player))
            throw new RuntimeException("Entity must be a ServerPlayerEntity to hold persistent data!");
        return getPlayerState(player);
    }
}
