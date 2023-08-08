/**
 * Copyright (C) 2017 Zrips
 */

package net.Zrips.CMILib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.authlib.properties.Property;

import net.Zrips.CMILib.Attributes.Attribute;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIServerProperties;
import net.Zrips.CMILib.Effects.CMIEffect;
import net.Zrips.CMILib.Effects.CMIEffectManager.CMIParticleDataType;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;
import net.minecraft.advancements.Advancements;

public class Reflections {

    private Class<?> CraftServerClass;
    private Class<?> CraftWorldClass;
    private Class<?> WorldServerClass;
    private Object CraftServer;
    private Class<?> CraftStatistic;
    private Class<?> Statistics;
//    private Class<?> TileEntitySign;
    private Class<?> MinecraftServerClass;
    private Class<?> PropertyManagerClass;
    private Object MinecraftServer;
    private Class<?> ServerStatisticManager;
//    private Class<?> MojangsonParser;
//    private Class<?> PlayerList;

//    private Class<?> EntityHuman;
//    private Class<?> EntityPlayer;
//    private Class<?> EnumGameMode;
    private Class<?> CraftPlayer;
    private Class<?> CraftEntity;
    private Class<?> CEntity;
    private Class<?> nbtTagCompound;
//    private Class<?> nbtTagList;
    private Class<?> NBTBase;
    private Class<?> EntityLiving;
    private Class<?> BlockPosition;
//    private Class<?> EnumHand;
    private Class<?> CraftBeehive;
    private Class<?> CraftNamespacedKey;
//    private Class<?> TileEntityBeehive;

//    private Class<?> IChatBaseComponent$ChatSerializer;
//    private Class<?> CraftMetaBook;
//    private Class<?> CraftItemStack;
    private Class<?> Item;
    private Class<?> IStack;
    private Class<?> nmsChatSerializer;
//    private Class<?> IChatBaseComponent;
    private Class<?> dimensionManager;
//    private Class<?> PacketPlayOutAnimation;

    private Class<?> CraftContainer;
    private Class<?> CraftContainers;
    private Class<?> PacketPlayOutOpenWindow;
//    private Class<?> PacketPlayOutWindowItems;
    private Class<?> PacketPlayOutEntityTeleport;
//    private Class<?> PacketPlayOutGameStateChange;
    private Class<?> PacketPlayOutSpawnEntityLiving;
    private Class<?> PacketPlayOutEntityMetadata;
    private Class<?> PacketPlayOutSetSlot;
    private Class<?> DataWatcher;
//    private Field playerConnection;
    private Method sendPacket;
    private Class<?> PacketPlayOutWorldParticles;
    private Class<?> EnumParticle;
    private Class<?> ParticleParam;
    private Class<?> CraftParticle;
    private Class<?> AdvancementDataWorld;
    private Class<?> ChatDeserializer;
    private Class<?> SerializedAdvancement;
    private Class<?> LootDeserializationContext;

    private Class<?> world;

    private Object advancementRegistry;

    private CMILib plugin;

    public Reflections(CMILib plugin) {
        this.plugin = plugin;
        initialize();
    }

