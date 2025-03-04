package net.idothehax.haxutils.impl.client.imgui;

import imgui.ImFont;
import net.idothehax.haxutils.api.client.imgui.HaxImGui;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.ObjIntConsumer;

@ApiStatus.Internal
public class InactiveHaxImGuiImpl implements HaxImGui {

    @Override
    public void begin() {
    }

    @Override
    public void beginFrame() {
    }

    @Override
    public void endFrame() {
    }

    @Override
    public void end() {
    }

    @Override
    public void onGrabMouse() {
    }

    @Override
    public void toggle() {
    }

    @Override
    public void updateFonts() {
    }

    @Override
    public void addImguiShaders(ObjIntConsumer<ResourceLocation> registry) {
    }

    @Override
    public boolean mouseButtonCallback(long window, int button, int action, int mods) {
        return false;
    }

    @Override
    public boolean scrollCallback(long window, double xOffset, double yOffset) {
        return false;
    }

    @Override
    public boolean keyCallback(long window, int key, int scancode, int action, int mods) {
        return false;
    }

    @Override
    public boolean charCallback(long window, int codepoint) {
        return false;
    }

    @Override
    public boolean shouldHideMouse() {
        return false;
    }

    @Override
    public void free() {
    }
}