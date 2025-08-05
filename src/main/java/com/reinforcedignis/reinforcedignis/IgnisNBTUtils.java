package com.reinforcedignis.reinforcedignis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
public class IgnisNBTUtils
{
    private static final String IGNIS_STACKS_KEY="ReinforcedIgnisStacks";
    private static final String IGNIS_EXPIRE_KEY="ReinforcedIgnisExpireTime";
    public static int getIgnisStacks(LivingEntity entity)
    {
        CompoundTag persistentData=entity.getPersistentData();
        if(persistentData.contains(IGNIS_STACKS_KEY))
        {
            return persistentData.getInt(IGNIS_STACKS_KEY);
        }
        return 0;
    }
    public static void setIgnisStacks(LivingEntity entity,int stacks)
    {
        CompoundTag persistentData=entity.getPersistentData();
        persistentData.putInt(IGNIS_STACKS_KEY,Math.min(stacks,10));
    }
    public static int getExpireTime(LivingEntity entity)
    {
        CompoundTag persistentData=entity.getPersistentData();
        if(persistentData.contains(IGNIS_EXPIRE_KEY))
        {
            return persistentData.getInt(IGNIS_EXPIRE_KEY);
        }
        return 0;
    }
    public static void setExpireTime(LivingEntity entity,int expireTime)
    {
        CompoundTag persistentData=entity.getPersistentData();
        persistentData.putInt(IGNIS_EXPIRE_KEY,expireTime);
    }
    public static void clearIgnisStacks(LivingEntity entity)
    {
        CompoundTag persistentData=entity.getPersistentData();
        persistentData.remove(IGNIS_STACKS_KEY);
        persistentData.remove(IGNIS_EXPIRE_KEY);
    }
}
