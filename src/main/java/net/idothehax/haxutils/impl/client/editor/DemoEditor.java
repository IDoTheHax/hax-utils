package net.idothehax.haxutils.impl.client.editor;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.idothehax.haxutils.api.client.editor.Editor;
import net.idothehax.haxutils.api.client.render.HaxRenderSystem;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class DemoEditor implements Editor {

    public static final Component TITLE = Component.translatable("editor.veil.example.imgui.title");

    private final ImBoolean open = new ImBoolean();

    @Override
    public void render() {
        ImGui.showDemoWindow(this.open);

        if (!this.open.get()) {
            HaxRenderSystem.renderer().getEditorManager().hide(this);
        }
    }

    @Override
    public void onShow() {
        this.open.set(true);
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public Component getGroup() {
        return EXAMPLE_GROUP;
    }
}
