package com.example.versionspoofer.mixin;

import com.example.versionspoofer.VersionSpooferClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.NetworkState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Миксин для подмены версии в пакете Handshake при подключении к серверу
 * Это ключевой момент - сервер получает версию из этого пакета
 */
@Mixin(HandshakeC2SPacket.class)
public class HandshakeC2SPacketMixin {
    
    @ModifyVariable(method = "<init>(ILjava/lang/String;IZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static int modifyProtocolVersion(int protocolVersion) {
        if (VersionSpooferClient.spoofedVersion != null) {
            int newVersion = VersionSpooferClient.getProtocolVersion(VersionSpooferClient.spoofedVersion);
            System.out.println("[VersionSpoofer] Подмена версии в Handshake: " + 
                             VersionSpooferClient.spoofedVersion + " (протокол: " + newVersion + ")");
            return newVersion;
        }
        return protocolVersion;
    }
}
