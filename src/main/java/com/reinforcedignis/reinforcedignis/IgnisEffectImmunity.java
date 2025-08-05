package com.reinforcedignis.reinforcedignis;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
@Mod.EventBusSubscriber(modid = ReinforcedIgnisMod.MODID)
public class IgnisEffectImmunity {
    // 允许的效果列表
    private static final Set<MobEffect> ALLOWED_EFFECTS = Set.of(
            // 正面效果
            MobEffects.MOVEMENT_SPEED,
            MobEffects.DIG_SPEED,
            MobEffects.DAMAGE_BOOST,
            MobEffects.HEAL,
            MobEffects.REGENERATION,
            MobEffects.DAMAGE_RESISTANCE,
            MobEffects.FIRE_RESISTANCE,
            MobEffects.WATER_BREATHING,
            MobEffects.INVISIBILITY,
            MobEffects.NIGHT_VISION,
            MobEffects.HEALTH_BOOST,
            MobEffects.ABSORPTION,
            MobEffects.SATURATION,
            MobEffects.LUCK,
            MobEffects.SLOW_FALLING,
            MobEffects.CONDUIT_POWER,
            MobEffects.DOLPHINS_GRACE
    );

    @SubscribeEvent
    public static void onEffectCheck(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Ignis_Entity)) {
            return;
        }

        MobEffect effect = event.getEffectInstance().getEffect();

        // 检查效果是否在允许列表中
        if (!ALLOWED_EFFECTS.contains(effect)) {
            event.setResult(MobEffectEvent.Applicable.Result.DENY);
        }
    }
}