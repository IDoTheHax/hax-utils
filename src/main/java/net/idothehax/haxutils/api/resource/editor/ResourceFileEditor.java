package net.idothehax.haxutils.api.resource.editor;

import net.idothehax.haxutils.HaxUtils;
import net.idothehax.haxutils.api.resource.HaxEditorEnvironment;
import net.idothehax.haxutils.api.resource.HaxResource;
import net.idothehax.haxutils.api.resource.HaxResourceInfo;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;

public interface ResourceFileEditor<T extends HaxResource<?>> {

    /**
     * Renders this editor to the screen.
     */
    void render();

    /**
     * Opens the specified resource in the environment.
     *
     * @param environment The environment to open the resource in
     * @param resource    The resource to open
     */
    void open(HaxEditorEnvironment environment, T resource);

    /**
     * Saves the specified data to the specified resource and hot reloads it.
     *
     * @param data     The data to write to the resource file
     * @param resource The resource to write to
     */
    default void save(byte[] data, HaxResource<?> resource) {
        HaxResourceInfo info = resource.resourceInfo();
        CompletableFuture.runAsync(() -> {
            try {
                if (info.isStatic()) {
                    throw new IOException("Read-only resource");
                }

                Path path = info.filePath();
                try (OutputStream os = Files.newOutputStream(path)) {
                    os.write(data);
                }

                Path modPath = info.modResourcePath();
                if (modPath == null) {
                    return;
                }

                // Copy from build to resources
                try (InputStream is = Files.newInputStream(path); OutputStream os = Files.newOutputStream(modPath, StandardOpenOption.TRUNCATE_EXISTING)) {
                    IOUtils.copyLarge(is, os);
                }
            } catch (Exception e) {
                HaxUtils.LOGGER.warning("Failed to write resource: " + info.location() + " " + e);
            }
        }, Util.ioPool()).thenRunAsync(resource::hotReload, Minecraft.getInstance()).exceptionally(e -> {
            HaxUtils.LOGGER.warning("Failed to hot-swap resource: " + info.location() + " " + e);
            return null;
        });
    }
}
