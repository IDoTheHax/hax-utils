package net.idothehax.haxutils.api.client.render;

import net.idothehax.haxutils.HaxUtils;
import net.idothehax.haxutils.impl.client.imgui.HaxImGuiImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.system.NativeResource;

public class HaxRenderSystem {
    private static HaxRenderer renderer;

    private HaxRenderSystem() {
    }

    @ApiStatus.Internal
    public static void init() {
        Minecraft client = Minecraft.getInstance();
        if (!(client.getResourceManager() instanceof ReloadableResourceManager resourceManager)) {
            throw new IllegalStateException("Client resource manager is " + client.getResourceManager().getClass());
        }

        renderer = new HaxRenderer();
        HaxImGuiImpl.init(client.getWindow().getWindow());
    }

    /**
     * @return The veil renderer instance
     */
    public static HaxRenderer renderer() {
        return renderer;
    }
}
