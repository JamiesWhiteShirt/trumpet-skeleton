package com.jamieswhiteshirt.trumpetskeleton.common.entity;

import com.jamieswhiteshirt.trumpetskeleton.TrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonItems;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonSoundEvents;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTrumpetSkeleton extends AbstractSkeleton {
    public EntityTrumpetSkeleton(World worldIn) {
        super(worldIn);
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TrumpetSkeletonSoundEvents.ENTITY_TRUMPET_SKELETON_AMBIENT;
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TrumpetSkeletonItems.TRUMPET));
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return TrumpetSkeleton.ENTITIES_TRUMPET_SKELETON_LOOT_TABLE;
    }
}
