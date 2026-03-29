package lol.sylvie.rosemarylib.mixin;

import lol.sylvie.rosemarylib.gui.DialogManager;
import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonPacketListenerImpl.class)
public class ServerCommonNetworkHandlerMixin {
    @Inject(method = "handleCustomClickAction", at = @At("TAIL"))
    public void rosemarylib$handleDialogLocationSelect(ServerboundCustomClickActionPacket packet, CallbackInfo ci) {
        if (!(((ServerCommonPacketListenerImpl) (Object) this instanceof ServerGamePacketListenerImpl playNetworkHandler))) return;

        DialogManager.onCustomClickAction(packet, playNetworkHandler.player);
    }
}
