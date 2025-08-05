package com.reinforcedignis.reinforcedignis;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ReinforcedIgnisMod.MODID)
public class IgnisInvincibilityFrame {
    // 各阶段无敌时间 (ticks)
    private static final int PHASE1_INVINCIBILITY_DURATION = 10;  // 0.5秒
    private static final int PHASE2_INVINCIBILITY_DURATION = 20;  // 1秒
    private static final int PHASE3_INVINCIBILITY_DURATION = 30;  // 1.5秒

    private static final Map<Ignis_Entity, Integer> invincibilityTimers = new HashMap<>();
    private static final Map<Ignis_Entity, Float> storedHealthMap = new HashMap<>();

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Ignis_Entity ignis) {
            // 如果处于无敌状态，取消此次伤害
            if (invincibilityTimers.containsKey(ignis)) {
                event.setCanceled(true);
                return;
            }

            // 如果实体正处于受伤动画中(hurtTime > 0)，启动无敌帧
            if (ignis.hurtTime > 0) {
                storedHealthMap.put(ignis, ignis.getHealth());
                invincibilityTimers.put(ignis, 0);
                event.setCanceled(true); // 取消此次伤害，使用无敌帧替代
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        // 允许在无敌期间回血
        if (event.getEntity() instanceof Ignis_Entity ignis && invincibilityTimers.containsKey(ignis)) {
            event.setAmount(event.getAmount() * 1.5f); // 无敌期间回血效果增强50%
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            invincibilityTimers.entrySet().removeIf(entry -> {
                Ignis_Entity ignis = entry.getKey();

                // 如果Ignis死亡或不存在，清除数据
                if (!ignis.isAlive()) {
                    storedHealthMap.remove(ignis);
                    return true;
                }

                int timer = entry.getValue();
                int maxDuration = getInvincibilityDuration(ignis);

                // 在无敌期间保持血量不变
                if (timer < maxDuration) {
                    Float health = storedHealthMap.get(ignis);
                    if (health != null) {
                        // 保持血量不低于存储值，但允许回血
                        ignis.setHealth(Math.max(health, ignis.getHealth()));
                    }
                    entry.setValue(timer + 1); // 增加计时器
                    return false;
                }
                // 无敌时间结束
                else {
                    storedHealthMap.remove(ignis);
                    return true;
                }
            });
        }
    }
    public static void startInvincibility(Ignis_Entity ignis) {
        storedHealthMap.put(ignis, ignis.getHealth());
        invincibilityTimers.put(ignis, 0);
    }
    private static int getInvincibilityDuration(Ignis_Entity ignis) {
        // 根据阶段返回不同的无敌时间
        return switch (ignis.getBossPhase()) {
            case 1 -> PHASE2_INVINCIBILITY_DURATION;
            case 2 -> PHASE3_INVINCIBILITY_DURATION;
            default -> PHASE1_INVINCIBILITY_DURATION;
        };
    }
}