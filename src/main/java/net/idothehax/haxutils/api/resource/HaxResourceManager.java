package net.idothehax.haxutils.api.resource;

import net.idothehax.haxutils.api.resource.type.McMetaResource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;
import org.jetbrains.annotations.Nullable;

public interface HaxResourceManager {

    default ResourceManager resources(HaxResourceInfo resourceInfo) {
        return resourceInfo.packType() == PackType.SERVER_DATA ? this.serverResources() : this.clientResources();
    }

    ResourceManager clientResources();

    ResourceManager serverResources();

    @Nullable
    HaxResource<?> getHaxResource(String namespace, String path);

    default @Nullable HaxResource<?> getHaxResource(ResourceLocation location) {
        return this.getHaxResource(location.getNamespace(), location.getPath());
    }

    default @Nullable ResourceMetadata getResourceMetadata(ResourceLocation location) {
        return this.getResourceMetadata(location.getNamespace(), location.getPath());
    }

    default @Nullable ResourceMetadata getResourceMetadata(String namespace, String path) {
        HaxResource<?> resource = this.getHaxResource(namespace, path);
        return resource instanceof McMetaResource mcMetaResource ? mcMetaResource.metadata() : null;
    }
}