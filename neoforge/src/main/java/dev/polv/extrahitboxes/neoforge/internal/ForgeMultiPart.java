/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge.internal;

import dev.polv.extrahitboxes.api.AnimationOverride;
import dev.polv.extrahitboxes.api.HitboxData;
import dev.polv.extrahitboxes.api.MultiPart;
import dev.polv.extrahitboxes.api.MultiPartEntity;
import com.google.auto.service.AutoService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.PartEntity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class ForgeMultiPart<T extends Mob & MultiPartEntity<T>> extends PartEntity<T> implements MultiPart<T> {
    private final EntityDimensions size;
    private final Vec3 offset;
    private final String partName;
    @Nullable
    private AnimationOverride animationOverride;

    public ForgeMultiPart(T parent, HitboxData hitboxData) {
        super(parent);
        this.size = EntityDimensions.scalable(hitboxData.width(), hitboxData.height());
        this.offset = hitboxData.pos();
        this.partName = hitboxData.name();
        this.noPhysics = true;
        this.refreshDimensions();
    }

    @Override
    public @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        return getParent().interact(player, hand);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (isInvulnerableTo(source)) {
            return false;
        }
        return getParent().partHurt(this, source, amount);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder arg) {

    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        setRemoved(reason);
    }

    @Override
    public boolean is(@NotNull Entity entity) {
        return this == entity || getParent() == entity;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        if (animationOverride != null) {
            return size.scale(getParent().getScale()).scale(animationOverride.scaleW(), animationOverride.scaleH());
        }
        return size.scale(getParent().getScale());
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public String getPartName() {
        return partName;
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    @Override
    public Vec3 getOffset() {
        return offset;
    }

    @Override
    public void setOverride(AnimationOverride newOverride) {
        if (animationOverride != null && (animationOverride.scaleH() != newOverride.scaleH() || animationOverride.scaleW() != newOverride.scaleW())) {
            animationOverride = newOverride;
            refreshDimensions();
        } else {
            animationOverride = newOverride;
        }
    }

    @Override
    public AnimationOverride getOverride() {
        return animationOverride;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {

    }

    @ApiStatus.Internal
    @AutoService(MultiPart.Factory.class)
    public static class ForgeMultiPartFactory implements MultiPart.Factory {

        @Override
        public <T extends Mob & MultiPartEntity<T>> MultiPart<T> create(T parent, HitboxData hitboxData) {
            return new ForgeMultiPart<>(parent, hitboxData);
        }
    }
}
