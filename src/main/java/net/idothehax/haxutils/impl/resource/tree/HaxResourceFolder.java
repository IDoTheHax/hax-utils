package net.idothehax.haxutils.impl.resource.tree;

import net.idothehax.haxutils.api.resource.HaxResource;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * A resource folder for a tree-structure
 */
public class HaxResourceFolder {

    private final String name;
    private final Map<String, HaxResourceFolder> subFolders = new TreeMap<>();
    private final Map<String, HaxResource<?>> resources = new TreeMap<>();

    public HaxResourceFolder(String name) {
        this.name = name;
    }

    /**
     * Adds a resource to this folder, creating sub-folders if necessary
     *
     * @param path     The path of the resource
     * @param resource The resource to add
     */
    public void addResource(String path, HaxResource<?> resource) {
        // If the path contains a slash, we need to create a sub-folder
        if (path.contains("/")) {
            String[] parts = path.split("/", 2);
            HaxResourceFolder folder = this.subFolders.computeIfAbsent(parts[0], HaxResourceFolder::new);
            folder.addResource(parts[1], resource);
            return;
        }

        this.resources.put(path, resource);
    }

    /**
     * Adds a folder to this folder
     *
     * @param folder The folder to add, with a pre-known name
     */
    public void addFolder(HaxResourceFolder folder) {
        this.subFolders.put(folder.name, folder);
    }

    /**
     * @return An iterable collection of all folders contained within this folder
     */
    public Collection<HaxResourceFolder> getSubFolders() {
        return this.subFolders.values();
    }

    /**
     * @return An iterable collection of all resources contained within this folder
     */
    public Iterable<HaxResource<?>> getResources() {
        return this.resources.values();
    }

    public String getName() {
        return this.name;
    }

    public @Nullable HaxResourceFolder getFolder(String name) {
        return this.subFolders.get(name);
    }

    public @Nullable HaxResource<?> getResource(String name) {
        return this.resources.get(name);
    }
}