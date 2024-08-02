package net.idothehax.haxutils.api.resource.type;

import net.idothehax.haxutils.api.resource.HaxResource;
import net.idothehax.haxutils.api.resource.HaxResourceAction;
import net.idothehax.haxutils.api.resource.HaxResourceInfo;
import net.minecraft.server.packs.resources.ResourceMetadata;

import java.util.List;

public record McMetaResource(HaxResourceInfo resourceInfo, ResourceMetadata metadata) implements HaxResource<McMetaResource> {

    @Override
    public List<HaxResourceAction<McMetaResource>> getActions() {
        return List.of();
    }

    @Override
    public boolean canHotReload() {
        return false;
    }

    @Override
    public void hotReload() {
    }

    @Override
    public int getIconCode() {
        return 0xECEA; // Info file icon
    }
}
