/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.api;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * This interface provides support for hitbox parts that follow GeckoLib animations
 * <p>
 * Required steps to use:
 * <ol>
 *     <li>Add one or more hitboxes in data/{modId}/hitboxes/{entityTypeKey}.json with {@link HitboxData#ref()} set to a bone name</li>
 *     <li>Add a geckolib animation controlling the bone. See {@link AnimationOverride} for supported features</li>
 *     <li>Implement this interface</li>
 *     <li>Call and save {@link EntityHitboxDataFactory#create(Mob) EntityHitboxDataFactory#create(Mob)}</li>
 * </ol>
 *
 * @param <T> the type of the mob implementing this interface
 */
public interface GeckoLibMultiPartEntity<T extends Mob & MultiPartEntity<T>> extends MultiPartEntity<T> {

    /**
     * This method will be called clientside if {@link GeckoLibMultiPartEntity#canSetAnchorPos(String)} returned {@code true}
     * and should be used instead of {@link AnchorData#getAnchorPos(String)} if the position is only needed on the client.
     * <p>
     * Possible use cases are the positioning of geckolib particle listeners
     *
     * @param boneName the name of the bone
     * @param localPos the position of the bone relative to the mobs position
     */
    default void setAnchorPos(String boneName, Vec3 localPos) {
    }

    /**
     * Called to check if the position for the given bone should be calculated and passed to {@link GeckoLibMultiPartEntity#setAnchorPos(String, Vec3)}
     *
     * @param boneName the name of the bone
     * @return {@code true} if the position for the given bone should be calculated
     * @apiNote this method will be called for every bone that is not attached to a {@link MultiPart} or {@link AnchorData}
     */
    default boolean canSetAnchorPos(String boneName) {
        return false;
    }
}
