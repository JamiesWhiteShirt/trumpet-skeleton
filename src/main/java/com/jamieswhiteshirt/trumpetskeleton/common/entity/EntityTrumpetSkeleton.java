package com.jamieswhiteshirt.trumpetskeleton.common.entity;

import com.jamieswhiteshirt.trumpetskeleton.TrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonItems;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonSoundEvents;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class EntityTrumpetSkeleton extends AbstractSkeleton {
    private static final DataParameter<Boolean> SWINGING_ARMS;

    static {
        Field swingingArmsField = ReflectionHelper.findField(AbstractSkeleton.class, "field_184728_b", "SWINGING_ARMS");
        DataParameter<Boolean> swingingArms = null;
        try {
            swingingArms = (DataParameter<Boolean>) swingingArmsField.get(null);
        } catch (IllegalAccessException | ClassCastException e) {
        	TrumpetSkeleton.logger.error("Could not access SWINGING_ARMS data field", e);
        }
        SWINGING_ARMS = swingingArms;
    }

    public EntityTrumpetSkeleton(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return TrumpetSkeleton.ENTITIES_TRUMPET_SKELETON_LOOT_TABLE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TrumpetSkeletonSoundEvents.ENTITY_TRUMPET_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TrumpetSkeletonItems.TRUMPET));
    }

    @Override
    public void playLivingSound() {
        super.playLivingSound();
        if (SWINGING_ARMS != null) {
            boolean isSwingingArms = this.dataManager.get(SWINGING_ARMS);
            if (isSwingingArms) {
                TrumpetSkeleton.scare(world, this);
            }
        }
    }
}
