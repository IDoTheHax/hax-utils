package net.idothehax.haxutils;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.idothehax.haxutils.api.client.editor.EditorManager;
import net.idothehax.haxutils.api.client.render.HaxRenderSystem;
import net.idothehax.haxutils.api.client.render.HaxRenderer;
import net.idothehax.haxutils.impl.client.editor.DemoEditor;
import net.idothehax.haxutils.impl.client.imgui.HaxImGuiImpl;
import net.idothehax.haxutils.impl.resource.HaxResourceManagerImpl;
import net.idothehax.haxutils.platform.HaxEventPlatform;
import net.minecraft.client.KeyMapping;
import org.jetbrains.annotations.ApiStatus;

import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL32C.GL_DEPTH_CLAMP;

public class HaxUtilsClient implements ClientModInitializer {
    private static final HaxResourceManagerImpl RESOURCE_MANAGER = new HaxResourceManagerImpl();

    public static final KeyMapping MOUSE_KEY = new KeyMapping("key.hax.mouse", InputConstants.Type.KEYSYM, InputConstants.KEY_F7, "key.categories.hax_utils");
    public static final KeyMapping EDITOR_KEY = new KeyMapping("key.hax.editor", InputConstants.Type.KEYSYM, InputConstants.KEY_F6, "key.categories.hax");

    @Override
    public void onInitializeClient() {
        HaxImGuiImpl.setImGuiPath();

        HaxEventPlatform.INSTANCE.onHaxRendererAvailable(renderer -> {

            RESOURCE_MANAGER.addHaxLoaders(renderer);
            if (HaxRenderer.hasImGui()) {
                EditorManager editorManager = renderer.getEditorManager();

                // debug editors
                editorManager.add(new DemoEditor());
                //editorManager.add(new PostEditor());
                //editorManager.add(new ShaderEditor());
                //editorManager.add(new TextureEditor());
                //editorManager.add(new OpenCLEditor());
                //editorManager.add(new DeviceInfoViewer());
                //editorManager.add(new DeferredEditor());
                //editorManager.add(new LightEditor());
                //editorManager.add(new FramebufferEditor());
                //editorManager.add(new ResourceManagerEditor());
            }
            glEnable(GL_DEPTH_CLAMP); // TODO add config option
        });

        KeyBindingHelper.registerKeyBinding(EDITOR_KEY);
        KeyBindingHelper.registerKeyBinding(MOUSE_KEY);
    }

    @ApiStatus.Internal
    public static void initRenderer() {
        HaxRenderSystem.init();
    }
}
