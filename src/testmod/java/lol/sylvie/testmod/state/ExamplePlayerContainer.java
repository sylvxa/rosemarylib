package lol.sylvie.testmod.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lol.sylvie.rosemarylib.state.PlayerContainer;
import lol.sylvie.rosemarylib.state.PlayerData;
import lol.sylvie.testmod.Testmod;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExamplePlayerContainer extends PlayerContainer<ExamplePlayerContainer, ExamplePlayerContainer.ExamplePlayerData> {
    private static final Codec<ExamplePlayerContainer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, ExamplePlayerData.CODEC).fieldOf("players").forGetter(PlayerContainer::getPlayers)
    ).apply(instance, ExamplePlayerContainer::new));

    private static final Identifier dataId = Identifier.fromNamespaceAndPath(Testmod.MOD_ID, "saved_data");

    public ExamplePlayerContainer() {
        super(dataId, CODEC);
    }

    public ExamplePlayerContainer(Map<UUID, ExamplePlayerData> map) {
        super(dataId, CODEC);
        this.players = new HashMap<>(map);
    }

    @Override
    public ExamplePlayerData getDefaultPlayerState(UUID playerId) {
        return new ExamplePlayerData(0, "Nobody", ExamplePlayerData.SearchingFor.TREASURE);
    }

    @Override
    public ExamplePlayerContainer getDefaultState() {
        return new ExamplePlayerContainer();
    }

    @Setter
    @Getter
    public static class ExamplePlayerData extends PlayerData<ExamplePlayerData> {
        private SearchingFor searchingFor;
        private int favoriteNumber;
        private String preferredName;

        public static Codec<ExamplePlayerData> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.INT.fieldOf("favorite_number").forGetter(ExamplePlayerData::getFavoriteNumber),
                Codec.STRING.fieldOf("preferred_name").forGetter(ExamplePlayerData::getPreferredName),
                Codec.STRING.xmap(
                        SearchingFor::valueOf,
                        Enum::name
                ).fieldOf("searching_for").forGetter(ExamplePlayerData::getSearchingFor)
        ).apply(i, ExamplePlayerData::new));

        public ExamplePlayerData(int favoriteNumber, String preferredName, SearchingFor searchingFor) {
            super(CODEC);

            this.favoriteNumber = favoriteNumber;
            this.preferredName = preferredName;
            this.searchingFor = searchingFor;
        }

        public enum SearchingFor {
            TREASURE,
            LOVE,
            HAPPINESS
        }
    }
}
