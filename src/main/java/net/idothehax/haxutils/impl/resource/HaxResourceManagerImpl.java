package net.idothehax.haxutils.impl.resource;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.*;
import net.idothehax.haxutils.HaxUtils;
import net.idothehax.haxutils.api.client.render.HaxRenderer;
import net.idothehax.haxutils.api.resource.HaxResource;
import net.idothehax.haxutils.api.resource.HaxResourceInfo;
import net.idothehax.haxutils.api.resource.HaxResourceLoader;
import net.idothehax.haxutils.api.resource.HaxResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.NativeResource;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Manages all Hax resources
 */
public class HaxResourceManagerImpl implements HaxResourceManager, NativeResource {

    private static final AtomicInteger WATCHER_ID = new AtomicInteger(1);
    private final ObjectList<HaxResourceLoader> loaders;
    private final List<HaxPackResources> packResources;
    private final Object2ObjectMap<Path, PackResourceListener> watchers;

    private ResourceManager serverResourceManager = ResourceManager.Empty.INSTANCE;

    public HaxResourceManagerImpl() {
        this.loaders = new ObjectArrayList<>(8);
        this.packResources = new LinkedList<>();
        this.watchers = new Object2ObjectArrayMap<>();
    }

    public void addHaxLoaders(HaxRenderer renderer) {
        //this.addLoader(new TextResourceLoader());
    }

    /**
     * Adds a resource loader to the resource manager
     */
    public void addLoader(HaxResourceLoader loader) {
        this.loaders.add(loader);
    }


    @Override
    public ResourceManager clientResources() {
        return Minecraft.getInstance().getResourceManager();
    }

    @Override
    public ResourceManager serverResources() {
        return this.serverResourceManager;
    }

    @Override
    public @Nullable HaxResource<?> getHaxResource(String namespace, String path) {
        for (HaxPackResources pack : this.packResources) {
            HaxResource<?> resource = pack.getHaxResource(namespace, path);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public void free() {
        for (PackResourceListener listener : this.watchers.values()) {
            try {
                listener.close();
            } catch (IOException e) {
                HaxUtils.LOGGER.warning("Error closing watch service: " + listener.getPath() + " " + e);
            }
        }
        this.watchers.clear();
        for (HaxPackResources resources : this.packResources) {
            resources.free();
        }
        this.packResources.clear();
        WATCHER_ID.set(1);
    }

    /**
     * @return All pack folders
     */
    public List<HaxPackResources> getAllPacks() {
        return this.packResources;
    }

    private record Preparations(List<HaxPackResources> packs, Object2ObjectMap<Path, PackResourceListener> watchers) {
    }

    private static class PackResourceListener implements Closeable {

        private final Path path;
        private final WatchService watchService;
        private final ObjectSet<Path> watchedDirectories;
        private final ObjectSet<Path> ignoredPaths;
        private final Object2ObjectMap<Path, HaxResource<?>> resources;
        private final Thread watchThread;

        public PackResourceListener(Path path) {
            WatchService watchService;
            try {
                watchService = path.getFileSystem().newWatchService();
            } catch (Exception ignored) {
                watchService = null;
            }

            this.path = path;
            this.watchService = watchService;
            this.watchedDirectories = ObjectSets.synchronize(new ObjectArraySet<>());
            this.ignoredPaths = ObjectSets.synchronize(new ObjectArraySet<>());
            this.resources = new Object2ObjectOpenHashMap<>();

            if (this.watchService != null) {
                this.watchThread = new Thread(this::run, "Hax File Watcher Thread " + WATCHER_ID.getAndIncrement());
                this.watchThread.start();
            } else {
                this.watchThread = null;
            }
        }

        @SuppressWarnings("unchecked")
        private void run() {
            while (true) {
                WatchKey key;
                try {
                    key = this.watchService.take();
                } catch (ClosedWatchServiceException e) {
                    return;
                } catch (Throwable t) {
                    HaxUtils.LOGGER.warning("Error waiting for file " + t);
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> pathWatchEvent = (WatchEvent<Path>) event;
                    Path path = ((Path) key.watchable()).resolve(pathWatchEvent.context());
                    if (this.ignoredPaths.add(path)) {
                        HaxResource<?> resource = this.resources.get(path);
                        if (resource != null) {
                            resource.onFileSystemChange(pathWatchEvent).thenRun(() -> this.ignoredPaths.remove(path));
                        }
                    }
                }

                key.reset();
            }
        }

        public void listen(HaxResource<?> resource, Path listenPath) throws IOException {
            Path folder = listenPath.getParent();
            if (folder == null) {
                return;
            }

            this.resources.put(listenPath, resource);
            if (this.watchedDirectories.add(folder)) {
                folder.register(this.watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
            }
        }

        @Override
        public void close() throws IOException {
            this.watchService.close();

            try {
                this.watchThread.join();
            } catch (InterruptedException e) {
                throw new IOException("Failed to stop watcher thread", e);
            }
        }

        public Path getPath() {
            return this.path;
        }
    }
}
