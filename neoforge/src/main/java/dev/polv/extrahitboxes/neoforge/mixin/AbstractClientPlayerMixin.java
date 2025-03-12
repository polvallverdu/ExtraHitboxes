/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge.mixin;

import dev.polv.extrahitboxes.api.MultiPart;
import dev.polv.extrahitboxes.api.MultiPartEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends Player {

    protected AbstractClientPlayerMixin(Level arg, BlockPos arg2, float f, GameProfile gameProfile) {
        super(arg, arg2, f, gameProfile);
    }

    @Override
    public boolean isCloseEnough(Entity entity, double dist) {
        if (super.isCloseEnough(entity, dist)) {
            return true;
        }
        if (entity instanceof MultiPartEntity<?> multiPartEntity) {
            for (MultiPart<?> part : multiPartEntity.getEntityHitboxData().getCustomParts()) {
                if (isCloseEnough(part.getEntity(), dist)) {
                    return true;
                }
            }
        }
        return false;
    }
}
