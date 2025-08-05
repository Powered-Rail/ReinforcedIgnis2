package com.reinforcedignis.reinforcedignis;

import com.github.L_Ender.cataclysm.config.CMConfig;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.github.L_Ender.cataclysm.init.ModEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ReinforcedIgnisMod.MODID)
public class IgnisAttackEnhancer {
    private static final int MAX_STACKS = 5;
    private static final int EXPIRE_DURATION = 5 * 20;
    private static final float ARMOR_REDUCTION_PER_STACK = 0.20f;

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        Entity source = event.getSource().getEntity();
        LivingEntity target = event.getEntity();

        // 检查攻击者是否是Ignis
        if (source instanceof Ignis_Entity ignis) {
            handleIgnisStacks(target, ignis);
            handlePhaseBasedDamage(target, ignis);
        }
    }

    private static void handleIgnisStacks(LivingEntity target, Ignis_Entity ignis) {
        target.removeEffect(ModEffect.EFFECTBLAZING_BRAND.get());
        int currentStacks = IgnisNBTUtils.getIgnisStacks(target);

        if (currentStacks < MAX_STACKS) {
            currentStacks++;
            IgnisNBTUtils.setIgnisStacks(target, currentStacks);
        }

        IgnisNBTUtils.setExpireTime(target, target.tickCount + EXPIRE_DURATION);

        float healAmount = 5 * (float) CMConfig.IgnisHealingMultiplier * (float) (currentStacks + 1);
        ignis.heal(healAmount);
        updateArmorReduction(target, currentStacks);
    }

    private static void handlePhaseBasedDamage(LivingEntity target, Ignis_Entity ignis) {
        int bossPhase = ignis.getBossPhase();

        // 通用击杀伤害（所有阶段都有）
        float genericKillDamage = 0;
        switch (bossPhase) {
            case 0 -> genericKillDamage = target.getMaxHealth() * 0.0125f; // 1.25%
            case 1 -> genericKillDamage = target.getMaxHealth() * 0.025f;  // 2.5%
            case 2 -> genericKillDamage = target.getMaxHealth() * 0.05f;   // 5%
        }

        if (genericKillDamage > 0) {
            DamageSource genericDamage = target.damageSources().generic();
            target.hurt(genericDamage, genericKillDamage);
        }

        // SetHealth伤害（无视伤害免疫）
        float setHealthDamage = 0;
        switch (bossPhase) {
            case 1 -> setHealthDamage = target.getMaxHealth() * 0.0125f; // 1.25%
            case 2 -> setHealthDamage = target.getMaxHealth() * 0.025f;  // 2.5%
            // 阶段0没有setHealth伤害
        }

        if (setHealthDamage > 0) {
            float newHealth = target.getHealth() - setHealthDamage;
            if (newHealth < 0) newHealth = 0;
            target.setHealth(newHealth);
        }
    }

    private static void updateArmorReduction(LivingEntity target, int stacks) {
        AttributeInstance armorAttr = target.getAttribute(Attributes.ARMOR);
        if (armorAttr == null) return;

        armorAttr.removeModifier(ReinforcedIgnisMod.ARMOR_REDUCTION_UUID);
        if (stacks > 0) {
            float reduction = Math.min(stacks * ARMOR_REDUCTION_PER_STACK, 1.0f);
            double amount = -armorAttr.getBaseValue() * reduction;
            armorAttr.addPermanentModifier(new AttributeModifier(
                    ReinforcedIgnisMod.ARMOR_REDUCTION_UUID,
                    "ignis_armor_reduction",
                    amount,
                    AttributeModifier.Operation.ADDITION
            ));
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        int expireTime = IgnisNBTUtils.getExpireTime(entity);

        if (expireTime > 0 && entity.tickCount >= expireTime) {
            int oldStacks = IgnisNBTUtils.getIgnisStacks(entity);
            if (oldStacks > 0) {
                IgnisNBTUtils.clearIgnisStacks(entity);
                updateArmorReduction(entity, 0);
            }
        }
    }
}