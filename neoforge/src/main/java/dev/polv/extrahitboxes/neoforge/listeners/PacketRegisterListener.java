/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge.listeners;

import dev.polv.extrahitboxes.ExtraHitboxes;
import dev.polv.extrahitboxes.neoforge.ExtraHitboxesNeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ExtraHitboxes.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PacketRegisterListener {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ExtraHitboxesNeoForge.SyncHitboxDataPayload.TYPE, ExtraHitboxesNeoForge.SyncHitboxDataPayload.STREAM_CODEC, ExtraHitboxesNeoForge::handleSyncHitboxDataClient);
    }

}
