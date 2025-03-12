/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.internal;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;

/**
 * Mixin interface injected into GeoEntityRenderer.
 */
@ApiStatus.NonExtendable
@ApiStatus.Internal
public interface MultiPartGeoEntityRenderer {

    @ApiStatus.Internal
    void moreHitboxes$removeTickForEntity(Entity entity);
}
