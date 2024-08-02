package net.idothehax.haxutils.api.client.render;

import net.idothehax.haxutils.api.client.editor.EditorManager;
import net.idothehax.haxutils.impl.client.imgui.HaxImGuiImpl;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.system.NativeResource;

import java.util.List;

public class HaxRenderer implements NativeResource {

    private final EditorManager editorManager;
    private final GuiInfo guiInfo;

    @ApiStatus.Internal
    public HaxRenderer() {
        this.editorManager = new EditorManager();
        this.guiInfo = new GuiInfo();
    }


    /**
     * @return The manager for all editors
     */
    public EditorManager getEditorManager() {
        return this.editorManager;
    }

    /**
     * @return The gui info instance
     */
    public GuiInfo getGuiInfo() {
        return this.guiInfo;
    }

    /**
     * @return Whether ImGui can be used
     */
    public static boolean hasImGui() {
        return HaxImGuiImpl.get() instanceof HaxImGuiImpl;
    }

    @Override
    public void free() {
        this.guiInfo.free();
    }
}
