package net.idothehax.haxutils.client;

import net.fabricmc.api.ModInitializer;
import net.idothehax.haxutils.HaxUtils;

public class HaxUtilsClient implements ModInitializer {
    @Override
    public void onInitialize() {
        HaxUtils.LOGGER.info("Initializing HaxUtils Client");
    }
}
