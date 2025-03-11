package dev.polv.extrahitboxes.neoforge;

import dev.polv.extrahitboxes.ExtraHitboxes;
import net.neoforged.fml.common.Mod;

@Mod(ExtraHitboxes.MOD_ID)
public final class ExtraHitboxesNeoForge {
    public ExtraHitboxesNeoForge() {
        // Run our common setup.
        ExtraHitboxes.init();
    }
}
