package lol.sylvie.rosemarylib.state;

import com.mojang.serialization.Codec;
import lombok.Getter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerContainer<T extends PlayerContainer<T, P>, P extends PlayerData<P>> extends ServerContainer<T> {
    @Getter
    public HashMap<UUID, P> players = new HashMap<>();

    public PlayerContainer(Identifier dataId, Codec<T> codec) {
        super(dataId, codec);
    }

    public abstract P getDefaultPlayerState(UUID playerId);

    public P getPlayerState(MinecraftServer server, UUID player) {
        T serverState = getServerState(server);
        return serverState.players.computeIfAbsent(player, this::getDefaultPlayerState);
    }


    public P getPlayerState(ServerPlayer player) {
        return getPlayerState(player.level().getServer(), player.getUUID());
    }

    public P getPlayerState(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player))
            throw new RuntimeException("Entity must be a ServerPlayer to hold persistent data!");
        return getPlayerState(player);
    }
}
