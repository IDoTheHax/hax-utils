package net.idothehax.haxutils.api.client.render;

import org.lwjgl.system.NativeResource;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL31C.GL_UNIFORM_BUFFER;

/**
 * Manages the global gui context variables.
 *
 * @author Ocelot
 */
public class GuiInfo implements NativeResource {

    private static final int SIZE = Float.BYTES;

    private float guiScale;
    private boolean enabled;

    private void write(ByteBuffer buffer) {
        buffer.putFloat(0, this.guiScale);
    }

    /**
    /**
     * @return The far clipping plane of the frustum
     */
    public float getGuiScale() {
        return this.guiScale;
    }

    /**
     * @return Whether the gui is currently being drawn
     */
    public boolean isGuiRendering() {
        return this.enabled;
    }

    @Override
    public void free() {
    }
}