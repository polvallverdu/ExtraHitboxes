/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge.mixin;

import dev.polv.extrahitboxes.api.MultiPart;
import dev.polv.extrahitboxes.internal.MultiPartEntityHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * Adds a {@link MultiPart} field that is used to determine the exact part hit during an attack
 */
@Mixin(EntityHitResult.class)
public abstract class EntityHitResultMixin implements MultiPartEntityHitResult {
    @Unique
    @Nullable
    private MultiPart<?> moreHitboxes$part;

    @Override
    public void moreHitboxes$setMultiPart(MultiPart<?> part) {
        this.moreHitboxes$part = part;
    }

    @Override
    public @Nullable MultiPart<?> moreHitboxes$getMultiPart() {
        return moreHitboxes$part;
    }
}
