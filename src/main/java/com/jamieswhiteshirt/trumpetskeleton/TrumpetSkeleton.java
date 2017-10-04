package com.jamieswhiteshirt.trumpetskeleton;

import com.jamieswhiteshirt.trumpetskeleton.common.CommonProxy;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonItems;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonSoundEvents;
import com.jamieswhiteshirt.trumpetskeleton.common.entity.EntityTrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.common.item.ItemTrumpet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.*;

import java.util.List;

@Mod(
        modid = TrumpetSkeleton.MODID,
        version = TrumpetSkeleton.VERSION,
        acceptedMinecraftVersions = "[1.12,)",
        name = "Trumpet Skeleton"
)
public class TrumpetSkeleton {
    public static final String MODID = "trumpetskeleton";
    public static final String VERSION = "1.12-1.0.0.0";

    public static final ResourceLocation ENTITIES_TRUMPET_SKELETON_LOOT_TABLE = new ResourceLocation(MODID, "entities/trumpet_skeleton");

    @Mod.Instance
    public static TrumpetSkeleton instance;
    @SidedProxy(
            clientSide = "com.jamieswhiteshirt.trumpetskeleton.client.ClientProxy",
            serverSide = "com.jamieswhiteshirt.trumpetskeleton.server.ServerProxy",
            modId = MODID
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        LootTableList.register(ENTITIES_TRUMPET_SKELETON_LOOT_TABLE);
        proxy.preInit(event);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new ItemTrumpet().setUnlocalizedName("trumpetskeleton.trumpet").setRegistryName(MODID, "trumpet")
        );
    }

    @SubscribeEvent
    public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                new SoundEvent(new ResourceLocation(MODID, "entity.trumpet_skeleton.ambient")).setRegistryName(new ResourceLocation(MODID, "entity.trumpet_skeleton.ambient")),
                new SoundEvent(new ResourceLocation(MODID, "item.trumpet.use")).setRegistryName(new ResourceLocation(MODID, "item.trumpet.use"))
        );
    }

    @SubscribeEvent
    public void registerEntityEntries(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                EntityEntryBuilder.create()
                        .entity(EntityTrumpetSkeleton.class)
                        .id(new ResourceLocation(MODID, "trumpet_skeleton"), 0)
                        .name("trumpetskeleton.TrumpetSkeleton")
                        .tracker(80, 3, false)
                        .egg(0xC1C1C1, 0xFCFC00)
                        .spawn(EnumCreatureType.MONSTER, 25, 4, 4, ForgeRegistries.BIOMES.getValues())
                        .build()
        );
    }

    @SubscribeEvent
    public void onActiveItemUseTick(LivingEntityUseItemEvent.Tick event) {
        ItemStack stack = event.getItem();
        if (stack.getItem() == TrumpetSkeletonItems.TRUMPET) {
            if (event.getDuration() == stack.getMaxItemUseDuration() - 10) {
                EntityLivingBase user = event.getEntityLiving();
                World world = user.world;

                user.playSound(TrumpetSkeletonSoundEvents.ITEM_TRUMPET_USE, 1.0F, 0.9F + world.rand.nextFloat() * 0.2F);
                if (!world.isRemote) {
                    List<EntityLivingBase> spookedEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, user.getEntityBoundingBox().grow(10.0D));
                    for (EntityLivingBase spookedEntity : spookedEntities) {
                        if (spookedEntity == user) continue;
                        double deltaX = user.posX - spookedEntity.posX;
                        double deltaZ = user.posZ - spookedEntity.posZ;
                        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                        if (distance > 0.25D) {
                            spookedEntity.knockBack(user, 0.5F, deltaX / distance, deltaZ / distance);
                        }
                        spookedEntity.setRevengeTarget(user);
                    }
                    stack.damageItem(1, user);
                }
            } else if (event.getDuration() <= stack.getMaxItemUseDuration() - 15) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlaySoundAtEntity(PlaySoundAtEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.getActiveItemStack().getItem() == TrumpetSkeletonItems.TRUMPET) {
                if (event.getSound() == SoundEvents.ENTITY_GENERIC_EAT) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
