package net.sixik.orestages;

import net.minecraft.server.level.ServerPlayer;

public interface InterceptedClientboundPacket {
    public void interceptRestrictions(ServerPlayer player);
}
