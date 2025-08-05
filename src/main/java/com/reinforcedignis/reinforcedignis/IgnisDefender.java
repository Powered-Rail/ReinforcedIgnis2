package com.reinforcedignis.reinforcedignis;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ReinforcedIgnisMod.MODID)
public class IgnisDefender {
    private static boolean isHealthChangeAllowed = false;
    private static final String ALLOWED_SOURCE = "com.reinforcedignis.reinforcedignis";

    public static boolean isHealthChangeAllowed() {
        // 检查是否在允许的上下文（如来自 IgnisDefender 的逻辑）
        return isHealthChangeAllowed || isCalledFromAllowedSource();
    }

    private static boolean isCalledFromAllowedSource() {
        // 检查调用栈，确保只有 ReinforcedIgnis 的代码能修改生命值
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(ALLOWED_SOURCE)) {
                return true;
            }
        }
        return false;
    }

    public static void allowHealthChange(Runnable action) {
        isHealthChangeAllowed = true;
        try {
            action.run();
        } finally {
            isHealthChangeAllowed = false;
        }
    }

    public static void handleNBTData(Ignis_Entity ignis, CompoundTag compound) {
        allowHealthChange(() -> {
            float oldHealth = ignis.getHealth();
            ignis.readAdditionalSaveData(compound);
            float newHealth = ignis.getHealth();

            // 如果血量减少，触发无敌帧
            if (newHealth < oldHealth) {
                IgnisInvincibilityFrame.startInvincibility(ignis);
            }
        });
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Ignis_Entity ignis) {
            allowHealthChange(() -> ignis.setHealth(ignis.getHealth() - event.getAmount()));
            event.setCanceled(true); // 取消原版伤害计算，改用我们的逻辑
        }
    }
}