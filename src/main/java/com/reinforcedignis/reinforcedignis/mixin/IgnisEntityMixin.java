package com.reinforcedignis.reinforcedignis.mixin;

import com.reinforcedignis.reinforcedignis.IgnisDefender;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class) // 改为实际包含 setHealth 的类
public abstract class IgnisEntityMixin {
    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void onSetHealth(float health, CallbackInfo ci) {
        if (!IgnisDefender.isHealthChangeAllowed()) {
            ci.cancel();
        }
    }
}