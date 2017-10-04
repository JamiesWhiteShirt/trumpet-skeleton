package com.jamieswhiteshirt.trumpetskeleton.client;

import com.jamieswhiteshirt.trumpetskeleton.client.renderer.entity.RenderTrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.common.CommonProxy;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonItems;
import com.jamieswhiteshirt.trumpetskeleton.common.entity.EntityTrumpetSkeleton;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        MinecraftForge.EVENT_BUS.register(this);
        RenderingRegistry.registerEntityRenderingHandler(EntityTrumpetSkeleton.class, RenderTrumpetSkeleton::new);
    }

    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(TrumpetSkeletonItems.TRUMPET, 0, new ModelResourceLocation(TrumpetSkeletonItems.TRUMPET.getRegistryName(), "inventory"));
    }
}
