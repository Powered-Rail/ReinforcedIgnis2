package com.reinforcedignis.reinforcedignis;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ReinforcedIgnisMod.MODID)
public class IgnisPhase3ProjectileProtection {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        // 检查是否为三阶段焰魔且伤害来源是弹射物
        if (entity instanceof Ignis_Entity ignis &&
                ignis.getBossPhase() == 2 &&
                source.is(DamageTypeTags.IS_PROJECTILE)) { // 使用 DamageTypeTags 判断

            event.setCanceled(true); // 取消弹射物伤害
        }
    }
}