package dev.polv.extrahitboxes.neoforge.mixin;

import dev.polv.extrahitboxes.api.MultiPart;
import dev.polv.extrahitboxes.api.MultiPartEntity;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract void setPos(double x, double y, double z);

    @Inject(method = "refreshDimensions", at = @At("HEAD"))
    public void saveYPos(CallbackInfo ci, @Share("oldY") LocalDoubleRef oldY) {
        oldY.set(getY());
    }

    @Inject(method = "refreshDimensions", at = @At("RETURN"))
    public void refreshDimensionsForParts(CallbackInfo ci, @Share("oldY") LocalDoubleRef oldY) {
        if (this instanceof MultiPartEntity<?> multiPartEntity) {
            for (MultiPart<?> part : multiPartEntity.getEntityHitboxData().getCustomParts()) {
                part.getEntity().refreshDimensions();
            }
            if (multiPartEntity.getEntityHitboxData().fixPosOnRefresh()) {
                setPos(getX(), oldY.get(), getZ());
            }
        }
    }

    @ModifyReturnValue(method = "getBoundingBoxForCulling", at = @At("RETURN"))
    public AABB changeCullBox(AABB original) {
        if (this instanceof MultiPartEntity<?> multiPartEntity && multiPartEntity.getEntityHitboxData() != null && multiPartEntity.getEntityHitboxData().hasCustomParts()) {
            return multiPartEntity.getEntityHitboxData().getCullingBounds();
        }
        return original;
    }

    @Inject(method = "setBoundingBox", at = @At("RETURN"))
    public void updateBounds(AABB aABB, CallbackInfo ci) {
        if (this instanceof MultiPartEntity<?> multiPartEntity && multiPartEntity.getEntityHitboxData() != null) {
            multiPartEntity.getEntityHitboxData().makeAttackBounds();
            multiPartEntity.getEntityHitboxData().makeBoundingBoxForCulling();
        }
    }

    @Inject(method = "setId", at = @At("RETURN"))
    public void setPartIds(int id, CallbackInfo ci) {
        if (this instanceof MultiPartEntity<?> multiPartEntity) {
            var list = multiPartEntity.getEntityHitboxData().getCustomParts();
            for (int i = 0; i < list.size(); i++) {
                list.get(i).getEntity().setId(id + i + 1);
            }
        }
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void callRemoveCallback(Entity.RemovalReason removalReason, CallbackInfo ci) {
        if (this instanceof MultiPartEntity<?> multiPartEntity) {
            for (MultiPart<?> part : multiPartEntity.getEntityHitboxData().getCustomParts()) {
                part.getEntity().remove(removalReason);
            }
        }
    }
}
