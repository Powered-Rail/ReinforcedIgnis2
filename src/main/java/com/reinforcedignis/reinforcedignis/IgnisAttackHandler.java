//package com.reinforcedignis.reinforcedignis;
//import com.github.L_Ender.cataclysm.config.CMConfig;
//import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
//import com.github.L_Ender.cataclysm.init.ModEffect;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.AttributeInstance;
//import net.minecraft.world.entity.ai.attributes.AttributeModifier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraftforge.event.entity.living.LivingAttackEvent;
//import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//@Mod.EventBusSubscriber(modid = ReinforcedIgnisMod.MODID)
//public class IgnisAttackHandler
//
//{
//    private static final int MAX_STACKS=5;
//    private static final int EXPIRE_DURATION = 5 * 20;
//    private static final float ARMOR_REDUCTION_PER_STACK=0.20f;
//    @SubscribeEvent
//    public static void onLivingAttack(LivingAttackEvent event) {
//        Entity source = event.getSource().getEntity();
//        LivingEntity target = event.getEntity();
//        target.removeEffect(ModEffect.EFFECTBLAZING_BRAND.get());
//
//        if (source instanceof Ignis_Entity ignis) {
//            target.removeEffect(ModEffect.EFFECTBLAZING_BRAND.get());
//            int currentStacks = IgnisNBTUtils.getIgnisStacks(target);
//
//            if (currentStacks < MAX_STACKS) {
//                currentStacks++;
//                IgnisNBTUtils.setIgnisStacks(target, currentStacks);
//            }
//
//            // 修改为 5秒（100 ticks）
//            IgnisNBTUtils.setExpireTime(target, target.tickCount + EXPIRE_DURATION);
//
//            float healAmount = 5 * (float) CMConfig.IgnisHealingMultiplier * (float) (currentStacks + 1);
//            ignis.heal(healAmount);
//            updateArmorReduction(target, currentStacks);
//        }
//    }
//    private static void updateArmorReduction(LivingEntity target,int stacks)
//    {
//        AttributeInstance armorAttr=target.getAttribute(Attributes.ARMOR);
//        if(armorAttr==null) return;
//        armorAttr.removeModifier(ReinforcedIgnisMod.ARMOR_REDUCTION_UUID);
//        if(stacks>0)
//        {
//            float reduction=Math.min(stacks*ARMOR_REDUCTION_PER_STACK,1.0f);
//            double amount=-armorAttr.getBaseValue()*reduction;
//            armorAttr.addPermanentModifier(new AttributeModifier(
//                    ReinforcedIgnisMod.ARMOR_REDUCTION_UUID,
//                    "ignis_armor_reduction",
//                    amount,
//                    AttributeModifier.Operation.ADDITION
//            ));
//        }
//    }
//    @SubscribeEvent
//    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
//        LivingEntity entity = event.getEntity();
//        int expireTime = IgnisNBTUtils.getExpireTime(entity);
//
//        if (expireTime > 0 && entity.tickCount >= expireTime) {
//            int oldStacks = IgnisNBTUtils.getIgnisStacks(entity);
//            if (oldStacks > 0) {
//                IgnisNBTUtils.clearIgnisStacks(entity);
//                updateArmorReduction(entity, 0);
//            }
//        }
//    }
//}
