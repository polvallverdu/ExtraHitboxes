package dev.polv.extrahitboxes.neoforge.mixin;

import dev.polv.extrahitboxes.api.MultiPart;
import dev.polv.extrahitboxes.api.MultiPartEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class ClientEntityMixin {

    @Inject(method = "onClientRemoval", at = @At("RETURN"))
    public void removePartsOnClientRemoval(CallbackInfo ci) {
        if (this instanceof MultiPartEntity<?> multiPartEntity) {
            for (MultiPart<?> part : multiPartEntity.getEntityHitboxData().getCustomParts()) {
                part.getEntity().remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }
}
