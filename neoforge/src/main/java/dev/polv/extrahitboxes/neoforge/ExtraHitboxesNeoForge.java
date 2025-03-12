/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge;

import dev.polv.extrahitboxes.ExtraHitboxes;
import dev.polv.extrahitboxes.api.HitboxData;
import dev.polv.extrahitboxes.internal.HitboxDataLoader;
import dev.polv.extrahitboxes.neoforge.utils.PolPacketCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiStatus.Internal
@Mod(ExtraHitboxes.MOD_ID)
public final class ExtraHitboxesNeoForge {
    private static final ResourceLocation SYNC_HITBOX_DATA_ID = ResourceLocation.fromNamespaceAndPath(ExtraHitboxes.MOD_ID,
            "sync_hitbox_data");

    public ExtraHitboxesNeoForge() {
        // Run our common setup.
        ExtraHitboxes.init();

        // Register event listeners
        NeoForge.EVENT_BUS.addListener(this::onDatapackSync);
    }

    private void onDatapackSync(OnDatapackSyncEvent event) {
        // Send hitbox data to the player that just joined or to all players if null
        if (event.getPlayer() != null) {
            PacketDistributor.sendToPlayer(event.getPlayer(),
                    new SyncHitboxDataPayload(HitboxDataLoader.HITBOX_DATA.getHitboxData()));
        } else {
            PacketDistributor.sendToAllPlayers(
                    new SyncHitboxDataPayload(HitboxDataLoader.HITBOX_DATA.getHitboxData()));
        }
    }

    public static void handleSyncHitboxDataClient(final SyncHitboxDataPayload data, final IPayloadContext context) {
        // Make sure we're on the client side
        if (FMLEnvironment.dist == Dist.CLIENT) {
            context.enqueueWork(() -> {
                // Update the hitbox data on the client
                HitboxDataLoader.HITBOX_DATA.replaceData(data.hitboxes());
            });
        }
    }

    /**
     * Custom payload for syncing hitbox data from server to client
     */
    public record SyncHitboxDataPayload(Map<ResourceLocation, List<HitboxData>> hitboxes)
            implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<SyncHitboxDataPayload> TYPE = new CustomPacketPayload.Type<>(SYNC_HITBOX_DATA_ID);

        // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
        // 'name' will be encoded and decoded as a string
        // 'age' will be encoded and decoded as an integer
        // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
        public static final StreamCodec<ByteBuf, SyncHitboxDataPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, PolPacketCodecs.createListPacketCodec(PolPacketCodecs.HITBOX_DATA), 256),
                SyncHitboxDataPayload::hitboxes,
                SyncHitboxDataPayload::new
        );


        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
