package net.idothehax.haxutils.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.idothehax.haxutils.api.event.HaxRendererEvent;

/**
 * Fired when Hax Utils has finished initialization and the renderer is safe to use.
 *
 * @author Ocelot
 */
@FunctionalInterface
public interface FabricHaxRendererEvent extends HaxRendererEvent {

    Event<HaxRendererEvent> EVENT = EventFactory.createArrayBacked(HaxRendererEvent.class, events -> renderer -> {
        for (HaxRendererEvent event : events) {
            event.onHaxRendererAvailable(renderer);
        }
    });
}
