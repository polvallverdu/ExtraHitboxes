/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ResourcePackRegistry {
    void register(PackType type, PreparableReloadListener listener);
}
