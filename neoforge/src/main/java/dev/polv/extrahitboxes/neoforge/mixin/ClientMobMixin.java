/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge.mixin;

import dev.polv.extrahitboxes.api.MultiPartEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class ClientMobMixin extends LivingEntity {

    protected ClientMobMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    public void tickCustomParts(CallbackInfo ci) {
        if (this instanceof MultiPartEntity<?> multiPartEntity) {
            if (level() instanceof ClientLevel clientLevel) {
                multiPartEntity.getEntityHitboxData().getAttackBoxData().clientTick(clientLevel);
            }
        }
    }
}
