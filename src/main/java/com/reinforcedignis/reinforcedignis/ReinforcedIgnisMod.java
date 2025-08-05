package com.reinforcedignis.reinforcedignis;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod("reinforcedignis")
public class ReinforcedIgnisMod {
    public static final String MODID = "reinforcedignis";
    public static final UUID ARMOR_REDUCTION_UUID = UUID.fromString("a5b8c3d0-1e2f-4a3b-9c8d-0e1f2a3b4c5d");

    public ReinforcedIgnisMod() {
        MinecraftForge.EVENT_BUS.register(this);
//        MinecraftForge.EVENT_BUS.register(IgnisAttackHandler.class);
        MinecraftForge.EVENT_BUS.register(IgnisNBTUtils.class);
        MinecraftForge.EVENT_BUS.register(IgnisInvincibilityFrame.class);
        MinecraftForge.EVENT_BUS.register(new IgnisDamageLimiter());
        MinecraftForge.EVENT_BUS.register(IgnisDefender.class);
        MinecraftForge.EVENT_BUS.register(IgnisPhase3ProjectileProtection.class);
    }
}