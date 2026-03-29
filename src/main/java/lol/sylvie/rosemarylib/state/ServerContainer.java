package lol.sylvie.rosemarylib.state;

import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

public abstract class ServerContainer<T extends ServerContainer<?>> extends SavedData {
    private final SavedDataType<T> type;

    public ServerContainer(Identifier dataId, Codec<T> codec) {
        this.type = new SavedDataType<>(dataId, this::getDefaultState, codec, null);
    }

    public abstract T getDefaultState();

    public T getServerState(MinecraftServer server) {
        SavedDataStorage persistentStateManager = server.getDataStorage();
        T state = persistentStateManager.computeIfAbsent(type);

        state.setDirty();

        return state;
    }
}
