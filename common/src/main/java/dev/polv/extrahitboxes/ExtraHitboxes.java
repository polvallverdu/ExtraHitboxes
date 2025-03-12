/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes;

import dev.polv.extrahitboxes.internal.HitboxDataLoader;
import dev.polv.extrahitboxes.platform.Services;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExtraHitboxes {
    public static final String MOD_ID = "extrahitboxes";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        Services.RESOURCE_PACK_PROVIDER.register(PackType.SERVER_DATA, HitboxDataLoader.HITBOX_DATA);
    }
}
