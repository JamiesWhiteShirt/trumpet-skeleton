package com.jamieswhiteshirt.trumpetskeleton;

import com.jamieswhiteshirt.trumpetskeleton.common.CommonProxy;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonItems;
import com.jamieswhiteshirt.trumpetskeleton.common.TrumpetSkeletonSoundEvents;
import com.jamieswhiteshirt.trumpetskeleton.common.entity.EntityTrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.common.item.ItemTrumpet;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod(
    modid = TrumpetSkeleton.MODID,
    version = TrumpetSkeleton.VERSION,
    acceptedMinecraftVersions = "[1.12,1.13)",
    dependencies = "required-after:forge@[14.21.1.2387,)",
    name = "Trumpet Skeleton"
)
public class TrumpetSkeleton {
    public static final String MODID = "trumpetskeleton";
    public static final String VERSION = "1.12-1.0.2.1";

    public static final ResourceLocation ENTITIES_TRUMPET_SKELETON_LOOT_TABLE = new ResourceLocation(MODID, "entities/trumpet_skeleton");

    @Mod.Instance
    public static TrumpetSkeleton instance;
    @SidedProxy(
        clientSide = "com.jamieswhiteshirt.trumpetskeleton.client.ClientProxy",
        serverSide = "com.jamieswhiteshirt.trumpetskeleton.server.ServerProxy",
        modId = MODID
    )
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(MODID);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        LootTableList.register(ENTITIES_TRUMPET_SKELETON_LOOT_TABLE);
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        EntityParrot.registerMimicSound(EntityTrumpetSkeleton.class, TrumpetSkeletonSoundEvents.E_PARROT_IM_TRUMPET_SKELETON);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
            new ItemTrumpet().setUnlocalizedName("trumpetskeleton.trumpet").setRegistryName(MODID, "trumpet").setCreativeTab(CreativeTabs.MISC)
        );
    }

    @SubscribeEvent
    public void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
            new SoundEvent(new ResourceLocation(MODID, "entity.trumpet_skeleton.ambient")).setRegistryName(new ResourceLocation(MODID, "entity.trumpet_skeleton.ambient")),
            new SoundEvent(new ResourceLocation(MODID, "item.trumpet.use")).setRegistryName(new ResourceLocation(MODID, "item.trumpet.use")),
            new SoundEvent(new ResourceLocation(MODID, "entity.parrot.imitate.trumpet_skeleton")).setRegistryName(new ResourceLocation(MODID, "entity.parrot.imitate.trumpet_skeleton"))
        );
    }

    @SubscribeEvent
    public void registerEntityEntries(RegistryEvent.Register<EntityEntry> event) {
        EntityRegistry.registerModEntity(
            new ResourceLocation(MODID, "trumpet_skeleton"), EntityTrumpetSkeleton.class, "trumpetskeleton.TrumpetSkeleton", 0,
            this,
            80, 3, false,
            0xC1C1C1, 0xFCFC00
        );
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            for (Biome.SpawnListEntry entry : new ArrayList<>(biome.getSpawnableList(EnumCreatureType.MONSTER))) {
                if (entry.entityClass == EntitySkeleton.class) {
                    EntityRegistry.addSpawn(EntityTrumpetSkeleton.class, entry.itemWeight / 4, entry.minGroupCount, entry.maxGroupCount, EnumCreatureType.MONSTER, biome);
                }
            }
        }
    }

    public static void scare(World world, EntityLivingBase user) {
        if (!world.isRemote) {
            List<EntityLivingBase> spookedEntities = world.getEntitiesWithinAABB(EntityLivingBase.class, user.getEntityBoundingBox().grow(10.0D));
            for (EntityLivingBase spookedEntity : spookedEntities) {
                if (spookedEntity == user) continue;
                double deltaX = spookedEntity.posX - user.posX + world.rand.nextDouble() - world.rand.nextDouble();
                double deltaZ = spookedEntity.posZ - user.posZ + world.rand.nextDouble() - world.rand.nextDouble();
                double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
                spookedEntity.velocityChanged = true;
                spookedEntity.addVelocity(0.5 * deltaX / distance, 5.0D / (10.0D + distance), 0.5 * deltaZ / distance);
                spookedEntity.setRevengeTarget(user);
            }
        }
    }

    @SubscribeEvent
    public void onActiveItemUseTick(LivingEntityUseItemEvent.Tick event) {
        ItemStack stack = event.getItem();
        if (stack.getItem() == TrumpetSkeletonItems.TRUMPET) {
            if (event.getDuration() == stack.getMaxItemUseDuration() - 10) {
                EntityLivingBase user = event.getEntityLiving();
                World world = user.world;
                user.playSound(TrumpetSkeletonSoundEvents.ITEM_TRUMPET_USE, 1.0F, 0.9F + world.rand.nextFloat() * 0.2F);
                scare(world, user);
                stack.damageItem(1, user);
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
