package com.reinforcedignis.reinforcedignis.mixin;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.Ignis_Entity;
import com.reinforcedignis.reinforcedignis.IgnisDefender;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ignis_Entity.class)
public abstract class MixinIgnisEntity {
    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"), cancellable = true)
    private void onReadAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        IgnisDefender.handleNBTData((Ignis_Entity) (Object) this, compound);
        ci.cancel(); // 取消原版 NBT 读取逻辑，改用 IgnisDefender 控制
    }
}