package lol.sylvie.rosemarylib.state;

import com.mojang.serialization.Codec;

public class PlayerData<P extends PlayerData<P>> {
    protected Codec<P> codec;

    public PlayerData(Codec<P> codec) {
        this.codec = codec;
    }
}