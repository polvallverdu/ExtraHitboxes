/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.internal;

import dev.polv.extrahitboxes.api.MultiPart;
import net.minecraft.world.phys.EntityHitResult;

public class ProjectileUtilOverride {

    /**
     * Uses {@link MultiPartEntityHitResult} to add the correct part to the EntityHitResult
     */
    public static EntityHitResult modifyPartEntity(EntityHitResult original) {
        if (original == null) {
            return null;
        }
        if (original.getEntity() instanceof MultiPart<?> part) {
            EntityHitResult hitResult = new EntityHitResult(part.getParent(), original.getLocation());
            ((MultiPartEntityHitResult) hitResult).moreHitboxes$setMultiPart(part);
            return hitResult;
        }
        return original;
    }
}