    private void initialize() {

        if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
            try {
                world = net.minecraft.world.level.World.class;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {

            try {
                sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "sendPacket",
                    net.minecraft.network.protocol.Packet.class);
                CraftServerClass = getBukkitClass("CraftServer");
                CraftServer = CraftServerClass.cast(Bukkit.getServer());
                CraftWorldClass = getBukkitClass("CraftWorld");
                MinecraftServerClass = net.minecraft.server.MinecraftServer.class;

                CraftBeehive = getBukkitClass("block.impl.CraftBeehive");
                CraftNamespacedKey = getBukkitClass("util.CraftNamespacedKey");
                PacketPlayOutEntityTeleport = net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport.class;

                if (Version.isCurrentEqualOrLower(Version.v1_18_R2)) {
                    PacketPlayOutSpawnEntityLiving = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving");
                } else {
                    PacketPlayOutSpawnEntityLiving = net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity.class;
                }

                PacketPlayOutEntityMetadata = net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata.class;
                PacketPlayOutSetSlot = net.minecraft.network.protocol.game.PacketPlayOutSetSlot.class;
                PacketPlayOutOpenWindow = net.minecraft.network.protocol.game.PacketPlayOutOpenWindow.class;
                PacketPlayOutWorldParticles = net.minecraft.network.protocol.game.PacketPlayOutWorldParticles.class;
                CraftContainer = net.minecraft.world.inventory.Container.class;
                CraftContainers = net.minecraft.world.inventory.Containers.class;
                DataWatcher = net.minecraft.network.syncher.DataWatcher.class;

                AdvancementDataWorld = net.minecraft.server.AdvancementDataWorld.class;
                ChatDeserializer = net.minecraft.util.ChatDeserializer.class;
                SerializedAdvancement = net.minecraft.advancements.Advancement.SerializedAdvancement.class;
                LootDeserializationContext = net.minecraft.advancements.critereon.LootDeserializationContext.class;

                CraftParticle = getBukkitClass("CraftParticle");
                ParticleParam = net.minecraft.core.particles.ParticleParam.class;

                WorldServerClass = net.minecraft.server.level.WorldServer.class;
                dimensionManager = net.minecraft.world.level.dimension.DimensionManager.class;
                BlockPosition = net.minecraft.core.BlockPosition.class;

                nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");

                MinecraftServer = CraftServer.getClass().getMethod("getServer").invoke(CraftServer);

                CraftStatistic = getBukkitClass("CraftStatistic");
                CraftPlayer = getBukkitClass("entity.CraftPlayer");
                CraftEntity = getBukkitClass("entity.CraftEntity");
                Statistics = net.minecraft.stats.Statistic.class;
                ServerStatisticManager = net.minecraft.stats.ServerStatisticManager.class;
                PropertyManagerClass = net.minecraft.server.dedicated.PropertyManager.class;

                CEntity = net.minecraft.world.entity.Entity.class;

                nbtTagCompound = net.minecraft.nbt.NBTTagCompound.class;
                NBTBase = net.minecraft.nbt.NBTBase.class;

                EntityLiving = net.minecraft.world.entity.EntityLiving.class;
                Item = net.minecraft.world.item.Item.class;
                IStack = net.minecraft.world.item.ItemStack.class;

                String variable = "ax";
                if (Version.isCurrentEqualOrHigher(Version.v1_19_R3))
                    variable = "az";
                else if (Version.isCurrentEqualOrHigher(Version.v1_19_R2))
                    variable = "ay";
                else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1))
                    variable = "az";
                else if (Version.isCurrentLower(Version.v1_18_R1))
                    variable = "getAdvancementData";

                Object advancementData = MinecraftServer.getClass().getMethod(variable).invoke(MinecraftServer);
                advancementRegistry = advancementData.getClass().getField("c").get(advancementData);

            } catch (Throwable e) {
                e.printStackTrace();
            }

        } else {
            try {
                CraftServerClass = getBukkitClass("CraftServer");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CraftWorldClass = getBukkitClass("CraftWorld");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CraftBeehive = getBukkitClass("block.impl.CraftBeehive");
            } catch (Throwable e) {
            }
            try {
                PacketPlayOutWorldParticles = getMinecraftClass("PacketPlayOutWorldParticles");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CraftNamespacedKey = getBukkitClass("util.CraftNamespacedKey");
            } catch (Throwable e) {
            }

            try {
                CraftContainer = getMinecraftClass("Container");
            } catch (Throwable e) {
            }
            try {
                AdvancementDataWorld = getMinecraftClass("AdvancementDataWorld");
            } catch (Throwable e) {
            }
            try {
                ChatDeserializer = getMinecraftClass("ChatDeserializer");
            } catch (Throwable e) {
            }
            try {
                SerializedAdvancement = getMinecraftClass("Advancement$SerializedAdvancement");
            } catch (Throwable e) {
//		e.printStackTrace();
            }
            try {
                LootDeserializationContext = getMinecraftClass("LootDeserializationContext");
            } catch (Throwable e) {
            }
            try {
                CraftContainers = getMinecraftClass("Containers");
            } catch (Throwable e) {
            }
            try {
                if (Version.isCurrentEqualOrLower(Version.v1_12_R1))
                    EnumParticle = getMinecraftClass("EnumParticle");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (Version.isCurrentHigher(Version.v1_12_R1)) {
                try {
                    CraftParticle = getBukkitClass("CraftParticle");
                } catch (Throwable e) {
                }
                try {
                    ParticleParam = getMinecraftClass("ParticleParam");
                } catch (Throwable e) {
                }
            }
            try {
                PacketPlayOutEntityTeleport = getMinecraftClass("PacketPlayOutEntityTeleport");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                PacketPlayOutSpawnEntityLiving = getMinecraftClass("PacketPlayOutSpawnEntityLiving");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                DataWatcher = getMinecraftClass("DataWatcher");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                PacketPlayOutEntityMetadata = getMinecraftClass("PacketPlayOutEntityMetadata");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                PacketPlayOutSetSlot = getMinecraftClass("PacketPlayOutSetSlot");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                PacketPlayOutOpenWindow = getMinecraftClass("PacketPlayOutOpenWindow");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                WorldServerClass = getMinecraftClass("WorldServer");
            } catch (Throwable e) {
            }
            try {
                dimensionManager = getMinecraftClass("DimensionManager");
            } catch (Throwable e) {
            }
            try {
                BlockPosition = getMinecraftClass("BlockPosition");
            } catch (Throwable e) {
            }
            try {
                CraftServer = CraftServerClass.cast(Bukkit.getServer());
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                MinecraftServerClass = getMinecraftClass("MinecraftServer");
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                if (!Version.isCurrentHigher(Version.v1_8_R2))
                    nmsChatSerializer = getMinecraftClass("ChatSerializer");
                else  // 1_8_R2 moved to IChatBaseComponent
                    nmsChatSerializer = getMinecraftClass("IChatBaseComponent$ChatSerializer");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                MinecraftServer = CraftServer.getClass().getMethod("getServer").invoke(CraftServer);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CraftStatistic = getBukkitClass("CraftStatistic");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                Statistics = getMinecraftClass("Statistic");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                ServerStatisticManager = getMinecraftClass("ServerStatisticManager");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {

                PropertyManagerClass = getMinecraftClass("PropertyManager");
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                CraftPlayer = getBukkitClass("entity.CraftPlayer");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CraftEntity = getBukkitClass("entity.CraftEntity");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                CEntity = getMinecraftClass("Entity");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                nbtTagCompound = getMinecraftClass("NBTTagCompound");
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                NBTBase = getMinecraftClass("NBTBase");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                EntityLiving = getMinecraftClass("EntityLiving");
            } catch (Throwable e) {
                e.printStackTrace();
            }

            try {
                Item = getMinecraftClass("Item");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                IStack = getMinecraftClass("ItemStack");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }

    public String serializeText(String text) {
        try {
            Object serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, CMIChatColor.translate(text));
            return (String) serialized.getClass().getMethod("e").invoke(serialized);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return text;
    }

    public Object textToIChatBaseComponent(String text) {
        try {
            Object serialized = nmsChatSerializer.getMethod("a", String.class).invoke(null, CMIChatColor.translate(text));
            return serialized;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return text;
    }

    public int getCurrentTick() {
        try {
            return MinecraftServer.getClass().getField("currentTick").getInt(CraftServer);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return (int) (System.currentTimeMillis() / 50);
    }

//    public void setDura() {
//	Class<?> c;
//	try {
//	    c = getMinecraftClass("Block");
//	    Method method = c.getDeclaredMethod("b", float.class);
//	    method.setAccessible(true);
//	    Block b = Block.REGISTRY.getId(1);
//	    method.invoke(b, 30.0F);
//	} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//	    e.printStackTrace();
//	}
//    }

    public Object getItemInfo(int id, String fieldName) {
        try {

//	    BlockStateList list = net.minecraft.server.v1_12_R1.Block.getById(id).s();
//	    for (IBlockData one : list.a()) {
//		for (IBlockState<?> ones : one.s()) {
//
//		    for (int i = 0; i < ones.c().size(); i++) {
//
//		    }
//		}
//
//	    }
            Field field = getMinecraftClass("Block").getDeclaredField(fieldName);
            field.setAccessible(true);

            if (Version.isCurrentEqualOrHigher(Version.v1_13_R2)) {
                Method method = getMinecraftClass("Block").getDeclaredMethod("getByCombinedId", int.class);
                Object res = method.invoke(getMinecraftClass("Block"), id);
                res = res.getClass().getMethod("getBlock").invoke(res);
                return field.get(res);
            }

            Method method = getMinecraftClass("Block").getDeclaredMethod("getById", int.class);
            Object res = method.invoke(getMinecraftClass("Block"), id);

            return field.get(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> getBukkitClass(String nmsClassString) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + Version.getCurrent().toString() + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
//	    e.printStackTrace();
        }
        return null;
    }

    public Class<?> getMinecraftClass(String nmsClassString) {
        try {
            return Class.forName("net.minecraft.server." + Version.getCurrent().toString() + "." + nmsClassString);
        } catch (ClassNotFoundException e) {
//	    e.printStackTrace();
        }
        return null;
    }

    public void setServerProperties(CMIServerProperties setting, Object value, boolean save) {
        if (Version.isCurrentEqualOrHigher(Version.v1_19_R2)) {

            try {
                // DedicatedServer -> DedicatedServerSettings
                Object field1 = MinecraftServer.getClass().getField("u").get(MinecraftServer);
                // DedicatedServerSettings -> DedicatedServerProperties method
                Object prop = field1.getClass().getMethod("a").invoke(field1);
                // PropertyManager -> Properties
                Object field2 = prop.getClass().getField("Y").get(prop);
                Method setPropertyMethod = field2.getClass().getDeclaredMethod("setProperty", String.class, String.class);
                setPropertyMethod.invoke(field2, setting.getPath(), String.valueOf(value));
                if (save)
                    // DedicatedServerSettings -> forceSave method
                    field1.getClass().getMethod("b").invoke(field1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {

            try {
                // DedicatedServer -> DedicatedServerSettings
                Object field1 = MinecraftServer.getClass().getField("u").get(MinecraftServer);
                // DedicatedServerSettings -> DedicatedServerProperties method
                Object prop = field1.getClass().getMethod("a").invoke(field1);
                // PropertyManager -> Properties
                Object field2 = prop.getClass().getField("X").get(prop);
                Method setPropertyMethod = field2.getClass().getDeclaredMethod("setProperty", String.class, String.class);
                setPropertyMethod.invoke(field2, setting.getPath(), String.valueOf(value));
                if (save)
                    // DedicatedServerSettings -> void method
                    field1.getClass().getMethod("b").invoke(field1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        if (Version.isCurrentEqualOrHigher(Version.v1_18_R2)) {
            try {
                // DedicatedServer -> DedicatedServerSettings
                Object field1 = MinecraftServer.getClass().getField("y").get(MinecraftServer);
                // DedicatedServerSettings -> DedicatedServerProperties method
                Object prop = field1.getClass().getMethod("a").invoke(field1);
                // PropertyManager -> Properties
                Object field2 = prop.getClass().getField("Y").get(prop);
                Method setPropertyMethod = field2.getClass().getDeclaredMethod("setProperty", String.class, String.class);
                setPropertyMethod.invoke(field2, setting.getPath(), String.valueOf(value));
                if (save)
                    // DedicatedServerSettings -> void method
                    field1.getClass().getMethod("b").invoke(field1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
            try {
                Object field1 = MinecraftServer.getClass().getField("z").get(MinecraftServer);
                Object prop = field1.getClass().getMethod("a").invoke(field1);
                Object field2 = prop.getClass().getField("Y").get(prop);
                Method setPropertyMethod = field2.getClass().getDeclaredMethod("setProperty", String.class, String.class);
                setPropertyMethod.invoke(field2, setting.getPath(), String.valueOf(value));
                if (save)
                    field1.getClass().getMethod("b").invoke(field1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                Object field1 = MinecraftServer.getClass().getField("x").get(MinecraftServer);
                Object prop = field1.getClass().getMethod("getProperties").invoke(field1);
                Object field2 = prop.getClass().getField("X").get(prop);
                Method setPropertyMethod = field2.getClass().getDeclaredMethod("setProperty", String.class, String.class);
                setPropertyMethod.invoke(field2, setting.getPath(), String.valueOf(value));
                if (save)
                    field1.getClass().getMethod("save").invoke(field1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
            try {
                Object field1 = MinecraftServer.getClass().getField("propertyManager").get(MinecraftServer);
                Object prop = field1.getClass().getMethod("getProperties").invoke(field1);
                Object field2 = prop.getClass().getField("properties").get(prop);
                Method setPropertyMethod = field2.getClass().getDeclaredMethod("setProperty", String.class, String.class);
                setPropertyMethod.invoke(field2, setting.getPath(), String.valueOf(value));
                if (save)
                    field1.getClass().getMethod("save").invoke(field1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            Method getServerMethod = MinecraftServerClass.getDeclaredMethod("getServer");
            Method getPropertyManagerMethod = MinecraftServerClass.getDeclaredMethod("getPropertyManager");
            Method setPropertyMethod = PropertyManagerClass.getDeclaredMethod("setProperty", String.class, Object.class);
            Method savePropertiesFileMethod = PropertyManagerClass.getDeclaredMethod("savePropertiesFile");
            Object manager = getPropertyManagerMethod.invoke(getServerMethod.invoke(null));
            setPropertyMethod.invoke(manager, setting.getPath(), value);
            if (save)
                savePropertiesFileMethod.invoke(manager);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public boolean isNbtSimilar(ItemStack is, ItemStack is2) {
        return CMINBT.isNBTSimilar(is, is2);
    }

    public com.mojang.authlib.GameProfile getProfile(Player player) {
        Object craftPlayer = this.CraftPlayer.cast(player);
        Method method;
        try {
            method = this.CraftPlayer.getMethod("getProfile");
            Object profile = method.invoke(craftPlayer);
            return (com.mojang.authlib.GameProfile) profile;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getCraftPlayer(Player player) {
        try {
            return this.CraftPlayer.cast(player);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

//    public boolean forceTeleport(Player player, Location loc, TeleportCause cause) {
//	if (player == null || player.isDead())
//	    return false;
//
//	if (player.isInsideVehicle())
//	    player.leaveVehicle();
//
//	if (loc == null)
//	    return false;
//
//	Location from = player.getLocation();
//	Location to = loc;
//
//	Object wsTo = WorldServerClass.cast(this.getCraftWorld(to.getWorld()));
//
//	    player.closeInventory();
//		
//	if (from.getWorld().equals(to.getWorld())) {
//	    Object res = this.getPlayerConnection(player).getClass().getMethod("teleport", Location.class).invoke(this.getPlayerConnection(player), to);
//	} else {
////	    MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
//	    
//	    Object entityplayer = this.CraftPlayer.getMethod("getHandle").invoke(player);
//	    
//	    wsTo.getClass().getMethod("moveToWorld", entityplayer.getClass()).moveToWorld(entity, toWorld.dimension, true, to, true);
//	}
////	Object craftEntity = CraftEntity.cast(ent);
////	if (cause == null)
////	    cause = TeleportCause.PLUGIN;
////	try {
////	    Method meth = craftEntity.getClass().getMethod("teleport", Location.class, TeleportCause.class);
////	    Object res = meth.invoke(craftEntity, loc, cause);
////	    return (boolean) res;
////	} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
////	    e.printStackTrace();
////	}
//	return false;
//    }

    public Object getPlayerHandle(Player player) {
        Object handle = null;
        try {
            handle = player.getClass().getMethod("getHandle").invoke(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handle;
    }

    public Object getEntityHandle(Entity ent) {
        Object handle = null;
        try {
            handle = CraftEntity.cast(ent).getClass().getMethod("getHandle").invoke(CraftEntity.cast(ent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return handle;
    }

    public Object getPlayerConnection(Player player) {
        Object connection = null;
        try {
            Object handle = getPlayerHandle(player);
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1))
                connection = handle.getClass().getField("c").get(handle);
            else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
                connection = handle.getClass().getField("b").get(handle);
            else
                connection = handle.getClass().getField("playerConnection").get(handle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Object getDimensionManager(World world) {

        Object dm = null;
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
                switch (world.getEnvironment()) {
                case NETHER:
                    return dimensionManager.getField("NETHER").get(dimensionManager);
                case NORMAL:
                    return dimensionManager.getField("OVERWORLD").get(dimensionManager);
                case THE_END:
                    return dimensionManager.getField("NETHER").get(dimensionManager);
                default:
                    break;
                }
            }

            Object worldServer = this.getCraftWorld(world).getClass().getMethod("getHandle").invoke(this.getCraftWorld(world));
            dm = worldServer.getClass().getField(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "G" : "dimension").get(worldServer);
        } catch (Throwable e) {
//	    e.printStackTrace();
        }

        return dm;
    }

    public String getServerName() {

        if (Version.isCurrentEqualOrLower(Version.v1_13_R2)) {
            Server srv = Bukkit.getServer();
            Object res;
            try {
                res = srv.getClass().getMethod("getServerName").invoke(srv);
                return (String) res;
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return Bukkit.getName();
        }

        try {
            Object worldServer = this.getCraftWorld(Bukkit.getWorlds().get(0));
            return (String) worldServer.getClass().getMethod("getName").invoke(worldServer);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return Bukkit.getName();
    }

    public Object getCraftWorld(World world) {
        return this.CraftWorldClass.cast(world);
    }

    public Object getWorldServer(World world) {
        Object obj = getCraftWorld(world);
        try {
            return WorldServerClass.cast(obj.getClass().getMethod("getHandle").invoke(obj));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getMineCraftWorld(World world) {
        Object obj = getCraftWorld(world);
        try {
            Object handle = WorldServerClass.cast(obj.getClass().getMethod("getHandle").invoke(obj));
            return this.world.cast(handle.getClass().getMethod("getMinecraftWorld").invoke(handle));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getBlockPosition(Location loc) {
        try {
            Constructor<?> constructor = BlockPosition.getConstructor(int.class, int.class, int.class);
            return constructor.newInstance(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getTileEntityAt(Location loc) {
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_13_R2)) {
                try {
                    Object ncw = getWorldServer(loc.getWorld());
                    Method meth = ncw.getClass().getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "c_" : "getTileEntity", BlockPosition);
                    Object res = meth.invoke(ncw, getBlockPosition(loc));
                    return res;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            Object cw = getCraftWorld(loc.getWorld());
            Method meth = cw.getClass().getMethod("getTileEntityAt", int.class, int.class, int.class);
            Object res = meth.invoke(cw, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            return res;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendPlayerPacket(Player player, Object packet) throws Exception {
        Object connection = getPlayerConnection(player);
        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            sendPacket.invoke(connection, packet);
        } else
            connection.getClass().getMethod("sendPacket", getClass("{nms}.Packet")).invoke(connection, packet);
    }

    @Deprecated
    public String toJson(ItemStack item) {
        return CMINBT.toJson(item);
    }

    public String getItemMinecraftName(ItemStack item) {
        try {
            Object nmsStack = asNMSCopy(item);

            if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {

                Object pre = nmsStack.getClass().getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "c" : "getItem").invoke(nmsStack);
                Object n = pre.getClass().getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "getName").invoke(pre);

                Class<?> ll = null;
                if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
                    ll = net.minecraft.locale.LocaleLanguage.class;
                } else {
                    ll = Class.forName("net.minecraft.server." + Version.getCurrent() + ".LocaleLanguage");
                }

                Object lla = ll.getMethod("a").invoke(ll);

                if (Version.isCurrentEqualOrHigher(Version.v1_16_R1)) {
                    Method method = lla.getClass().getMethod("a", String.class);
                    method.setAccessible(true);
                    return (String) method.invoke(lla, (String) n);
                }
                return (String) lla.getClass().getMethod("a", String.class).invoke(lla, (String) n);
            }

            Field field = Item.getField("REGISTRY");
            Object reg = field.get(field);
            Method meth = reg.getClass().getMethod("b", Object.class);
            meth.setAccessible(true);
            Method secmeth = nmsStack.getClass().getMethod("getItem");
            Object res2 = secmeth.invoke(nmsStack);
            Object res = meth.invoke(reg, res2);
            return res.toString();
        } catch (Throwable e) {
//	    e.printStackTrace();
            return null;
        }
    }

    public void upadteItemWithPacket(Player player, ItemStack item, int slot) {
        if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
            try {
                Constructor<?> packet = PacketPlayOutSetSlot.getConstructor(int.class, int.class, int.class, IStack);
                Object newPack = packet.newInstance(this.getActiveContainerId(player), getActiveContainerStateId(player), slot, CMINBT.asNMSCopy(item));
                this.sendPlayerPacket(player, newPack);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            Constructor<?> packet = PacketPlayOutSetSlot.getConstructor(int.class, int.class, IStack);
            Object newPack = packet.newInstance(this.getActiveContainerId(player), slot, CMINBT.asNMSCopy(item));
            this.sendPlayerPacket(player, newPack);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

//    @SuppressWarnings("deprecation")
//    public String getItemMinecraftNamePath(ItemStack item) {
//	try {
//	    Object nmsStack = asNMSCopy(item);
//	    Method itemMeth = Item.getMethod("getById", int.class);
//	    Object res = itemMeth.invoke(Item, item.getType().getId());
//	    Method nameThingy = Item.getMethod("j", IStack);
//	    Object resThingy = nameThingy.invoke(res, nmsStack);
//	    return resThingy.toString();
//	} catch (Exception e) {
//	    return null;
//	}
//    }

    @Deprecated
    public ItemStack setTag(ItemStack item, Object tag) {
        return CMINBT.setTag(item, tag);
    }

    @Deprecated
    public Object getNbt(Entity entity) {
        return CMINBT.getNbt(entity);
    }

    @Deprecated
    public Object getNbt(ItemStack item) {
        return CMINBT.getNbt(item);
    }

    public ItemStack setSkullTexture(ItemStack item, String customProfileName, String texture) {

        if (item == null)
            return null;
        try {

            com.mojang.authlib.GameProfile prof = new com.mojang.authlib.GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);
            prof.getProperties().removeAll("textures");
            prof.getProperties().put("textures", new Property("textures", texture));

//	    ItemMeta headMeta = item.getItemMeta();
            SkullMeta headMeta = (SkullMeta) item.getItemMeta();

            Field profileField = null;
            try {
                profileField = headMeta.getClass().getDeclaredField("profile");
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            if (profileField != null) {
                profileField.setAccessible(true);
                try {
                    profileField.set(headMeta, prof);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                item.setItemMeta(headMeta);
            }

            try {
                byte[] decodedBytes = java.util.Base64.getMimeDecoder().decode(texture);
                String decodedString = new String(decodedBytes);

                if (decodedString.contains("profileName\" : \"")) {
                    String name = decodedString.split("profileName\" : \"", 2)[1].split("\"", 2)[0];
                    item = (ItemStack) new CMINBT(item).setString("SkullOwner.Name", name);
                }
            } catch (Throwable e) {

            }

            Object i = new CMINBT(item).setString("Id", UUID.nameUUIDFromBytes(texture.getBytes()).toString());

            return i == null ? null : (ItemStack) i;
        } catch (Throwable e) {
//            e.printStackTrace();
            return item;
        }
    }

    @Deprecated
    public Object getNbt(Block block) {
        return CMINBT.getNbt(block);
    }

    public void updateTileEntity(Location loadValue, Object tag) {
        if (tag == null)
            return;
        try {

            Object ent = this.getTileEntityAt(loadValue);
            String ff = "load";
            switch (Version.getCurrent()) {
            case v1_10_R1:
            case v1_9_R1:
            case v1_9_R2:
            case v1_8_R1:
            case v1_8_R3:
            case v1_8_R2:
            case v1_11_R1:
                ff = "a";
                break;
            case v1_12_R1:
                ff = "load";
                break;
            case v1_13_R2:
            case v1_13_R1:
                ff = "create";
                break;
            case v1_7_R1:
            case v1_7_R2:
            case v1_7_R3:
            case v1_7_R4:
                ff = "a";
                break;
            default:
                break;
            }

            Method meth = ent.getClass().getMethod(ff, this.nbtTagCompound);
            meth.invoke(ent, tag);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Deprecated
    public Object asNMSCopy(ItemStack item) {
        return CMINBT.asNMSCopy(item);
    }

    @Deprecated
    public Object asBukkitCopy(Object item) {
        return CMINBT.asBukkitCopy(item);
    }

    public Object getCraftServer() {
        return CraftServer;
    }

    public ItemStack getItemInMainHand(Player player) {
        if (player == null)
            return null;
        if (Version.isCurrentHigher(Version.v1_8_R3))
            return player.getInventory().getItemInMainHand();
        return player.getItemInHand();
    }

    public void setItemInMainHand(Player player, ItemStack item) {
        if (player == null)
            return;
        if (Version.isCurrentHigher(Version.v1_8_R3))
            player.getInventory().setItemInMainHand(item);
        else
            player.setItemInHand(item);
    }

    public void setItemInOffHand(Player player, ItemStack item) {
        if (player == null)
            return;
        if (Version.isCurrentHigher(Version.v1_8_R3))
            player.getInventory().setItemInOffHand(item);
        else
            return;
    }

    public ItemStack getItemInOffHand(Player player) {
        if (player == null)
            return null;
        if (Version.isCurrentLower(Version.v1_9_R1))
            return null;
        return player.getInventory().getItemInOffHand();
    }

    public Class<?> getClass(String classname) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            String path = classname.replace("{nms}", "net.minecraft.server." + version)
                .replace("{nm}", "net.minecraft." + version)
                .replace("{cb}", "org.bukkit.craftbukkit.." + version);
            return Class.forName(path);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public Object getNmsPlayer(Player p) throws Exception {
        Method getHandle = p.getClass().getMethod("getHandle");
        return getHandle.invoke(p);
    }

    public Object getNmsScoreboard(Scoreboard s) throws Exception {
        Method getHandle = s.getClass().getMethod("getHandle");
        return getHandle.invoke(s);
    }

    public Object getFieldValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T getFieldValue(Field field, Object obj) {
        try {
            return (T) field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Field getField(Class<?> clazz, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public void setValue(Object instance, String field, Object value) {
        try {
            Field f = instance.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(instance, value);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void sendAllPacket(Object packet) throws Exception {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Object nmsPlayer = getNmsPlayer(p);
            Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                sendPacket.invoke(connection, packet);
            } else
                connection.getClass().getMethod("sendPacket", getClass("{nms}.Packet")).invoke(connection, packet);
        }
    }

    public void sendListPacket(List<String> players, Object packet) {
        try {
            for (String name : players) {
                Object nmsPlayer = getNmsPlayer(Bukkit.getPlayer(name));
                Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
                if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                    sendPacket.invoke(connection, packet);
                } else
                    connection.getClass().getMethod("sendPacket", getClass("{nms}.Packet")).invoke(connection, packet);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<Class<?>, Class<?>>();

    public Class<?> getPrimitiveType(Class<?> Class) {
        return CORRESPONDING_TYPES.containsKey(Class) ? CORRESPONDING_TYPES.get(Class) : Class;
    }

    public Method getMethod(String MethodName, Class<?> Class, Class<?>... Parameters) {
        Class<?>[] T = toPrimitiveTypeArray(Parameters);
        for (Method M : Class.getMethods())
            if (M.getName().equals(MethodName) && equalsTypeArray(toPrimitiveTypeArray(M.getParameterTypes()), T))
                return M;
        return null;
    }

    public boolean equalsTypeArray(Class<?>[] Value1, Class<?>[] Value2) {
        if (Value1.length != Value2.length)
            return false;
        for (int i = 0; i < Value1.length; i++)
            if (!Value1[i].equals(Value2[i]) && !Value1[i].isAssignableFrom(Value2[i]))
                return false;
        return true;
    }

    public Class<?>[] toPrimitiveTypeArray(Class<?>[] Classes) {
        int L = Classes != null ? Classes.length : 0;
        Class<?>[] T = new Class<?>[L];
        for (int i = 0; i < L; i++) {
            if (Classes != null)
                T[i] = getPrimitiveType(Classes[i]);
        }
        return T;
    }

    public Object invokeMethod(String MethodName, Object Parameter) {
        try {
            return getMethod(MethodName, Parameter.getClass()).invoke(Parameter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getPlayerField(Player Player, String Field) {
        try {
            Object P = Player.getClass().getMethod("getHandle").invoke(Player);
            return P.getClass().getField(Field).get(P);
        } catch (Error | Exception e) {
            return null;
        }
    }

    private Integer getActiveContainerStateId(Player player) {
        try {
            return getActiveContainerStateId(CraftPlayer.getMethod("getHandle").invoke(player));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer getActiveContainerStateId(Object entityplayer) {
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                try {
                    // EntityHuman -> Container
                    Field field = entityplayer.getClass().getField("bU");
                    Object container = this.CraftContainer.cast(field.get(entityplayer));
                    Method field2 = container.getClass().getMethod("j");
                    Object ids = field2.invoke(container);
                    return (int) ids;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    public Integer getActiveContainerId(Player player) {
        try {
            return getActiveContainerId(CraftPlayer.getMethod("getHandle").invoke(player));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer getActiveContainerId(Object entityplayer) {

        String activeContainer = "activeContainer";
        String windowId = "windowId";

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
                windowId = "j";

            // EntityHuman -> Container
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                activeContainer = "bR";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R3)) {
                activeContainer = "bP";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                activeContainer = "bU";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_18_R2)) {
                activeContainer = "bV";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
                activeContainer = "bW";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                activeContainer = "bV";
            }

            Object container = CraftContainer.cast(entityplayer.getClass().getField(activeContainer).get(entityplayer));
            return (int) container.getClass().getField(windowId).get(container);
        } catch (Throwable e) {
            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
                e.printStackTrace();
        }

        return null;
    }

    private Object getContainer(String name) {
        try {
            Field field = CraftContainers.getDeclaredField(name);
            return field.get(CraftContainers);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateInventoryTitle(Player p, String title) {

        if (Version.isCurrentEqualOrHigher(Version.v1_16_R1)) {
            if (CMIChatColor.stripColor(title).length() > 64) {
                title = title.substring(0, 63) + "~";
            }
        } else {
            if (title.length() > 32) {
                title = title.substring(0, 31) + "~";
            }
        }

        try {

            if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
                Object entityplayer = CraftPlayer.getMethod("getHandle").invoke(p);

                Object s = null;
                if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                    switch (p.getOpenInventory().getTopInventory().getSize()) {
                    case 9:
                        s = getContainer("a");
                        break;
                    case 18:
                        s = getContainer("b");
                        break;
                    case 27:
                        s = getContainer("c");
                        break;
                    case 36:
                        s = getContainer("d");
                        break;
                    case 45:
                        s = getContainer("e");
                        break;
                    case 54:
                        s = getContainer("f");
                        break;
                    default:
                        return;
                    }
                } else {
                    switch (p.getOpenInventory().getTopInventory().getSize()) {
                    case 9:
                        s = getContainer("GENERIC_9X1");
                        break;
                    case 18:
                        s = getContainer("GENERIC_9X2");
                        break;
                    case 27:
                        s = getContainer("GENERIC_9X3");
                        break;
                    case 36:
                        s = getContainer("GENERIC_9X4");
                        break;
                    case 45:
                        s = getContainer("GENERIC_9X5");
                        break;
                    case 54:
                        s = getContainer("GENERIC_9X6");
                        break;
                    default:
                        return;
                    }
                }

                if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {

                    RawMessage rm = new RawMessage();
                    rm.addText(title);

                    Constructor<?> packet = PacketPlayOutOpenWindow.getConstructor(int.class, CraftContainers, net.minecraft.network.chat.IChatBaseComponent.class);

                    Object newPack = packet.newInstance(getActiveContainerId(entityplayer), s, textToIChatBaseComponent(rm.getRaw()));
                    sendPlayerPacket(p, newPack);

                    // Need to update inventory to actually update existing items
                    p.updateInventory();

                    // Deprecated, we can use bukkit API as above
//		    Field field = entityplayer.getClass().getField("bV");
//		    net.minecraft.world.inventory.Container container = (net.minecraft.world.inventory.Container) this.CraftContainer.cast(field.get(entityplayer));
//		    net.minecraft.network.protocol.game.PacketPlayOutWindowItems pac = null;
//		    if (Version.isCurrentEqualOrLower(Version.v1_17_R1) && Version.isCurrentSubEqualOrLower(0)) {
////			pac = new net.minecraft.network.protocol.game.PacketPlayOutWindowItems(getActiveContainerId(entityplayer), container.c());
//			p.updateInventory();
//		    } else {
//			pac = new net.minecraft.network.protocol.game.PacketPlayOutWindowItems(getActiveContainerId(entityplayer), 0, container.c(), container.c().get(0));
//		    }
//		    sendPlayerPacket(p, pac); 
                } else {

                    RawMessage rm = new RawMessage();
                    rm.addText(title);
                    Constructor<?> packet = PacketPlayOutOpenWindow.getConstructor(int.class, CraftContainers, getMinecraftClass("IChatBaseComponent"));
                    Object newPack = packet.newInstance(getActiveContainerId(entityplayer), s, textToIChatBaseComponent(rm.getRaw()));
                    sendPlayerPacket(p, newPack);

                    Field field = entityplayer.getClass().getField("activeContainer");
                    Object container = CraftContainer.cast(field.get(entityplayer));

                    Method meth = entityplayer.getClass().getMethod("updateInventory", CraftContainer);
                    meth.invoke(entityplayer, container);
                }

            } else if (Version.isCurrentEqualOrHigher(Version.v1_8_R2)) {

                Object entityplayer = CraftPlayer.getMethod("getHandle").invoke(p);

                RawMessage rm = new RawMessage();
                rm.addText(title);

                Constructor<?> packet = PacketPlayOutOpenWindow.getConstructor(int.class, String.class, getMinecraftClass("IChatBaseComponent"), int.class);
                Object newPack = packet.newInstance(getActiveContainerId(entityplayer), "minecraft:chest", textToIChatBaseComponent(rm.getRaw()), p.getOpenInventory().getTopInventory()
                    .getSize());

                sendPlayerPacket(p, newPack);

                Field field = entityplayer.getClass().getField("activeContainer");
                Object container = CraftContainer.cast(field.get(entityplayer));

                Method meth = entityplayer.getClass().getMethod("updateInventory", CraftContainer);
                meth.invoke(entityplayer, container);

            }
        } catch (Throwable e) {
//	    e.printStackTrace(); 
        }
    }

    @Deprecated
    public ItemStack HideFlag(ItemStack item, int state) {
        return CMINBT.HideFlag(item, state);
    }

    public void superficialEntityTeleport(Player player, Object entity, Location targetLoc) {
        try {
            Object centity = CEntity.cast(entity);
            Method method = null;
            if (Version.isCurrentEqualOrHigher(Version.v1_18_R1))
                method = centity.getClass().getMethod("a", double.class, double.class, double.class);
            else
                method = centity.getClass().getMethod("setPosition", double.class, double.class, double.class);
            method.invoke(centity, targetLoc.getX(), targetLoc.getY(), targetLoc.getZ());
            Constructor<?> packet = PacketPlayOutEntityTeleport.getConstructor(CEntity);
            Object newPack = packet.newInstance(centity);
            sendPlayerPacket(player, newPack);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void spawnInEntityData(Player player, Entity entity) {

        if (Version.isCurrentEqualOrHigher(Version.v1_19_R2))
            return;

        try {
            Constructor<?> packet = null;
            if (Version.isCurrentEqualOrHigher(Version.v1_19_R2))
                packet = PacketPlayOutSpawnEntityLiving.getConstructor(CEntity);
            else
                packet = PacketPlayOutSpawnEntityLiving.getConstructor(EntityLiving);

            Object craftEntity = CraftEntity.cast(entity);
            Object craftEntityHandle = CraftEntity.getMethod("getHandle").invoke(craftEntity);

            if (EntityLiving.isInstance(craftEntityHandle)) {
                Object living = EntityLiving.cast(craftEntityHandle);
                Object newPack = packet.newInstance(living);
                this.sendPlayerPacket(player, newPack);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            Object craftEntity = CraftEntity.cast(entity);
            int id = (int) CraftEntity.getMethod("getEntityId").invoke(craftEntity);
            Object craftEntityHandle = CraftEntity.getMethod("getHandle").invoke(craftEntity);

            String methodName = "getDataWatcher";
            if (Version.isCurrentEqualOrHigher(Version.v1_19_R2))
                methodName = "al";
            else if (Version.isCurrentEqualOrHigher(Version.v1_18_R1))
                methodName = "ai";

            Object watcher = craftEntityHandle.getClass().getMethod(methodName).invoke(craftEntityHandle);

            Constructor<?> packet = null;
            Object newPack = null;

            if (Version.isCurrentEqualOrHigher(Version.v1_19_R2)) {
                packet = PacketPlayOutEntityMetadata.getConstructor(int.class, List.class);
                newPack = packet.newInstance(id, watcher.getClass().getMethod("b").invoke(watcher));
            } else {
                packet = PacketPlayOutEntityMetadata.getConstructor(int.class, DataWatcher, boolean.class);
                newPack = packet.newInstance(id, watcher, true);
            }

            Object pack = newPack;

            this.sendPlayerPacket(player, pack);

            CMIScheduler.get().runTaskLater(() -> {
                try {
                    this.sendPlayerPacket(player, pack);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }, 20L);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void playSound(Player player, Location location, Sound sound, float volume, float pitch) {
        Object cPlayer = this.getCraftPlayer(player);
        try {
            cPlayer.getClass().getMethod("playSound", Location.class, Sound.class, float.class, float.class).invoke(cPlayer, location, sound, volume, pitch);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public int getEggId(ItemStack item) {
        EntityType type = getEggType(item);
        return type == null ? 0 : type.getTypeId();
    }

    // For 1.13 and older servers
    @Deprecated
    public EntityType getEggType(ItemStack item) {
        return CMINBT.getEggType(item);
    }

    // For 1.13 and older servers
    @Deprecated
    public ItemStack setEggType(ItemStack item, EntityType etype) {
        return CMINBT.setEggType(item, etype);
    }

    public ItemStack modifyItemStack(ItemStack stack, String arguments) {
        return CMINBT.modifyItemStack(stack, arguments);
    }

    public int getHoneyLevel(Block block) {
        if (CMIMaterial.get(block) != CMIMaterial.BEE_NEST && CMIMaterial.get(block) != CMIMaterial.BEEHIVE)
            return 0;
        try {
            Object nb = CraftBeehive.cast(block.getBlockData());
            Method method = nb.getClass().getMethod("getHoneyLevel");
            return (int) method.invoke(nb);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMaxHoneyLevel(Block block) {
        if (CMIMaterial.get(block) != CMIMaterial.BEE_NEST && CMIMaterial.get(block) != CMIMaterial.BEEHIVE)
            return 0;
        try {
            Object nb = CraftBeehive.cast(block.getBlockData());
            Method method = nb.getClass().getMethod("getMaximumHoneyLevel");
            return (int) method.invoke(nb);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 5;
    }

    Constructor<?> effectConstructor = null;
    Method CraftParticleMethod = null;

    public void playEffect(Player player, Location location, CMIEffect ef) {
        if (location == null || ef == null || location.getWorld() == null || player == null || !player.isOnline())
            return;

        if (!location.getWorld().equals(player.getWorld()))
            return;

        if (ef.getParticle() == null)
            return;

        if (!ef.getParticle().isParticle())
            return;

        try {

            if (Version.isCurrentEqualOrLower(Version.v1_7_R4)) {

                Effect effect = ef.getParticle().getEffect();

                if (effect == null)
                    return;

                if (effectConstructor == null)
                    effectConstructor = PacketPlayOutWorldParticles.getConstructor(String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
                Object newPack = effectConstructor.newInstance(effect.name(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) ef.getOffset().getX(),
                    (float) ef.getOffset().getY(), (float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount());
                sendPlayerPacket(player, newPack);
            } else if (Version.isCurrentEqualOrLower(Version.v1_12_R1)) {

                Effect effect = ef.getParticle().getEffect();

                if (effect == null)
                    return;

                Object particle = ef.getParticle().getEnumParticle() == null ? null : EnumParticle.cast(ef.getParticle().getEnumParticle());
                int[] extra = ef.getParticle().getExtra();
                if (particle == null) {
                    for (Object p : EnumParticle.getEnumConstants()) {
                        if (effect.name().replace("_", "").equalsIgnoreCase((p.toString().replace("_", ""))) ||
                            ef.getParticle().getName().replace("_", "").equalsIgnoreCase((p.toString().replace("_", ""))) ||
                            ef.getParticle().getSecondaryName().replace("_", "").equalsIgnoreCase((p.toString().replace("_", "")))) {
                            particle = p;
                            if (ef.getParticle().getEffect().getData() != null) {
                                extra = new int[] { (0 << 12) | (0 & 0xFFF) };
                            }
                            break;
                        }
                    }
                    if (extra == null) {
                        extra = new int[0];
                    }
                }

                if (particle == null)
                    return;

                if (ef.getParticle().getEnumParticle() == null) {
                    ef.getParticle().setEnumParticle(particle);
                    ef.getParticle().setExtra(extra);
                }

                if (effectConstructor == null)
                    effectConstructor = PacketPlayOutWorldParticles.getConstructor(EnumParticle, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class,
                        int.class, int[].class);
                Object newPack = effectConstructor.newInstance(particle, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) ef.getOffset().getX(),
                    (float) ef.getOffset().getY(),
                    (float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount(), extra);

                sendPlayerPacket(player, newPack);
            } else if (Version.isCurrentEqualOrLower(Version.v1_14_R1)) {

                org.bukkit.Particle particle = ef.getParticle().getParticle();

                if (particle == null)
                    return;

                org.bukkit.Particle.DustOptions dd = null;
                if (particle.equals(org.bukkit.Particle.REDSTONE))
                    dd = new org.bukkit.Particle.DustOptions(ef.getColor(), ef.getSize());

                if (CraftParticleMethod == null)
                    CraftParticleMethod = CraftParticle.getMethod("toNMS", org.bukkit.Particle.class, Object.class);

                if (effectConstructor == null)
                    effectConstructor = PacketPlayOutWorldParticles.getConstructor(ParticleParam, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class,
                        float.class, int.class);

                Object param = CraftParticleMethod.invoke(null, particle, dd);
                Object packet = effectConstructor.newInstance(param, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) ef
                    .getOffset().getX(), (float) ef.getOffset().getY(), (float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount());
                sendPlayerPacket(player, packet);

            } else if (Version.isCurrentEqualOrLower(Version.v1_16_R3)) {

                org.bukkit.Particle particle = ef.getParticle().getParticle();

                if (particle == null)
                    return;

                org.bukkit.Particle.DustOptions dd = null;
                if (particle.equals(org.bukkit.Particle.REDSTONE))
                    dd = new org.bukkit.Particle.DustOptions(ef.getColor(), ef.getSize());

                if (CraftParticleMethod == null)
                    CraftParticleMethod = CraftParticle.getMethod("toNMS", org.bukkit.Particle.class, Object.class);

                if (effectConstructor == null)
                    effectConstructor = PacketPlayOutWorldParticles.getConstructor(ParticleParam, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class,
                        float.class, int.class);

                Object packet = effectConstructor.newInstance(CraftParticleMethod.invoke(null, particle, dd), true, location.getX(), location.getY(), location.getZ(), (float) ef
                    .getOffset().getX(), (float) ef.getOffset().getY(), (float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount());
                sendPlayerPacket(player, packet);

            } else {

                org.bukkit.Particle particle = ef.getParticle().getParticle();

                if (particle == null)
                    return;

//		org.bukkit.Particle.DustOptions dd = null;
                Object dd = null;
                if (particle.equals(org.bukkit.Particle.REDSTONE)) {
                    dd = new org.bukkit.Particle.DustOptions(ef.getColor(), ef.getSize());
                } else if (ef.getParticle().getDataType().equals(CMIParticleDataType.BlockData)) {
                    dd = Bukkit.createBlockData(ef.getMaterial() == null ? CMIMaterial.STONE.getMaterial() : ef.getMaterial().getMaterial());
                }

                if (CraftParticleMethod == null)
                    CraftParticleMethod = CraftParticle.getMethod("toNMS", org.bukkit.Particle.class, Object.class);

                net.minecraft.network.protocol.game.PacketPlayOutWorldParticles packet = new net.minecraft.network.protocol.game.PacketPlayOutWorldParticles(
                    (net.minecraft.core.particles.ParticleParam) CraftParticleMethod.invoke(null, particle, dd),
                    true,
                    location.getX(), location.getY(), location.getZ(),
                    (float) ef.getOffset().getX(),
                    (float) ef.getOffset().getY(),
                    (float) ef.getOffset().getZ(),
                    ef.getSpeed(),
                    ef.getAmount());

                sendPlayerPacket(player, packet);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void removeAdvancement(net.Zrips.CMILib.Advancements.CMIAdvancement ad) {

        if (Version.isCurrentLower(Version.v1_12_R1))
            return;

        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                Map<net.minecraft.resources.MinecraftKey, net.minecraft.advancements.Advancement> advancements =
                    (Map<net.minecraft.resources.MinecraftKey, net.minecraft.advancements.Advancement>) advancementRegistry.getClass().getField("b").get(advancementRegistry);
                if (ad.getMinecraftKey() != null)
                    advancements.remove(ad.getMinecraftKey());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
            Bukkit.getUnsafe().removeAdvancement(ad.getId());
            try {
                Object server = MinecraftServerClass.getDeclaredMethod("getServer").invoke(MinecraftServer);
                Object AdvancementData = server.getClass().getMethod("getAdvancementData").invoke(server);
                Object REGISTRY = AdvancementData.getClass().getField("REGISTRY").get(AdvancementData);

                Map<Object, Object> advs = (Map<Object, Object>) REGISTRY.getClass().getField("advancements").get(REGISTRY);
                if (ad.getMinecraftKey() != null)
                    advs.remove(ad.getMinecraftKey());
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        } else {
//	    Bukkit.getUnsafe().removeAdvancement(ad.getId());
//	    try {
//		Object REGISTRY = AdvancementDataWorld.getField("REGISTRY").get(AdvancementDataWorld);
//		Map<Object, Object> advs = (Map<Object, Object>) REGISTRY.getClass().getField("advancements").get(REGISTRY);
//		if (ad.getMinecraftKey() != null)
//		    advs.remove(ad.getMinecraftKey());
//	    } catch (Throwable e) {
//		e.printStackTrace();
//	    }
        }
    }

    public void loadAdvancement(net.Zrips.CMILib.Advancements.CMIAdvancement ad, String advancement) {
        if (Version.isCurrentLower(Version.v1_12_R1))
            return;

        if (Bukkit.getAdvancement(ad.getId()) != null) {
            return;
        }

        NamespacedKey key = ad.getId();
        if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {

            if (Bukkit.getAdvancement(key) != null) {
                return;
            }

            try {
                net.minecraft.resources.MinecraftKey minecraftkey = (net.minecraft.resources.MinecraftKey) CraftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class).invoke(CraftNamespacedKey, key);
                Object fieldB = AdvancementDataWorld.getField("b").get(AdvancementDataWorld);
                Object jsonelement = fieldB.getClass().getMethod("fromJson", String.class, Class.class).invoke(fieldB, advancement, JsonElement.class);
                Object jsonobject = ChatDeserializer.getMethod("m", JsonElement.class, String.class).invoke(ChatDeserializer, jsonelement, "advancement");
                Method meth = SerializedAdvancement.getMethod("a", jsonobject.getClass(), LootDeserializationContext);

                Object LootPredicateManager = null;

                LootPredicateManager = net.minecraft.server.MinecraftServer.getServer().aH();

                net.minecraft.advancements.critereon.LootDeserializationContext LDC = new net.minecraft.advancements.critereon.LootDeserializationContext(
                    minecraftkey, (net.minecraft.world.level.storage.loot.LootDataManager) LootPredicateManager);
                net.minecraft.advancements.Advancement.SerializedAdvancement nms = (net.minecraft.advancements.Advancement.SerializedAdvancement) meth.invoke(SerializedAdvancement, jsonobject,
                    LDC);

                if (nms != null) {
                    //getAdvancementData
                    net.minecraft.server.MinecraftServer.getServer().az().c.a(Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {

            if (Bukkit.getAdvancement(key) != null) {
                return;
            }

            try {
                net.minecraft.resources.MinecraftKey minecraftkey = (net.minecraft.resources.MinecraftKey) CraftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class).invoke(CraftNamespacedKey, key);
                Object fieldB = AdvancementDataWorld.getField("b").get(AdvancementDataWorld);
                Object jsonelement = fieldB.getClass().getMethod("fromJson", String.class, Class.class).invoke(fieldB, advancement, JsonElement.class);
                Object jsonobject = ChatDeserializer.getMethod("m", JsonElement.class, String.class).invoke(ChatDeserializer, jsonelement, "advancement");
                Method meth = SerializedAdvancement.getMethod("a", jsonobject.getClass(), LootDeserializationContext);

                Object LootPredicateManager = null;

                if (Version.isCurrentEqualOrLower(Version.v1_18_R1))
                    LootPredicateManager = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("aH").invoke(net.minecraft.server.MinecraftServer.getServer());
                else if (Version.isCurrentEqualOrLower(Version.v1_18_R2))
                    LootPredicateManager = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("aG").invoke(net.minecraft.server.MinecraftServer.getServer());
                else if (Version.isCurrentEqualOrLower(Version.v1_19_R1))
                    LootPredicateManager = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("aI").invoke(net.minecraft.server.MinecraftServer.getServer());
                else if (Version.isCurrentEqualOrLower(Version.v1_19_R2))
                    LootPredicateManager = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("aH").invoke(net.minecraft.server.MinecraftServer.getServer());
                else if (Version.isCurrentEqualOrLower(Version.v1_19_R3))
                    LootPredicateManager = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("aI").invoke(net.minecraft.server.MinecraftServer.getServer());

                if (LootPredicateManager == null)
                    return;

                Constructor<net.minecraft.advancements.critereon.LootDeserializationContext> constructor = net.minecraft.advancements.critereon.LootDeserializationContext.class.getConstructor(
                    net.minecraft.resources.MinecraftKey.class, LootPredicateManager.getClass());

                net.minecraft.advancements.critereon.LootDeserializationContext LDC = constructor.newInstance(minecraftkey, LootPredicateManager);

//                net.minecraft.advancements.critereon.LootDeserializationContext LDC = new net.minecraft.advancements.critereon.LootDeserializationContext(
//                    (net.minecraft.resources.MinecraftKey) minecraftkey, (net.minecraft.world.level.storage.loot.LootPredicateManager) LootPredicateManager);
                net.minecraft.advancements.Advancement.SerializedAdvancement nms = (net.minecraft.advancements.Advancement.SerializedAdvancement) meth.invoke(SerializedAdvancement, jsonobject,
                    LDC);

                if (nms != null) {
                    //getAdvancementData

                    if (Version.isCurrentEqualOrHigher(Version.v1_19_R3))
                        net.minecraft.server.MinecraftServer.getServer().az().c.a(Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
                    else if (Version.isCurrentEqualOrHigher(Version.v1_19_R2)) {
                        Object ay = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("ay").invoke(net.minecraft.server.MinecraftServer.getServer());
                        Object c = ay.getClass().getField("c").get(ay);
                        c.getClass().getMethod("a", Map.class).invoke(c, Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
                    } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                        Object ax = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("az").invoke(net.minecraft.server.MinecraftServer.getServer());
                        Object c = ax.getClass().getField("c").get(ax);
                        c.getClass().getMethod("a", Map.class).invoke(c, Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
                    } else {
                        Object ax = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("ax").invoke(net.minecraft.server.MinecraftServer.getServer());
                        Object c = ax.getClass().getField("c").get(ax);
                        c.getClass().getMethod("a", Map.class).invoke(c, Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
                    }
                }

//		net.minecraft.server.MinecraftServer.getServer().ax().c.a(Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                net.minecraft.resources.MinecraftKey minecraftkey = (net.minecraft.resources.MinecraftKey) CraftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class).invoke(CraftNamespacedKey, key);
                Object fieldB = AdvancementDataWorld.getField("b").get(AdvancementDataWorld);
                Object jsonelement = fieldB.getClass().getMethod("fromJson", String.class, Class.class).invoke(fieldB, advancement, JsonElement.class);
                Object jsonobject = ChatDeserializer.getMethod("m", JsonElement.class, String.class).invoke(ChatDeserializer, jsonelement, "advancement");
                Method meth = SerializedAdvancement.getMethod("a", jsonobject.getClass(), LootDeserializationContext);
                Object LootPredicateManager = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("getLootPredicateManager").invoke(net.minecraft.server.MinecraftServer.getServer());

                Constructor<net.minecraft.advancements.critereon.LootDeserializationContext> constructor = net.minecraft.advancements.critereon.LootDeserializationContext.class.getConstructor(
                    net.minecraft.resources.MinecraftKey.class, LootPredicateManager.getClass());
                net.minecraft.advancements.critereon.LootDeserializationContext LDC = constructor.newInstance(minecraftkey, LootPredicateManager);

//                net.minecraft.advancements.critereon.LootDeserializationContext LDC = new net.minecraft.advancements.critereon.LootDeserializationContext(
//                    (net.minecraft.resources.MinecraftKey) minecraftkey, (net.minecraft.world.level.storage.loot.LootPredicateManager) LootPredicateManager);

                net.minecraft.advancements.Advancement.SerializedAdvancement nms = (net.minecraft.advancements.Advancement.SerializedAdvancement) meth.invoke(SerializedAdvancement, jsonobject, LDC);
                Object data = net.minecraft.server.MinecraftServer.getServer().getClass().getMethod("getAdvancementData").invoke(net.minecraft.server.MinecraftServer.getServer());
                net.minecraft.advancements.Advancements advanceents = (Advancements) data.getClass().getField("c").get(data);
                advanceents.a(Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Version.isCurrentEqualOrHigher(Version.v1_16_R1)) {
            try {
                Object minecraftkey = CraftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class).invoke(CraftNamespacedKey, key);
                ad.setMinecraftKey(minecraftkey);
                Object DESERIALIZER = AdvancementDataWorld.getField("DESERIALIZER").get(AdvancementDataWorld);
                Object jsonelement = DESERIALIZER.getClass().getMethod("fromJson", String.class, Class.class).invoke(DESERIALIZER, advancement, JsonElement.class);
                Object jsonobject = ChatDeserializer.getMethod("m", JsonElement.class, String.class).invoke(ChatDeserializer, jsonelement, "advancement");
                Object Server = MinecraftServerClass.getDeclaredMethod("getServer").invoke(MinecraftServer);
                Object PredicateManager = Server.getClass().getMethod("getLootPredicateManager").invoke(Server);
                Method meth = SerializedAdvancement.getMethod("a", jsonobject.getClass(), LootDeserializationContext);
                Object LootDeserialization = LootDeserializationContext.getConstructor(minecraftkey.getClass(), PredicateManager.getClass()).newInstance(minecraftkey, PredicateManager);
                Object nms = meth.invoke(SerializedAdvancement, jsonobject, LootDeserialization);
                if (nms != null) {
                    Object AdvancementData = Server.getClass().getMethod("getAdvancementData").invoke(Server);
                    Object REGISTRY = AdvancementData.getClass().getField("REGISTRY").get(AdvancementData);
                    REGISTRY.getClass().getMethod("a", Map.class).invoke(REGISTRY, Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            // Fix
//	    if (plugin.isCmiPresent()) {
//		CMI.getInstance().getNMS().loadAdvancement(key, advancement);
//	    }
        }
//	Object minecraftkey = CraftNamespacedKey.getClass().getMethod("toMinecraft", NamespacedKey.class).invoke(CraftNamespacedKey, key);
//	
////	MinecraftKey minecraftkey = CraftNamespacedKey.toMinecraft(key);
//	JsonElement jsonelement = AdvancementDataWorld.b.fromJson(advancement, JsonElement.class);
//	com.google.gson.JsonObject jsonobject = ChatDeserializer.m(jsonelement, "advancement");
//	SerializedAdvancement nms = SerializedAdvancement.a(jsonobject, new LootDeserializationContext(minecraftkey, MinecraftServer.getServer().getLootPredicateManager()));
//	if (nms != null) {
//	    advancementRegistry
//	    
//	    .a(Maps.newHashMap(Collections.singletonMap(minecraftkey, nms)));
//	}
    }

    public ItemStack addAttributes(List<Attribute> ls, ItemStack item) {
        if (ls.isEmpty())
            return item;

        try {

            CMINBT nbt = new CMINBT(item);

            Object modifiers = nbt.getObjectList("AttributeModifiers", 10);

            if (modifiers == null)
                modifiers = CMINBT.newNBTTagList(10);

            Method sizeMeth = modifiers.getClass().getMethod("size");
            for (Attribute one : ls) {
                Object att = nbtTagCompound.newInstance();

                CMINBT subNBT = new CMINBT(att);
                subNBT.setString("AttributeName", one.getType().getFullName());
                subNBT.setString("Name", "generic." + one.getType().getName());
                if (one.getSlot() != null)
                    subNBT.setString("Slot", one.getSlot().getName());
                if (one.getOperation() > 0 && one.getOperation() < 3)
                    subNBT.setInt("Operation", one.getOperation());

                if (Version.isCurrentEqualOrLower(Version.v1_15_R2)) {
                    UUID uuid = UUID.randomUUID();
                    subNBT.setInt("UUIDLeast", (int) uuid.getLeastSignificantBits());
                    subNBT.setInt("UUIDMost", (int) uuid.getMostSignificantBits());
                } else {
                    Random rand = new Random();
                    int[] uuids = new int[] { rand.nextInt(160000), rand.nextInt(160000), rand.nextInt(160000), rand.nextInt(160000) };
                    subNBT.setIntArray("UUID", uuids);
                }

                subNBT.setDouble("Amount", one.getMod());
                CMINBT.addToList(modifiers, (int) sizeMeth.invoke(modifiers), att);
            }
            item = (ItemStack) nbt.set("AttributeModifiers", modifiers);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return item;
    }

//    public void registerCMIGlowEffect() {
//	try {
//	    Field f = Enchantment.class.getDeclaredField("acceptingNew");
//	    f.setAccessible(true);
//	    f.set(null, true);
//	} catch (Throwable e) {
//	    e.printStackTrace();
//	}
//	try {
//	    NamespacedKey key = new NamespacedKey(plugin, "CMIGlowEffect");
//	    CMIItemGlow glow = new CMIItemGlow(key);
//	    Enchantment.registerEnchantment(glow);
//	} catch (Throwable e) {
//	    e.printStackTrace();
//	}
//    }
}
