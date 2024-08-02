package net.idothehax.haxutils.api.resource;

import net.minecraft.resources.ResourceLocation;

/**
 * An environment where files can be opened, edited, and managed.
 */
public interface HaxEditorEnvironment {

    void open(HaxResource<?> resource, ResourceLocation editor);

    HaxResourceManager getResourceManager();
}
