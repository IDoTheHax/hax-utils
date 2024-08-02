package net.idothehax.haxutils.mixin.client.imgui;

import net.idothehax.haxutils.HaxUtils;
import net.idothehax.haxutils.HaxUtilsClient;
import net.idothehax.haxutils.impl.client.imgui.HaxImGuiImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "onPress", at = @At("HEAD"))
    public void keyPress(long window, int button, int action, int mods, CallbackInfo ci) {
        if (window == this.minecraft.getWindow().getWindow() && action == GLFW_PRESS && HaxUtilsClient.EDITOR_KEY.matchesMouse(button)) {
            HaxImGuiImpl.get().toggle();
        }
    }
}
