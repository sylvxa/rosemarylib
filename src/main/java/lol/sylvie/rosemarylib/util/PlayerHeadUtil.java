package lol.sylvie.rosemarylib.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTextures;
import com.mojang.authlib.yggdrasil.ProfileResult;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PlayerHeadUtil {
    public static ItemStack getPlayerHead(GameProfile profile) {
        ItemStack head = Items.PLAYER_HEAD.getDefaultStack();
        head.set(DataComponentTypes.PROFILE, new ProfileComponent(profile));
        return head;
    }

    public static ItemStack getPlayerHead(ServerPlayerEntity player) {
        return getPlayerHead(player.getGameProfile());
    }

    public static ItemStack getPlayerHead(MinecraftServer server, UUID uuid, String name) {
        GameProfile profile = new GameProfile(uuid, name);
        if (server != null && server.getSessionService().getTextures(profile) == MinecraftProfileTextures.EMPTY) {
            ProfileResult fetched = server.getSessionService().fetchProfile(profile.getId(), false);
            if (fetched != null)
                profile = fetched.profile();
        }

        return getPlayerHead(profile);
    }
}
