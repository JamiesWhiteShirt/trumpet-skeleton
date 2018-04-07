package com.jamieswhiteshirt.trumpetskeleton.common;

import com.jamieswhiteshirt.trumpetskeleton.TrumpetSkeleton;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(TrumpetSkeleton.MODID)
public class TrumpetSkeletonSoundEvents {
    @GameRegistry.ObjectHolder("entity.trumpet_skeleton.ambient")
    public static final SoundEvent ENTITY_TRUMPET_SKELETON_AMBIENT = null;

    @GameRegistry.ObjectHolder("entity.parrot.imitate.trumpet_skeleton")
    public static final SoundEvent E_PARROT_IM_TRUMPET_SKELETON = null;

    @GameRegistry.ObjectHolder("item.trumpet.use")
    public static final SoundEvent ITEM_TRUMPET_USE = null;
}
