/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.platform;

import dev.polv.extrahitboxes.ExtraHitboxes;
import dev.polv.extrahitboxes.ResourcePackRegistry;
import dev.polv.extrahitboxes.api.MultiPart;
import dev.polv.extrahitboxes.platform.services.IPlatformHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final ResourcePackRegistry RESOURCE_PACK_PROVIDER = load(ResourcePackRegistry.class);
    public static final MultiPart.Factory MULTI_PART = Services.load(MultiPart.Factory.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        ExtraHitboxes.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
