package com.reinforcedignis.reinforcedignis;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IgnisDamageLimiter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String LAST_VALID_HEALTH = "LastValidHealth";
    private static final float MAX_DAMAGE_PER_HIT = 20.0f;
    private static final float PHASE1_MIN_HEALTH = 300.0f;
    private static final float PHASE2_MIN_HEALTH = 150.0f;

    private float getMinimumHealth(Ignis_Entity ignis) {
        return switch(ignis.getBossPhase()) {
            case 0 -> PHASE1_MIN_HEALTH;
            case 1 -> PHASE2_MIN_HEALTH;
            default -> 0.0f;
        };
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        if(!(event.getEntity() instanceof Ignis_Entity ignis)) return;

        IgnisDefender.allowHealthChange(() -> {
            CompoundTag data = ignis.getPersistentData();
            float minHealth = getMinimumHealth(ignis);

            if(!data.contains(LAST_VALID_HEALTH)) {
                float currentHealth = Math.max(ignis.getHealth(), minHealth);
                data.putFloat(LAST_VALID_HEALTH, currentHealth);
            }

            float lastValidHealth = data.getFloat(LAST_VALID_HEALTH);
            float attemptedDamage = event.getAmount();
            float allowedDamage = Math.min(attemptedDamage, MAX_DAMAGE_PER_HIT);
            float newHealth = Math.max(lastValidHealth - allowedDamage, minHealth);

            LOGGER.debug("焰魔受到伤害: 原始={}, 实际={}, 阶段={}",
                    attemptedDamage, allowedDamage, ignis.getBossPhase());

            data.putFloat(LAST_VALID_HEALTH, newHealth);
            ignis.setHealth(newHealth);

            if(newHealth <= 0.0f) {
                ignis.die(event.getSource());
            }
        });

        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {
        if(!(event.getEntity() instanceof Ignis_Entity ignis)) return;

        IgnisDefender.allowHealthChange(() -> {
            CompoundTag data = ignis.getPersistentData();
            if(data.contains(LAST_VALID_HEALTH)) {
                float maxHealth = (float)ignis.getAttributeValue(Attributes.MAX_HEALTH);
                float lastHealth = data.getFloat(LAST_VALID_HEALTH);
                float newHealth = Math.min(lastHealth + event.getAmount(), maxHealth);
                data.putFloat(LAST_VALID_HEALTH, newHealth);
                ignis.setHealth(newHealth);
            }
        });

        event.setCanceled(true);
    }
}