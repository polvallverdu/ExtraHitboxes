/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.polv.extrahitboxes.api.*;
import dev.polv.extrahitboxes.internal.GeckoLibMultiPartMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;

@Mixin(GeoRenderer.class)
public interface GeoRendererMixin<T extends GeoAnimatable> {

    @Shadow T getAnimatable();

    @Inject(method = "renderCubesOfBone", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lsoftware/bernie/geckolib/cache/object/GeoBone;isHidden()Z"))
    default void getBonePositions(PoseStack poseStack, GeoBone bone, VertexConsumer buffer, int packedLight, int packedOverlay, int colour, CallbackInfo ci) {
        if (getAnimatable() instanceof GeckoLibMultiPartEntity<?> multiPartEntity) {
            if (getAnimatable() instanceof GeckoLibMultiPartMob multiPartMob && !multiPartMob.moreHitboxes$isNewRenderTick()) {
                return;
            }
            MultiPart<?> part = multiPartEntity.getEntityHitboxData().getCustomPart(bone.getName());
            if (part != null) {
                //Tick hitboxes
                org.joml.Vector3d localPos = bone.getLocalPosition();
                part.setOverride(new AnimationOverride(new Vec3(localPos.x, localPos.y, localPos.z), bone.getScaleX(), bone.getScaleY()));
                //TODO: Could also update the position of the part directly but that would make separating the library from geckolib more tedious
            } else if (multiPartEntity.getEntityHitboxData().getAnchorData().isAnchor(bone.getName())) {
                Vector3d localPos = bone.getLocalPosition();
                multiPartEntity.getEntityHitboxData().getAnchorData().updatePosition(bone.getName(), new Vec3(localPos.x, localPos.y, localPos.z));
            } else if (multiPartEntity.canSetAnchorPos(bone.getName())) {
                Vector3d localPos = bone.getLocalPosition();
                multiPartEntity.setAnchorPos(bone.getName(), new Vec3(localPos.x, localPos.y, localPos.z));
            } else {
                AttackBoxData attackBoxData = multiPartEntity.getEntityHitboxData().getAttackBoxData();
                HitboxData attackBox = attackBoxData.getAttackBox(bone.getName());
                if (attackBox != null && attackBoxData.isAttackBoxActive(attackBox)) {
                    Vector3d worldPos = bone.getWorldPosition();
                    multiPartEntity.getEntityHitboxData().getAttackBoxData().moveActiveAttackBox(attackBox, new Vec3(worldPos.x, worldPos.y, worldPos.z));
                }
            }
        }
    }

    @Inject(method = "actuallyRender", at = @At(value = "TAIL"))
    public default void updateTick(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour, CallbackInfo ci) {
        if (animatable instanceof GeckoLibMultiPartMob multiPartMob) {
            multiPartMob.moreHitboxes$updateRenderTick();
        }
    }

}
