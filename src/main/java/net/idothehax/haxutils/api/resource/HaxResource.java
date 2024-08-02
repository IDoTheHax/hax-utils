package net.idothehax.haxutils.api.resource;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import net.idothehax.haxutils.HaxUtils;
import net.idothehax.haxutils.api.client.imgui.HaxImGuiUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.WatchEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public interface HaxResource<T extends HaxResource<?>> {

    /**
     * Rebders this resource into the resource panel.
     *
     * @param dragging Whether the user is dragging the resource
     */
    default void render(boolean dragging) {
        HaxImGuiUtil.icon(this.getIconCode());
        ImGui.sameLine();

        HaxResourceInfo resourceInfo = this.resourceInfo();
        ImGui.pushStyleColor(ImGuiCol.Text, resourceInfo.isStatic() ? 0xFFAAAAAA : 0xFFFFFFFF);
        if (dragging) {
            HaxImGuiUtil.resourceLocation(resourceInfo.location());
        } else {
            ImGui.text(resourceInfo.fileName());
        }
        ImGui.popStyleColor();
    }

    /**
     * Called from the watcher thread when this resource updates on disc.
     *
     * @param event The event received from the file watcher
     * @return A future for when the key can be reset. All events are ignored until this future completes
     */
    default CompletableFuture<?> onFileSystemChange(WatchEvent<Path> event) {
        if (this.canHotReload() && (event.kind() == ENTRY_CREATE || event.kind() == ENTRY_MODIFY)) {
            HaxUtils.LOGGER.info("Hot swapping " + this.resourceInfo().location() + " after file system change");

            Minecraft client = Minecraft.getInstance();
            return CompletableFuture.runAsync(() -> {
                try {
                    this.copyToResources();
                } catch (IOException e) {
                    throw new CompletionException(e);
                }
            }, task -> client.tell(() -> Util.ioPool().execute(task))).thenRunAsync(this::hotReload, client).exceptionally(e -> {
                HaxUtils.LOGGER.warning("Failed to hot swap file system change: " + e);
                return null;
            });
        }
        return CompletableFuture.completedFuture(null);
    }

    HaxResourceInfo resourceInfo();

    /**
     * @return All actions that can be performed on this resource
     */
    List<HaxResourceAction<T>> getActions();

    /**
     * @return If this resource can be hot-reloaded
     */
    boolean canHotReload();

    /**
     * Hot-reloads the resource
     */
    void hotReload();

    default void copyToResources() throws IOException {
        HaxResourceInfo info = this.resourceInfo();
        Path filePath = info.filePath();
        if (filePath == null) {
            return;
        }

        Path modPath = info.modResourcePath();
        if (modPath == null) {
            return;
        }

        try (InputStream is = Files.newInputStream(modPath); OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.TRUNCATE_EXISTING)) {
            IOUtils.copyLarge(is, os);
        }
    }

    /**
     * Gets the icon code for this resource (ex. 0xED0F)
     */
    int getIconCode();
}
