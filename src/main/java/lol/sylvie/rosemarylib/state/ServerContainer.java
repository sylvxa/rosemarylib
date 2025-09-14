package lol.sylvie.rosemarylib.state;

import com.mojang.serialization.Codec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.PersistentStateType;

public abstract class ServerContainer<T extends ServerContainer<?>> extends PersistentState {
    private final PersistentStateType<T> type;

    public ServerContainer(String modId, Codec<T> codec) {
        this.type = new PersistentStateType<>(modId, this::getDefaultState, codec, null);
    }

    public abstract T getDefaultState();

    public T getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getOverworld().getPersistentStateManager();
        T state = persistentStateManager.getOrCreate(type);

        state.markDirty();

        return state;
    }
}
