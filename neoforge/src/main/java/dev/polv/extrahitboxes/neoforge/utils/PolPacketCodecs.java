/**
 * This file contains code from DarkPred's project
 * Original Copyright (c) 2024 DarkPred
 * Modified by Pol (c) 2025
 *
 * Licensed under MIT License
 */
package dev.polv.extrahitboxes.neoforge.utils;

import dev.polv.extrahitboxes.api.HitboxData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PolPacketCodecs {

    private final static int UTF8_MAX_LENGTH = 32767;

    public static StreamCodec<ByteBuf, HitboxData> HITBOX_DATA = new StreamCodec<ByteBuf, HitboxData>() {
        public HitboxData decode(ByteBuf buf) {
            return new HitboxData(
                    Utf8String.read(buf, UTF8_MAX_LENGTH), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readFloat(), buf.readFloat(), Utf8String.read(buf, UTF8_MAX_LENGTH), buf.readBoolean(), buf.readBoolean()
            );
        }

        public void encode(ByteBuf buf, HitboxData hitbox) {
            Utf8String.write(buf, hitbox.name(), UTF8_MAX_LENGTH);
            buf.writeDouble(hitbox.pos().x);
            buf.writeDouble(hitbox.pos().y);
            buf.writeDouble(hitbox.pos().z);
            buf.writeFloat(hitbox.width());
            buf.writeFloat(hitbox.height());
            Utf8String.write(buf, hitbox.ref(), UTF8_MAX_LENGTH);
            buf.writeBoolean(hitbox.isAttackBox());
            buf.writeBoolean(hitbox.isAnchor());
        }
    };

    public static <T> StreamCodec<ByteBuf, List<T>> createListPacketCodec(StreamCodec<ByteBuf, T> elementCodec) {
        return new StreamCodec<>() {
            public List<T> decode(ByteBuf byteBuf) {
                int amount = byteBuf.readInt();
                List<T> list = new ArrayList<>(amount);
                for (int i = 0; i < amount; i++) {
                    list.add(elementCodec.decode(byteBuf));
                }
                return list;
            }

            public void encode(ByteBuf byteBuf, List<T> list) {
                byteBuf.writeInt(list.size());

                list.forEach(element -> elementCodec.encode(byteBuf, element));
            }
        };
    }
}
