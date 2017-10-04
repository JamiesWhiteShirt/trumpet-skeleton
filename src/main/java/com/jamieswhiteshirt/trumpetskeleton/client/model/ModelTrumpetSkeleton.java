package com.jamieswhiteshirt.trumpetskeleton.client.model;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.entity.Entity;

public class ModelTrumpetSkeleton extends ModelSkeleton {
    public ModelTrumpetSkeleton()
    {
        this(0.0F, false);
    }

    public ModelTrumpetSkeleton(float v, boolean b) {
        super(v, b);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        bipedRightArm.rotateAngleX += (float) (Math.PI / 16.0D);
        bipedRightArm.rotateAngleY -= (float) (Math.PI / 8.0D);
        bipedRightArm.rotateAngleZ += (float) (Math.PI / 8.0D);
    }
}
