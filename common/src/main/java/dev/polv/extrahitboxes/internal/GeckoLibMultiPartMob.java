/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.internal;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
@ApiStatus.Internal
public interface GeckoLibMultiPartMob {

    /**
     * @return {@code true} if the GeckoLib renderer has not yet done a render pass this game tick
     */
    @ApiStatus.Internal
    boolean moreHitboxes$isNewRenderTick();

    /**
     * Updates the current render tick. Called after the first GeckoLib render pass
     */
    @ApiStatus.Internal
    void moreHitboxes$updateRenderTick();
}
