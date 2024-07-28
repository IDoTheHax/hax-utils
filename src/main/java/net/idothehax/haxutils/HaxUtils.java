package net.idothehax.haxutils;

import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class HaxUtils implements ModInitializer {
    public static final String MOD_ID = "haxutils";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        HaxUtils.LOGGER.info("Initializing HaxUtils");
    }
}
