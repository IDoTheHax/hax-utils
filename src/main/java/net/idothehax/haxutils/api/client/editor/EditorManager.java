package net.idothehax.haxutils.api.client.editor;

import imgui.ImFont;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import net.idothehax.haxutils.HaxUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.network.chat.Component;

/**
 * <p>Manages all editors for Hax. Editors are ImGui powered panels that can be dynamically registered and unregistered with {@link #add(Editor)}.</p>
 * <p>Developed for use in veil, now being used for the guis in the utilities</p>
 *
 * @author Ocelot
 */
public class EditorManager {

    public static final ResourceLocation DEFAULT_FONT = HaxUtils.haxUtilsPath("jetbrains_mono");

    private final Map<Editor, ImBoolean> editors;
    private final EditorFontManager fonts;
    private boolean enabled;

    @ApiStatus.Internal
    public EditorManager() {
        this.editors = new TreeMap<>(Comparator.comparing(editor -> editor.getClass().getSimpleName()));
        this.fonts = new EditorFontManager();
        this.enabled = false;
    }

    public ImFont getFont(ResourceLocation name, boolean bold, boolean italic) {
        return this.fonts.getFont(name, bold, italic);
    }

    public ImFont getFont(boolean bold, boolean italic) {
        return this.getFont(DEFAULT_FONT, bold, italic);
    }

    @ApiStatus.Internal
    public void render() {
        if (!this.enabled) {
            return;
        }

        if (ImGui.beginMainMenuBar()) {
            ImFont font = ImGui.getFont();
            float dingleWidth = font.calcTextSizeAX(ImGui.getFontSize(), Float.MAX_VALUE, 0, " Veil ") + 4;
            float dingleHeight = ImGui.getTextLineHeightWithSpacing() + 2;
            ImGui.getWindowDrawList().addRectFilled(0f, 0f, dingleWidth, dingleHeight, ImGui.getColorU32(ImGuiCol.FrameBgHovered));
            ImGui.text("Hax Utils ");

            for (Map.Entry<Editor, ImBoolean> entry : this.editors.entrySet()) {
                Editor editor = entry.getKey();
                Component group = editor.getGroup();
                if (group == null) {
                    group = Editor.DEFAULT_GROUP;
                }
                if (ImGui.beginMenu(group.getString())) {
                    ImBoolean enabled = entry.getValue();

                    ImGui.beginDisabled(!editor.isEnabled());
                    if (ImGui.menuItem(editor.getDisplayName().getString(), null, enabled.get())) {
                        if (!enabled.get()) {
                            this.show(editor);
                        } else {
                            this.hide(editor);
                        }
                    }
                    ImGui.endDisabled();
                    ImGui.endMenu();
                }
            }

            for (Map.Entry<Editor, ImBoolean> entry : this.editors.entrySet()) {
                Editor editor = entry.getKey();
                if (entry.getValue().get() && editor.isMenuBarEnabled()) {
                    ImGui.separator();
                    ImGui.textColored(0xFFAAAAAA, editor.getDisplayName().getString());
                    editor.renderMenuBar();
                }
            }

            ImGui.endMainMenuBar();
        }

        for (Map.Entry<Editor, ImBoolean> entry : this.editors.entrySet()) {
            Editor editor = entry.getKey();
            ImBoolean enabled = entry.getValue();

            if (!editor.isEnabled()) {
                enabled.set(false);
            }
            if (!enabled.get()) {
                continue;
            }

            editor.render();
        }
    }

    @ApiStatus.Internal
    public void renderLast() {
        if (!this.enabled) {
            return;
        }

        for (Map.Entry<Editor, ImBoolean> entry : this.editors.entrySet()) {
            Editor editor = entry.getKey();
            ImBoolean enabled = entry.getValue();
            if (enabled.get()) {
                editor.renderLast();
            }
        }
    }

    public void show(Editor editor) {
        ImBoolean enabled = this.editors.get(editor);
        if (enabled != null && !enabled.get()) {
            editor.onShow();
            enabled.set(true);
        }
    }

    public void hide(Editor editor) {
        ImBoolean enabled = this.editors.get(editor);
        if (enabled != null && enabled.get()) {
            editor.onHide();
            enabled.set(false);
        }
    }

    public boolean isVisible(Editor editor) {
        ImBoolean visible = this.editors.get(editor);
        return visible != null && visible.get();
    }

    public synchronized void add(Editor editor) {
        this.editors.computeIfAbsent(editor, unused -> new ImBoolean());
    }

    public synchronized void remove(Editor editor) {
        this.hide(editor);
        this.editors.remove(editor);
    }

    /**
     * Toggles visibility of the ImGui overlay.
     */
    public void toggle() {
        this.enabled = !this.enabled;
    }

    /**
     * @return Whether the overlay is active
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Sets whether the overlay should be active.
     *
     * @param enabled Whether to enable the ImGui overlay
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}