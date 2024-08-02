package net.idothehax.haxutils.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.idothehax.haxutils.HaxUtils;

public class HaxUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HaxUtils.LOGGER.info("Initializing HaxUtils Client");
    }
}
