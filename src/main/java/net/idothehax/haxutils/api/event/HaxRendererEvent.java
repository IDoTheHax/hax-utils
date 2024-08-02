package net.idothehax.haxutils.api.event;

import net.idothehax.haxutils.api.client.render.HaxRenderer;

/**
 * Fired when Hax Utils has finished initialization and the renderer is safe to use.
 *
 * @author Ocelot
 */
@FunctionalInterface
public interface HaxRendererEvent {

    /**
     * Called when the Hax renderer is now available.
     *
     * @param renderer The renderer instance
     */
    void onHaxRendererAvailable(HaxRenderer renderer);
}
