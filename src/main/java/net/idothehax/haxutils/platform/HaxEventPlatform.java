package net.idothehax.haxutils.platform;

import net.idothehax.haxutils.api.event.HaxRendererEvent;

import java.util.ServiceLoader;

/**
 * Manages platform-specific implementations of event subscriptions.
 *
 * @author Ocelot
 */
public interface HaxEventPlatform {

    HaxEventPlatform INSTANCE = ServiceLoader.load(HaxEventPlatform.class).findFirst().orElseThrow(() -> new RuntimeException("Failed to find platform event provider"));

    void onHaxRendererAvailable(HaxRendererEvent event);
}
