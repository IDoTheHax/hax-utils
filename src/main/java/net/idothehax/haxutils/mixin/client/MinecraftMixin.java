package net.idothehax.haxutils.mixin.client;


import net.idothehax.haxutils.HaxUtilsClient;
import net.idothehax.haxutils.api.client.render.HaxRenderSystem;
import net.idothehax.haxutils.fabric.event.FabricHaxRendererEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;resizeDisplay()V", shift = At.Shift.BEFORE))
    public void init(GameConfig gameConfig, CallbackInfo ci) {
        HaxUtilsClient.initRenderer();
        FabricHaxRendererEvent.EVENT.invoker().onHaxRendererAvailable(HaxRenderSystem.renderer());
    }
}
