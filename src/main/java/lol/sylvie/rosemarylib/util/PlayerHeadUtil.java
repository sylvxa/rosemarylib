package lol.sylvie.rosemarylib.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.ProfileResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import java.util.UUID;

public class PlayerHeadUtil {
    public static ItemStack getPlayerHead(GameProfile profile) {
        ItemStack head = Items.PLAYER_HEAD.getDefaultInstance();

        head.set(DataComponents.PROFILE, ResolvableProfile.createResolved(profile));
        return head;
    }

    public static ItemStack getPlayerHead(ServerPlayer player) {
        return getPlayerHead(player.getGameProfile());
    }

    public static ItemStack getPlayerHead(MinecraftServer server, UUID uuid, String name) {
        GameProfile profile = new GameProfile(uuid, name);
        MinecraftSessionService sessionService = server.services().sessionService();
        if (sessionService.getTextures(profile) == MinecraftProfileTextures.EMPTY) {
            ProfileResult fetched = sessionService.fetchProfile(profile.id(), false);
            if (fetched != null)
                profile = fetched.profile();
        }

        return getPlayerHead(profile);
    }
}
