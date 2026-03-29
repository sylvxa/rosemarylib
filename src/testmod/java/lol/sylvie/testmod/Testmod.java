package lol.sylvie.testmod;

import lol.sylvie.rosemarylib.config.ConfigManager;
import lol.sylvie.rosemarylib.gui.DialogBuilder;
import lol.sylvie.rosemarylib.util.PlayerHeadUtil;
import lol.sylvie.testmod.config.ExampleConfig;
import lol.sylvie.testmod.state.ExamplePlayerContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.minecraft.server.dialog.input.SingleOptionInput;

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
            new DialogBuilder(player, Component.literal("Test"))
                    .bodyText(Component.literal("Here are some persistent values you can modify!"))
                    .bodyItem(PlayerHeadUtil.getPlayerHead(player), 16, 16)
                    .bodyItem(PlayerHeadUtil.getPlayerHead(player.level().getServer(), UUID.fromString("75f9c6f0-5dc1-4e24-92ee-82fd64392936"), "sylvxa"), 16, 16)
                    .numberInput("favorite_number", 400, Component.literal("Favorite Number"), new NumberRangeInput.RangeInfo(0, 80085, Optional.of((float) playerData.getFavoriteNumber()), Optional.of(1f)))
                    .textInput("preferred_name", 400, Component.literal("Preferred Name"), playerData.getPreferredName(), 64)
                    .singleOptionInput("searching_for", 400, Component.literal("Searching for"), Arrays.stream(ExamplePlayerContainer.ExamplePlayerData.SearchingFor.values()).map(e -> new SingleOptionInput.Entry(e.name(), Optional.empty(), playerData.getSearchingFor().equals(e))).toList())
                    .actionButton(Identifier.fromNamespaceAndPath(MOD_ID, "save_data"), Component.literal("Save Data"), (data) -> {
                        playerData.setFavoriteNumber(data.getIntOr("favorite_number", playerData.getFavoriteNumber()));
                        playerData.setPreferredName(data.getStringOr("preferred_name", playerData.getPreferredName()));
                        // don't do this
                        playerData.setSearchingFor(ExamplePlayerContainer.ExamplePlayerData.SearchingFor.valueOf(data.getStringOr("searching_for", ExamplePlayerContainer.ExamplePlayerData.SearchingFor.TREASURE.name())));
                    })
                    .closeOnEsc(false)
                    .buildAndOpen(player);

            player.sendSystemMessage(Component.literal(EXAMPLE_CONFIG.getInstance().getWelcomeMessage()));
        });

        EXAMPLE_CONFIG.addSaveShutdownHook();
    }
}
