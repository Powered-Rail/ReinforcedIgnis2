package com.reinforcedignis.reinforcedignis;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ReinforcedIgnisMod.MODID)
public class IgnisTagAdder {

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof Ignis_Entity ignis) {
            updatePhaseTags(ignis);
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Ignis_Entity ignis) {
            updatePhaseTags(ignis);
        }
    }

    private static void updatePhaseTags(Ignis_Entity ignis) {
        // 移除所有阶段tag
        ignis.getTags().remove("ignis_phase1");
        ignis.getTags().remove("ignis_phase2");
        ignis.getTags().remove("ignis_phase3");

        // 根据当前阶段添加对应tag
        switch (ignis.getBossPhase()) {
            case 1 -> ignis.addTag("ignis_phase2");
            case 2 -> ignis.addTag("ignis_phase3");
            default -> ignis.addTag("ignis_phase1"); // 默认阶段1
        }

        // 调试输出
        // System.out.println("Updated Ignis phase tags. Current phase: " + ignis.getBossPhase());
    }
}