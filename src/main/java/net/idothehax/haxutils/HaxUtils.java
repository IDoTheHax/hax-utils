package net.idothehax.haxutils;

import net.fabricmc.api.ModInitializer;
import net.idothehax.haxutils.api.client.imgui.HaxImGui;
import net.idothehax.haxutils.impl.client.imgui.HaxImGuiImpl;
import net.idothehax.haxutils.platform.HaxPlatform;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;
import java.util.logging.Logger;

public class HaxUtils implements ModInitializer {
    public static final String MOD_ID = "haxutils";
    public static final Logger LOGGER = (Logger) Logger.getLogger(MOD_ID);
    public static final boolean IMGUI;
    //private static final HaxPlatform PLATFORM = ServiceLoader.load(HaxPlatform.class).findFirst().orElseThrow(() -> new RuntimeException("Hax Utils expected platform implementation"));

    static {
        IMGUI = System.getProperty("hax.disableImgui") == null;
    }

    @Override
    public void onInitialize() {
        HaxUtils.LOGGER.info("Initializing HaxUtils");
    }

    public static HaxImGui beginImGui() {
        HaxImGui imGui = HaxImGuiImpl.get();
        imGui.begin();
        return imGui;
    }

    public static void endImGui() {
        HaxImGuiImpl.get().end();
    }

    public static ResourceLocation haxUtilsPath(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
