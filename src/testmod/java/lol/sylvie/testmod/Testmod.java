package lol.sylvie.testmod;

import lol.sylvie.rosemarylib.config.ConfigManager;
import lol.sylvie.rosemarylib.gui.DialogBuilder;
import lol.sylvie.rosemarylib.util.PlayerHeadUtil;
import lol.sylvie.testmod.config.ExampleConfig;
import lol.sylvie.testmod.state.ExamplePlayerContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.dialog.input.NumberRangeInputControl;
import net.minecraft.dialog.input.SingleOptionInputControl;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class Testmod implements ModInitializer {
    public static final String MOD_ID = "testmod";

    public static ExamplePlayerContainer PLAYER_CONTAINER = new ExamplePlayerContainer();
    public static ConfigManager<ExampleConfig> EXAMPLE_CONFIG = new ConfigManager<>(MOD_ID, new ExampleConfig());

    @Override
    public void onInitialize() {
        ServerPlayerEvents.JOIN.register(player -> {
            ExamplePlayerContainer.ExamplePlayerData playerData = PLAYER_CONTAINER.getPlayerState(player);
            new DialogBuilder(player, Text.literal("Test"))
                    .bodyText(Text.literal("Here are some persistent values you can modify!"))
                    .bodyItem(PlayerHeadUtil.getPlayerHead(player), 16, 16)
                    .bodyItem(PlayerHeadUtil.getPlayerHead(player.getServer(), UUID.fromString("75f9c6f0-5dc1-4e24-92ee-82fd64392936"), "sylvxa"), 16, 16)
                    .numberInput("favorite_number", 400, Text.literal("Favorite Number"), new NumberRangeInputControl.RangeInfo(0, 80085, Optional.of((float) playerData.getFavoriteNumber()), Optional.of(1f)))
                    .textInput("preferred_name", 400, Text.literal("Preferred Name"), playerData.getPreferredName(), 64)
                    .singleOptionInput("searching_for", 400, Text.literal("Searching for"), Arrays.stream(ExamplePlayerContainer.ExamplePlayerData.SearchingFor.values()).map(e -> new SingleOptionInputControl.Entry(e.name(), Optional.empty(), playerData.getSearchingFor().equals(e))).toList())
                    .actionButton(Identifier.of(MOD_ID, "save_data"), Text.literal("Save Data"), (data) -> {
                        playerData.setFavoriteNumber(data.getInt("favorite_number", playerData.getFavoriteNumber()));
                        playerData.setPreferredName(data.getString("preferred_name", playerData.getPreferredName()));
                        // don't do this
                        playerData.setSearchingFor(ExamplePlayerContainer.ExamplePlayerData.SearchingFor.valueOf(data.getString("searching_for", ExamplePlayerContainer.ExamplePlayerData.SearchingFor.TREASURE.name())));
                    })
                    .closeOnEsc(false)
                    .buildAndOpen(player);

            player.sendMessage(Text.literal(EXAMPLE_CONFIG.getInstance().getWelcomeMessage()));
        });

        EXAMPLE_CONFIG.addSaveShutdownHook();
    }
}
