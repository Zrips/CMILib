/**
 * Copyright (C) 2017 Zrips
 */

package net.Zrips.CMILib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
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

import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.Zrips.CMILib.Advancements.CMIAdvancement;
import net.Zrips.CMILib.Attributes.Attribute;
import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Container.CMIPlayerConnection;
import net.Zrips.CMILib.Container.CMIPlayerInventory;
import net.Zrips.CMILib.Container.CMIServerProperties;
import net.Zrips.CMILib.Effects.CMIEffect;
import net.Zrips.CMILib.Effects.CMIEffectManager;
import net.Zrips.CMILib.Items.CMIItemStack;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.NBT.CMINBT;
import net.Zrips.CMILib.RawMessages.RawMessage;
import net.Zrips.CMILib.Skins.SkinManager;
import net.Zrips.CMILib.Version.Version;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public class Reflections {

    private Class<?> CraftServerClass;
    private Class<?> CraftWorldClass;
    private Class<?> WorldServerClass;
    private Object CraftServer;

    private Class<?> MinecraftServerClass;
    private Class<?> PropertyManagerClass;
    private Object MinecraftServer;

    private Class<?> EntityHuman;
    private Class<?> CraftPlayer;
    private Class<?> CraftEntity;
    private Class<?> CEntity;
    private Class<?> nbtTagCompound;
    private Class<?> EntityLiving;
    private Class<?> BlockPosition;
    private Class<?> CraftBeehive;
    private Class<?> CraftNamespacedKey;
    private Class<?> Item;
    private Class<?> IStack;
    private Class<?> IChatBaseComponent;
    private Class<?> nmsChatSerializer;
    private Class<?> dimensionManager;

    private Class<?> CraftContainer;
    private Class<?> CraftContainers;
    private Class<?> PacketPlayOutOpenWindow;
    private Class<?> PacketPlayOutEntityTeleport;
    private Class<?> PacketPlayOutSpawnEntityLiving;
    private Class<?> PacketPlayOutEntityMetadata;
    private Class<?> PacketPlayOutSetSlot;
    private Class<?> DataWatcher;
    private Method sendPacket;
    private Class<?> AdvancementDataWorld;
    private Class<?> ChatDeserializer;
    private Class<?> SerializedAdvancement;
    private Class<?> LootDeserializationContext;
    private Class<?> CraftChatMessage;

    private Class<?> world;

    private Class<?> advancementFrameTypeClass = null;
    private Class<?> minecraftKeyClass = null;
    private Class<?> identifierClass = null;
    private Method resourceKeyMethod = null;
    private Method identifierMethod = null;
    private Class<?> advancementDisplayClass = null;
    private Class<?> advancementRequirementsClass = null;
    private Class<?> advancementProgressClass = null;
    private Class<?> packetPlayOutAdvancementsClass = null;

    Class<?> vec3DClass = null;
    Class<?> posMoveRotClass = null;
    Class<?> teleportPacketClass = null;

    private Field getConnectionField = null;

    private Object advancementRegistry;

    private CMILib plugin;

    public Reflections(CMILib plugin) {
        this.plugin = plugin;
        initialize();
    }

    private void initialize() {

        if (Version.isMojangMappings()) {
            try {
                world = Class.forName("net.minecraft.world.level.Level");
                sendPacket = Class.forName("net.minecraft.server.network.ServerGamePacketListenerImpl").getMethod("send", Class.forName("net.minecraft.network.protocol.Packet"));
                CraftServerClass = Class.forName("org.bukkit.craftbukkit.CraftServer");
                CraftServer = CraftServerClass.cast(Bukkit.getServer());
                MinecraftServer = CraftServer.getClass().getMethod("getServer").invoke(CraftServer);
                CraftWorldClass = Class.forName("org.bukkit.craftbukkit.CraftWorld");
                MinecraftServerClass = Class.forName("net.minecraft.server.MinecraftServer");
                CraftBeehive = Class.forName("org.bukkit.craftbukkit.block.impl.CraftBeehive");
                CraftNamespacedKey = Class.forName("org.bukkit.craftbukkit.util.CraftNamespacedKey");
                PacketPlayOutEntityTeleport = Class.forName("net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket");
                EntityHuman = Class.forName("net.minecraft.world.entity.player.Player");
                PacketPlayOutSpawnEntityLiving = Class.forName("net.minecraft.network.protocol.game.ClientboundAddEntityPacket");
                PacketPlayOutEntityMetadata = Class.forName("net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket");
                PacketPlayOutSetSlot = Class.forName("net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket");
                PacketPlayOutOpenWindow = Class.forName("net.minecraft.network.protocol.game.ClientboundOpenScreenPacket");
                CraftContainer = Class.forName("net.minecraft.world.inventory.AbstractContainerMenu");
                CraftContainers = Class.forName("net.minecraft.world.inventory.MenuType");
                DataWatcher = Class.forName("net.minecraft.network.syncher.SynchedEntityData");
                AdvancementDataWorld = Class.forName("net.minecraft.server.ServerAdvancementManager");
                ChatDeserializer = Class.forName("net.minecraft.util.GsonHelper");
                SerializedAdvancement = Class.forName("net.minecraft.advancements.Advancement$Builder");
                CraftChatMessage = Class.forName("org.bukkit.craftbukkit.util.CraftChatMessage");
                WorldServerClass = Class.forName("net.minecraft.server.level.ServerLevel");
                dimensionManager = Class.forName("net.minecraft.world.level.dimension.DimensionType");
                BlockPosition = Class.forName("net.minecraft.core.BlockPos");
                IChatBaseComponent = Class.forName("net.minecraft.network.chat.Component");
                CraftPlayer = Class.forName("org.bukkit.craftbukkit.entity.CraftPlayer");
                CraftEntity = Class.forName("org.bukkit.craftbukkit.entity.CraftEntity");
                PropertyManagerClass = Class.forName("net.minecraft.server.dedicated.Settings");
                CEntity = Class.forName("net.minecraft.world.entity.Entity");
                nbtTagCompound = Class.forName("net.minecraft.nbt.CompoundTag");
                EntityLiving = Class.forName("net.minecraft.world.entity.LivingEntity");
                Item = Class.forName("net.minecraft.world.item.Item");
                IStack = Class.forName("net.minecraft.world.item.ItemStack");
                advancementFrameTypeClass = Class.forName("net.minecraft.advancements.AdvancementType");
                minecraftKeyClass = Class.forName("net.minecraft.resources.ResourceKey");
                identifierClass = Class.forName("net.minecraft.resources.Identifier");
                resourceKeyMethod = minecraftKeyClass.getMethod("createRegistryKey", identifierClass);
                identifierMethod = identifierClass.getMethod("tryBuild", String.class, String.class);

                advancementDisplayClass = Class.forName("net.minecraft.advancements.DisplayInfo");
                advancementRequirementsClass = Class.forName("net.minecraft.advancements.AdvancementRequirements");
                advancementProgressClass = Class.forName("net.minecraft.advancements.AdvancementProgress");
                packetPlayOutAdvancementsClass = Class.forName("net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket");

                vec3DClass = Class.forName("net.minecraft.world.phys.Vec3");
                posMoveRotClass = Class.forName("net.minecraft.world.entity.PositionMoveRotation");                
                teleportPacketClass = Class.forName("net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket");

            } catch (Throwable e) {
                e.printStackTrace();
            }
            return;
        }

        if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
            try {
                world = Class.forName("net.minecraft.world.level.World");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {

            try {

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                    advancementFrameTypeClass = Class.forName("net.minecraft.advancements.AdvancementFrameType");
                    minecraftKeyClass = Class.forName("net.minecraft.resources.MinecraftKey");
                    advancementDisplayClass = Class.forName("net.minecraft.advancements.AdvancementDisplay");
                    advancementRequirementsClass = Class.forName("net.minecraft.advancements.AdvancementRequirements");
                    advancementProgressClass = Class.forName("net.minecraft.advancements.AdvancementProgress");
                    packetPlayOutAdvancementsClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutAdvancements");
                }

                if (Version.isCurrentEqualOrHigher(Version.v1_21_R2)) {
                    vec3DClass = Class.forName("net.minecraft.world.phys.Vec3D");
                    posMoveRotClass = Class.forName("net.minecraft.world.entity.PositionMoveRotation");
                    teleportPacketClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport");
                }

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R2))
                    sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("b", Class.forName("net.minecraft.network.protocol.Packet"));
                else if (Version.isCurrentEqualOrHigher(Version.v1_18_R1))
                    sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("a", Class.forName("net.minecraft.network.protocol.Packet"));
                else
                    sendPacket = Class.forName("net.minecraft.server.network.PlayerConnection").getMethod("sendPacket", Class.forName("net.minecraft.network.protocol.Packet"));

                CraftServerClass = getBukkitClass("CraftServer");
                CraftServer = CraftServerClass.cast(Bukkit.getServer());

                CraftWorldClass = getBukkitClass("CraftWorld");
                MinecraftServerClass = Class.forName("net.minecraft.server.MinecraftServer");

                CraftBeehive = getBukkitClass("block.impl.CraftBeehive");
                CraftNamespacedKey = getBukkitClass("util.CraftNamespacedKey");
                PacketPlayOutEntityTeleport = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport");

                EntityHuman = Class.forName("net.minecraft.world.entity.player.EntityHuman");

                if (Version.isCurrentEqualOrLower(Version.v1_18_R2)) {
                    PacketPlayOutSpawnEntityLiving = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntityLiving");
                } else {
                    PacketPlayOutSpawnEntityLiving = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity");
                }

                PacketPlayOutEntityMetadata = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata");
                PacketPlayOutSetSlot = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSetSlot");
                PacketPlayOutOpenWindow = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutOpenWindow");
                CraftContainer = Class.forName("net.minecraft.world.inventory.Container");
                CraftContainers = Class.forName("net.minecraft.world.inventory.Containers");
                DataWatcher = Class.forName("net.minecraft.network.syncher.DataWatcher");

                AdvancementDataWorld = Class.forName("net.minecraft.server.AdvancementDataWorld");
                ChatDeserializer = Class.forName("net.minecraft.util.ChatDeserializer");
                SerializedAdvancement = Class.forName("net.minecraft.advancements.Advancement$SerializedAdvancement");

                if (Version.isCurrentEqualOrLower(Version.v1_20_R2)) {
                    LootDeserializationContext = Class.forName("net.minecraft.advancements.critereon.LootDeserializationContext");
                }

                CraftChatMessage = getBukkitClass("util.CraftChatMessage");

                WorldServerClass = Class.forName("net.minecraft.server.level.WorldServer");
                dimensionManager = Class.forName("net.minecraft.world.level.dimension.DimensionManager");
                BlockPosition = Class.forName("net.minecraft.core.BlockPosition");

                IChatBaseComponent = Class.forName("net.minecraft.network.chat.IChatBaseComponent");

                if (Version.isCurrentLower(Version.v1_21_R5))
                    nmsChatSerializer = Class.forName("net.minecraft.network.chat.IChatBaseComponent$ChatSerializer");

                MinecraftServer = CraftServer.getClass().getMethod("getServer").invoke(CraftServer);

                CraftPlayer = getBukkitClass("entity.CraftPlayer");
                CraftEntity = getBukkitClass("entity.CraftEntity");
                PropertyManagerClass = Class.forName("net.minecraft.server.dedicated.PropertyManager");

                CEntity = Class.forName("net.minecraft.world.entity.Entity");

                nbtTagCompound = Class.forName("net.minecraft.nbt.NBTTagCompound");
                EntityLiving = Class.forName("net.minecraft.world.entity.EntityLiving");
                Item = Class.forName("net.minecraft.world.item.Item");
                IStack = Class.forName("net.minecraft.world.item.ItemStack");

                String variable = "ax";
                if (Version.isCurrentEqualOrHigher(Version.v1_21_R6))
                    variable = "aF";
                else if (Version.isCurrentEqualOrHigher(Version.v1_21_R2))
                    variable = "aD";
                else if (Version.isCurrentEqualOrHigher(Version.v1_20_R4))
                    variable = "aE";
                else if (Version.isCurrentEqualOrHigher(Version.v1_20_R3))
                    variable = "aB";
                else if (Version.isCurrentEqualOrHigher(Version.v1_19_R3))
                    variable = "az";
                else if (Version.isCurrentEqualOrHigher(Version.v1_19_R2))
                    variable = "ay";
                else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1))
                    variable = "az";
                else if (Version.isCurrentLower(Version.v1_18_R1))
                    variable = "getAdvancementData";

                Object advancementData = MinecraftServer.getClass().getMethod(variable).invoke(MinecraftServer);

                if (Version.isCurrentEqualOrHigher(Version.v1_21_R2))
                    advancementRegistry = advancementData.getClass().getField("b").get(advancementData);
                else
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
                else // 1_8_R2 moved to IChatBaseComponent
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
            Object serialized = textToIChatBaseComponent(text);
            return (String) serialized.getClass().getMethod("e").invoke(serialized);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return text;
    }

    Method nmsChatSerializerMethod = null;

    public Object textToIChatBaseComponent(String text) {

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
            try {
                if (nmsChatSerializerMethod == null)
                    nmsChatSerializerMethod = CraftChatMessage.getMethod("fromJSON", String.class);
                // fromJSON(String text)
//                return org.bukkit.craftbukkit.v1_20_R4.util.CraftChatMessage.fromJSON(text);
                return nmsChatSerializerMethod.invoke(null, text);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return text;
        }

        try {
            if (nmsChatSerializerMethod == null)
                nmsChatSerializerMethod = nmsChatSerializer.getMethod("a", String.class);
            return nmsChatSerializerMethod.invoke(null, text);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return text;
    }

    public int getCurrentTick() {
        if (Version.isFolia())
            return Bukkit.getCurrentTick();

        try {
            return MinecraftServer.getClass().getField("currentTick").getInt(CraftServer);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        return (int) (System.currentTimeMillis() / 50);
    }

    public Object getItemInfo(int id, String fieldName) {
        try {
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

        if (Version.isMojangMappings()) {

            try {
                // DedicatedServer -> DedicatedServerSettings
                Object field1 = MinecraftServer.getClass().getField("settings").get(MinecraftServer);

                // DedicatedServerSettings -> DedicatedServerProperties method
                Object prop = field1.getClass().getMethod("getProperties").invoke(field1);

                // PropertyManager -> Properties

                Object field2 = prop.getClass().getField("properties").get(prop);

                Method setPropertyMethod = field2.getClass().getDeclaredMethod("setProperty", String.class, String.class);
                setPropertyMethod.invoke(field2, setting.getPath(), String.valueOf(value));

                if (save)
                    // DedicatedServerSettings -> forceSave method
                    field1.getClass().getMethod("forceSave").invoke(field1);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return;
        }

        if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {

            try {
                // DedicatedServer -> DedicatedServerSettings
                Object field1 = null;
                if (Version.isCurrentEqualOrHigher(Version.v1_21_R6)) {
                    field1 = MinecraftServer.getClass().getField("t").get(MinecraftServer);
                } else if (Version.isCurrentEqualOrHigher(Version.v1_21_R2)) {
                    field1 = MinecraftServer.getClass().getField("s").get(MinecraftServer);
                } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                    field1 = MinecraftServer.getClass().getField("r").get(MinecraftServer);
                } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {
                    field1 = MinecraftServer.getClass().getField("s").get(MinecraftServer);
                } else
                    field1 = MinecraftServer.getClass().getField("u").get(MinecraftServer);

                // DedicatedServerSettings -> DedicatedServerProperties method
                Object prop = field1.getClass().getMethod("a").invoke(field1);

                // PropertyManager -> Properties

                Object field2 = null;
                if (Version.isCurrentEqualOrHigher(Version.v1_21_R7))
                    field2 = prop.getClass().getField("al").get(prop);
                else if (Version.isCurrentEqualOrHigher(Version.v1_21_R6))
                    field2 = prop.getClass().getField("ak").get(prop);
                else if (Version.isCurrentEqualOrHigher(Version.v1_21_R1))
                    field2 = prop.getClass().getField("ac").get(prop);
                else if (Version.isCurrentEqualOrHigher(Version.v1_20_R4))
                    field2 = prop.getClass().getField("ab").get(prop);
                else
                    field2 = prop.getClass().getField("Z").get(prop);

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

    Method getProfileMethod;

    public com.mojang.authlib.GameProfile getProfile(Player player) {
        Object craftPlayer = this.CraftPlayer.cast(player);

        try {
            if (getProfileMethod == null)
                getProfileMethod = this.CraftPlayer.getMethod("getProfile");
            Object profile = getProfileMethod.invoke(craftPlayer);
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

    Method handleMEthod = null;

    public Object getPlayerHandle(Player player) {
        Object handle = null;
        try {
            if (handleMEthod == null)
                handleMEthod = player.getClass().getMethod("getHandle");
            handle = handleMEthod.invoke(player);
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

            if (getConnectionField == null) {
                if (Version.isMojangMappings())
                    getConnectionField = handle.getClass().getField("connection");
                else if (Version.isCurrentEqualOrHigher(Version.v1_21_R5))
                    getConnectionField = handle.getClass().getField("g");
                else if (Version.isCurrentEqualOrHigher(Version.v1_21_R2))
                    getConnectionField = handle.getClass().getField("f");
                else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1))
                    getConnectionField = handle.getClass().getField("c");
                else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
                    getConnectionField = handle.getClass().getField("b");
                else
                    getConnectionField = handle.getClass().getField("playerConnection");
            }

            connection = getConnectionField.get(handle);

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

    private Method getWorldServerMeth = null;
    private Method getWorldWorldMeth = null;

    public Object getWorldServer(World world) {
        Object obj = getCraftWorld(world);
        try {

            if (getWorldServerMeth == null)
                getWorldServerMeth = obj.getClass().getMethod("getHandle");

            return WorldServerClass.cast(getWorldServerMeth.invoke(obj));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object getMineCraftWorld(World world) {
        Object obj = getCraftWorld(world);
        try {
            if (getWorldServerMeth == null)
                getWorldServerMeth = obj.getClass().getMethod("getHandle");
            Object handle = WorldServerClass.cast(getWorldServerMeth.invoke(obj));
            if (getWorldWorldMeth == null)
                getWorldWorldMeth = handle.getClass().getMethod("getMinecraftWorld");
            return this.world.cast(getWorldWorldMeth.invoke(handle));
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

    private Method getTileEntityAtMeth = null;

    public Object getTileEntityAt(Location loc) {
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_13_R2)) {
                try {
                    Object ncw = getWorldServer(loc.getWorld());
                    if (getTileEntityAtMeth == null) {
                        if (Version.isMojangMappings())
                            getTileEntityAtMeth = ncw.getClass().getMethod("getBlockEntity", BlockPosition);
                        else
                            getTileEntityAtMeth = ncw.getClass().getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "c_" : "getTileEntity", BlockPosition);
                    }
                    return getTileEntityAtMeth.invoke(ncw, getBlockPosition(loc));
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
        sendPacket(getPlayerConnection(player), packet);
    }

    public void sendPacket(Object connection, Object packet) {
        if (connection == null || packet == null)
            return;
        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                sendPacket.invoke(connection, packet);
            } else {
                connection.getClass().getMethod("sendPacket", getClass("{nms}.Packet")).invoke(connection, packet);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public String toJson(ItemStack item) {
        return CMINBT.toJson(item);
    }

    public String getItemMinecraftName(ItemStack item) {
        try {

            if (Version.isCurrentEqualOrHigher(Version.v1_21_R7))
                return null;

            Object nmsStack = asNMSCopy(item);

            if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {

                Object pre = nmsStack.getClass().getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "c" : "getItem").invoke(nmsStack);
                Object n = pre.getClass().getMethod(Version.isCurrentEqualOrHigher(Version.v1_18_R1) ? "a" : "getName").invoke(pre);

                Class<?> ll = null;
                if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
                    ll = Class.forName("net.minecraft.locale.LocaleLanguage");
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

    public void updateItemWithPacket(Player player, ItemStack item, int slot) {

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

    private static String getSuffix(String input) {
        int hash = Math.abs(input.hashCode());
        String hex = Integer.toHexString(hash);
        return "_" + hex.substring(0, Math.min(3, hex.length()));
    }

    private static String sanitizeName(String input) {
        if (input == null || input.isEmpty())
            return "Player" + getSuffix("null");

        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if ((c >= 'a' && c <= 'z') ||
                    (c >= 'A' && c <= 'Z') ||
                    (c >= '0' && c <= '9') ||
                    c == '_') {
                sb.append(c);
            }
        }

        // If result is empty or too short, use hash suffix
        if (sb.length() < 3) {
            sb.append(getSuffix(input));
        }

        // Trim or pad to valid length
        while (sb.length() < 3) {
            sb.append('_');
        }

        if (sb.length() > 16) {
            sb.setLength(16);
        }

        return sb.toString();
    }

    @SuppressWarnings("deprecation")
    private static org.bukkit.profile.PlayerProfile getProfile(String name, String url) {
        name = sanitizeName(name);

        org.bukkit.profile.PlayerProfile profile = Bukkit.createPlayerProfile(UUID.nameUUIDFromBytes(url.getBytes()), name == null ? name : name.replace(" ", ""));
        org.bukkit.profile.PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(url));
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        profile.setTextures(textures);
        return profile;
    }

    public ItemStack setSkullTexture(ItemStack item, String customProfileName, String texture) {

        if (item == null)
            return null;

        try {
            com.mojang.authlib.GameProfile prof = null;
            if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                String decodedString = new String(java.util.Base64.getMimeDecoder().decode(texture)).replace("\n", "").replace(" ", "");
                if (decodedString.contains("url\":\"")) {
                    SkullMeta meta = (SkullMeta) item.getItemMeta();
                    meta.setOwnerProfile(getProfile(customProfileName, decodedString.split("url\":\"", 2)[1].split("\"", 2)[0]));
                    item.setItemMeta(meta);
                }
                return item;
            }

            prof = new com.mojang.authlib.GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);

            PropertyMap properties = SkinManager.getPropertyMap(prof);

            properties.removeAll("textures");
            properties.put("textures", new Property("textures", texture));

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
            }

//            if (CMILibConfig.playerNameForItemStack && customProfileName != null && !customProfileName.isEmpty() && !customProfileName.contains(" ") && !customProfileName.contains("-"))
//                headMeta.setDisplayName(LC.info_playerHeadName.get("[playerName]", customProfileName));
            item.setItemMeta(headMeta);

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
            e.printStackTrace();
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

    public Object getDedicatedServer() {
        return MinecraftServer;
    }

    @Deprecated
    public ItemStack getItemInMainHand(Player player) {
        return CMIItemStack.getItemInMainHand(player);
    }

    @Deprecated
    public void setItemInMainHand(Player player, ItemStack item) {
        CMIItemStack.setItemInMainHand(player, item);
    }

    @Deprecated
    public ItemStack getItemInOffHand(Player player) {
        return CMIItemStack.getItemInOffHand(player);
    }

    @Deprecated
    public void setItemInOffHand(Player player, ItemStack item) {
        CMIItemStack.setItemInOffHand(player, item);
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

    @Deprecated
    public Object getNmsPlayer(Player p) throws Exception {
        return getPlayerHandle(p);
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

    @Deprecated
    public void sendAllPacket(Object packet) throws Exception {
        for (Player p : Bukkit.getOnlinePlayers()) {

            Object connection = CMIPlayerConnection.getConnection(p);

            if (connection == null)
                continue;

            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                sendPacket.invoke(connection, packet);
            } else
                connection.getClass().getMethod("sendPacket", getClass("{nms}.Packet")).invoke(connection, packet);
        }
    }

    @Deprecated
    public void sendListPacket(List<String> players, Object packet) {
        try {
            for (String name : players) {

                Object connection = CMIPlayerConnection.getConnection(Bukkit.getPlayer(name));

                if (connection == null)
                    continue;

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
            Object P = getPlayerHandle(Player);
            return P.getClass().getField(Field).get(P);
        } catch (Error | Exception e) {
            return null;
        }
    }

    private Integer getActiveContainerStateId(Player player) {
        try {
            return getActiveContainerStateId(getPlayerHandle(player));
        } catch (IllegalArgumentException | SecurityException e) {
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
            return getActiveContainerId(getPlayerHandle(player));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    Field containerMenuField = null;
    Field containerIdField = null;

    private Integer getActiveContainerId(Object entityplayer) {

        String activeContainer = "activeContainer";
        String windowId = "windowId";

        if (Version.isMojangMappings()) {
            try {

                if (containerMenuField == null)
                    containerMenuField = EntityHuman.getField("containerMenu");

                Object container = CraftContainer.cast(containerMenuField.get(entityplayer));

                if (containerIdField == null)
                    containerIdField = container.getClass().getField("containerId");

                return (int) containerIdField.get(container);
            } catch (Throwable e) {
                if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
                    e.printStackTrace();
            }
            return -1;
        }

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_21_R2))
                windowId = "l";
            else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1))
                windowId = "j";

            // EntityHuman -> Container
            if (Version.isCurrentEqualOrHigher(Version.v1_21_R7)) {
                activeContainer = "cn";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_21_R6)) {
                activeContainer = "cl";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {
                activeContainer = "cn";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_21_R4)) {
                activeContainer = "bR";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_21_R1)) {
                activeContainer = "cd";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                activeContainer = "cb";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R2)) {
                activeContainer = "bS";
            } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
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

            Object container = null;
            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                container = CraftContainer.cast(EntityHuman.getField(activeContainer).get(entityplayer));
            } else {
                container = CraftContainer.cast(entityplayer.getClass().getField(activeContainer).get(entityplayer));
            }
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

            if (Version.isMojangMappings()) {

                Object entityplayer = getPlayerHandle(p);
                Object s = null;
                switch (CMIPlayerInventory.getTopInventory(p).getSize()) {
                case 9:
                    s = getContainer("GENERIC_9x1");
                    break;
                case 18:
                    s = getContainer("GENERIC_9x2");
                    break;
                case 27:
                    s = getContainer("GENERIC_9x3");
                    break;
                case 36:
                    s = getContainer("GENERIC_9x4");
                    break;
                case 45:
                    s = getContainer("GENERIC_9x5");
                    break;
                case 54:
                    s = getContainer("GENERIC_9x6");
                    break;
                default:
                    return;
                }

                RawMessage rm = new RawMessage();
                rm.addText(title);

                Constructor<?> packet = PacketPlayOutOpenWindow.getConstructor(int.class, CraftContainers, IChatBaseComponent);

                Object newPack = packet.newInstance(getActiveContainerId(entityplayer), s, textToIChatBaseComponent(rm.getRaw()));
                sendPlayerPacket(p, newPack);

                // Need to update inventory to actually update existing items
                p.updateInventory();

            } else if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
                Object entityplayer = getPlayerHandle(p);

                Object s = null;
                if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                    switch (CMIPlayerInventory.getTopInventory(p).getSize()) {
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
                    switch (CMIPlayerInventory.getTopInventory(p).getSize()) {
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

                    Constructor<?> packet = PacketPlayOutOpenWindow.getConstructor(int.class, CraftContainers, IChatBaseComponent);

                    Object newPack = packet.newInstance(getActiveContainerId(entityplayer), s, textToIChatBaseComponent(rm.getRaw()));
                    sendPlayerPacket(p, newPack);

                    // Need to update inventory to actually update existing items
                    p.updateInventory();
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

                Object entityplayer = getPlayerHandle(p);

                RawMessage rm = new RawMessage();
                rm.addText(title);

                Constructor<?> packet = PacketPlayOutOpenWindow.getConstructor(int.class, String.class, getMinecraftClass("IChatBaseComponent"), int.class);
                Object newPack = packet.newInstance(getActiveContainerId(entityplayer), "minecraft:chest", textToIChatBaseComponent(rm.getRaw()), CMIPlayerInventory.getTopInventory(p)
                        .getSize());

                sendPlayerPacket(p, newPack);

                Field field = entityplayer.getClass().getField("activeContainer");
                Object container = CraftContainer.cast(field.get(entityplayer));

                Method meth = entityplayer.getClass().getMethod("updateInventory", CraftContainer);
                meth.invoke(entityplayer, container);

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public ItemStack HideFlag(ItemStack item, int state) {
        return CMINBT.HideFlag(item, state);
    }

    private Method teleportMethod = null;
    private Constructor<?> teleportPacket = null;

    Constructor<?> vec3DConstructor = null;
    Constructor<?> posMoveRotConstructor = null;
    Method teleportFactoryMethod = null;

    public void superficialEntityTeleport(Player player, Object entity, Location targetLoc) {
        try {
            
            if (entity == null || !CEntity.isInstance(entity))
                return;

            if (Version.isMojangMappings()) {

                Object craft = entity.getClass().getMethod("getBukkitEntity").invoke(entity);
                int id = (int) craft.getClass().getMethod("getEntityId").invoke(craft);

                if (vec3DConstructor == null)
                    vec3DConstructor = vec3DClass.getConstructor(double.class, double.class, double.class);

                if (posMoveRotConstructor == null)
                    posMoveRotConstructor = posMoveRotClass.getConstructor(vec3DClass, vec3DClass, float.class, float.class);

                if (teleportFactoryMethod == null)
                    teleportFactoryMethod = teleportPacketClass.getMethod("teleport", int.class, posMoveRotClass, Set.class, boolean.class);

                Object position = vec3DConstructor.newInstance(
                        targetLoc.getX(),
                        targetLoc.getY(),
                        targetLoc.getZ());

                Object zeroVec = vec3DConstructor.newInstance(0.0, 0.0, 0.0);

                Object posMoveRot = posMoveRotConstructor.newInstance(
                        position,
                        zeroVec,
                        targetLoc.getYaw(),
                        targetLoc.getPitch());

                Object packet = teleportFactoryMethod.invoke(null, id, posMoveRot, new HashSet<>(), false);

                sendPlayerPacket(player, packet);
                return;
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_21_R2)) {

                Object craft = entity.getClass().getMethod("getBukkitEntity").invoke(entity);
                int id = (int) craft.getClass().getMethod("getEntityId").invoke(craft);

                if (vec3DConstructor == null)
                    vec3DConstructor = vec3DClass.getConstructor(double.class, double.class, double.class);

                if (posMoveRotConstructor == null)
                    posMoveRotConstructor = posMoveRotClass.getConstructor(vec3DClass, vec3DClass, float.class, float.class);

                if (teleportFactoryMethod == null)
                    teleportFactoryMethod = teleportPacketClass.getMethod("a", int.class, posMoveRotClass, Set.class, boolean.class);

                Object position = vec3DConstructor.newInstance(
                        targetLoc.getX(),
                        targetLoc.getY(),
                        targetLoc.getZ());

                Object zeroVec = vec3DConstructor.newInstance(0.0, 0.0, 0.0);

                Object posMoveRot = posMoveRotConstructor.newInstance(
                        position,
                        zeroVec,
                        targetLoc.getYaw(),
                        targetLoc.getPitch());

                Object packet = teleportFactoryMethod.invoke(null, id, posMoveRot, new HashSet<>(), false);

                sendPlayerPacket(player, packet);
                return;
            }

            Object centity = CEntity.cast(entity);

            if (teleportMethod == null) {
                if (Version.isCurrentEqualOrHigher(Version.v1_21_R1))
                    teleportMethod = CEntity.getMethod("a_", double.class, double.class, double.class);
                else if (Version.isCurrentEqualOrHigher(Version.v1_20_R4))
                    teleportMethod = CEntity.getMethod("p", double.class, double.class, double.class);
                else if (Version.isCurrentEqualOrHigher(Version.v1_18_R1))
                    teleportMethod = CEntity.getMethod("a", double.class, double.class, double.class);
                else
                    teleportMethod = CEntity.getMethod("setPosition", double.class, double.class, double.class);
            }

            teleportMethod.invoke(centity, targetLoc.getX(), targetLoc.getY(), targetLoc.getZ());

            if (player == null)
                return;

            if (teleportPacket == null)
                teleportPacket = PacketPlayOutEntityTeleport.getConstructor(CEntity);

            Object packet = teleportPacket.newInstance(centity);

            try {
                if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                    setValue(packet, "f", (byte) ((int) targetLoc.getYaw() * 256.0F / 360.0F));
                    setValue(packet, "g", (byte) ((int) targetLoc.getPitch() * 256.0F / 360.0F));
                } else {
                    setValue(packet, "e", (byte) ((int) targetLoc.getYaw() * 256.0F / 360.0F));
                    setValue(packet, "f", (byte) ((int) targetLoc.getPitch() * 256.0F / 360.0F));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            sendPlayerPacket(player, packet);
        } catch (Throwable e) {
            e.printStackTrace();
            teleportMethod = null;
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
        CMIScheduler.runTask(plugin, () -> {
            try {
                cPlayer.getClass().getMethod("playSound", Location.class, Sound.class, float.class, float.class).invoke(cPlayer, location, sound, volume, pitch);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
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

    @Deprecated // Use CMIEffectManager
    public void playEffect(Player player, Location location, CMIEffect ef) {
        CMIEffectManager.playEffect(player, location, ef);
    }

    private Constructor<?> keyConstructor = null;
    private Method minecraftKeyStaticA = null;
    private Method minecraftKeyStaticABase = null;

    private Object getKey(String key) {
        try {

            if (Version.isMojangMappings()) {
                return resourceKeyMethod.invoke(identifierMethod.invoke("minecraft", key));
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                if (minecraftKeyStaticA == null)
                    minecraftKeyStaticA = minecraftKeyClass.getMethod("a", String.class);
                return minecraftKeyStaticA.invoke(null, key);
            }

            String namespace = key.contains(":") ? key.split(":", 2)[0] : "minecraft";
            String path = key.contains(":") ? key.split(":", 2)[1] : key;

            return getKey(namespace, path);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getIdentifier(String base, String key) {
        if (!Version.isMojangMappings())
            return null;

        try {
            return identifierMethod.invoke(null, base, key);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }

    private Object getKey(String base, String key) {
        try {

            if (Version.isMojangMappings()) {
                return resourceKeyMethod.invoke(null, identifierMethod.invoke(null, base, key));
            }

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                if (minecraftKeyStaticABase == null) {
                    minecraftKeyStaticABase = minecraftKeyClass.getMethod("a", String.class, String.class);
                }
                return minecraftKeyStaticABase.invoke(null, base, key);
            }

            if (keyConstructor == null) {
                keyConstructor = minecraftKeyClass.getConstructor(String.class, String.class);
            }

            return keyConstructor.newInstance(base, key);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    private Field advancementRegistryBField = null;

    @SuppressWarnings("unchecked")
    private void removeAdvancementFromRegistry(Object ad) {
        try {
            if (advancementRegistryBField == null)
                advancementRegistryBField = advancementRegistry.getClass().getField("b");

            Map<Object, Object> advancements = (Map<Object, Object>) advancementRegistryBField.get(advancementRegistry);

            Method getId = ad.getClass().getMethod("getId");
            Object id = getId.invoke(ad);

            if (id != null) {
                Method getNamespace = id.getClass().getMethod("getNamespace");
                Method getKey = id.getClass().getMethod("getKey");
                String namespace = (String) getNamespace.invoke(id);
                String key = (String) getKey.invoke(id);
                advancements.remove(getKey(namespace, key));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void removeAdvancement(net.Zrips.CMILib.Advancements.CMIAdvancement ad) {

        if (Version.isCurrentLower(Version.v1_12_R1) || Version.isCurrentEqualOrHigher(Version.v1_20_R2))
            return;

        if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            removeAdvancementFromRegistry(ad);
        } else if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
            Bukkit.getUnsafe().removeAdvancement(ad.getId());
            try {
                Object server = MinecraftServerClass.getDeclaredMethod("getServer").invoke(MinecraftServer);
                Object AdvancementData = server.getClass().getMethod("getAdvancementData").invoke(server);
                Object REGISTRY = AdvancementData.getClass().getField("REGISTRY").get(AdvancementData);

                Map<Object, Object> advs = (Map<Object, Object>) REGISTRY.getClass().getField("advancements").get(REGISTRY);
                if (ad.getId() != null) {
                    advs.remove(CraftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class).invoke(CraftNamespacedKey, ad.getId()));
                }
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getUnsafe().removeAdvancement(ad.getId());
            try {
                Object REGISTRY = AdvancementDataWorld.getField("REGISTRY").get(AdvancementDataWorld);
                Map<Object, Object> advs = (Map<Object, Object>) REGISTRY.getClass().getField("advancements").get(REGISTRY);
                if (ad.getId() != null) {
                    advs.remove(CraftNamespacedKey.getMethod("toMinecraft", NamespacedKey.class).invoke(CraftNamespacedKey, ad.getId()));
                }
            } catch (Exception | Error e) {
                e.printStackTrace();
            }
        }
    }

    private Constructor<?> toastAdvancementDisplay = null;
    private Constructor<?> toastPacketPlayOutAdvancements = null;

    public void showToast(CMIAdvancement advancement, Player... players) {
        if (!Version.isCurrentEqualOrHigher(Version.v1_20_R2))
            return;

        CMIScheduler.runTaskAsynchronously(plugin, () -> {
            try {

                if (Version.isMojangMappings()) {

                    Object frame = advancementFrameTypeClass.getField("TASK").get(null);
                    if (advancement.getFrame().name().equals("CHALLENGE"))
                        frame = advancementFrameTypeClass.getField("CHALLENGE").get(null);
                    else if (advancement.getFrame().name().equals("GOAL"))
                        frame = advancementFrameTypeClass.getField("GOAL").get(null);

                    Object itemStack = CMINBT.asNMSCopy(advancement.getItem());
                    Object title = textToIChatBaseComponent(new RawMessage().addText(advancement.getTitle()).getRaw());
                    Object description = textToIChatBaseComponent(new RawMessage().addText(advancement.getDescription()).getRaw());

                    Object identifier = getIdentifier(advancement.getId().getNamespace(), advancement.getId().getKey());

                    if (toastAdvancementDisplay == null) {
                        toastAdvancementDisplay = advancementDisplayClass.getConstructor(
                                IStack,
                                IChatBaseComponent,
                                IChatBaseComponent,
                                java.util.Optional.class,
                                advancementFrameTypeClass,
                                boolean.class,
                                boolean.class,
                                boolean.class);
                    }

                    Object display = toastAdvancementDisplay.newInstance(
                            itemStack,
                            title,
                            description,
                            java.util.Optional.empty(),
                            frame,
                            true,
                            false,
                            false);

                    List<List<String>> adreq = Arrays.asList(Arrays.asList(CMIAdvancement.identificator));
                    Object requirements = advancementRequirementsClass.getConstructor(List.class).newInstance(adreq);

                    Object progress = advancementProgressClass.getConstructor().newInstance();
                    advancementProgressClass.getMethod("update", advancementRequirementsClass).invoke(progress, requirements);
                    advancementProgressClass.getMethod("grantProgress", String.class).invoke(progress, CMIAdvancement.identificator);

                    Map<Object, Object> progressMap = new HashMap<>();
                    progressMap.put(identifier, progress);

                    Set<Object> addedSet = new HashSet<>();
                    Object serializedAdv = SerializedAdvancement.getConstructor().newInstance();
                    SerializedAdvancement.getMethod("display", advancementDisplayClass).invoke(serializedAdv, display);
                    SerializedAdvancement.getMethod("requirements", advancementRequirementsClass).invoke(serializedAdv, requirements);
                    Object holder = SerializedAdvancement.getMethod("build", identifierClass).invoke(serializedAdv, identifier);
                    addedSet.add(holder);

                    Set<Object> removedSet = new HashSet<>();
                    removedSet.add(identifier);

                    try {
                        if (toastPacketPlayOutAdvancements == null)
                            toastPacketPlayOutAdvancements = packetPlayOutAdvancementsClass.getConstructor(boolean.class, Collection.class, Set.class, Map.class, boolean.class);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }

                    for (Player player : players) {
                        CMIPlayerConnection.sendPacket(player, toastPacketPlayOutAdvancements.newInstance(false, addedSet, new HashSet<>(), progressMap, true));
                        CMIPlayerConnection.sendPacket(player, toastPacketPlayOutAdvancements.newInstance(false, new HashSet<>(), removedSet, new HashMap<>(), true));
                    }

                } else if (Version.isCurrentEqualOrHigher(Version.v1_20_R3)) {

                    Object frame = advancementFrameTypeClass.getField("a").get(null);
                    if (advancement.getFrame().name().equals("CHALLENGE"))
                        frame = advancementFrameTypeClass.getField("b").get(null);
                    else if (advancement.getFrame().name().equals("GOAL"))
                        frame = advancementFrameTypeClass.getField("c").get(null);

                    Object itemStack = CMINBT.asNMSCopy(advancement.getItem());
                    Object title = textToIChatBaseComponent(new RawMessage().addText(advancement.getTitle()).getRaw());
                    Object description = textToIChatBaseComponent(new RawMessage().addText(advancement.getDescription()).getRaw());

                    Object minecraftKey = getKey(advancement.getId().getNamespace(), advancement.getId().getKey());

                    if (toastAdvancementDisplay == null) {
                        toastAdvancementDisplay = advancementDisplayClass.getConstructor(
                                IStack,
                                IChatBaseComponent,
                                IChatBaseComponent,
                                java.util.Optional.class,
                                advancementFrameTypeClass,
                                boolean.class,
                                boolean.class,
                                boolean.class);
                    }

                    Object display = toastAdvancementDisplay.newInstance(
                            itemStack,
                            title,
                            description,
                            Version.isCurrentEqualOrHigher(Version.v1_21_R4) ? java.util.Optional.empty() : java.util.Optional.of(getKey(advancement.getBackground().getUrl())),
                            frame,
                            true,
                            false,
                            false);

                    List<List<String>> adreq = Arrays.asList(Arrays.asList(CMIAdvancement.identificator));
                    Object requirements = advancementRequirementsClass.getConstructor(List.class).newInstance(adreq);

                    Object progress = advancementProgressClass.getConstructor().newInstance();
                    advancementProgressClass.getMethod("a", advancementRequirementsClass).invoke(progress, requirements);
                    advancementProgressClass.getMethod("a", String.class).invoke(progress, CMIAdvancement.identificator);

                    Map<Object, Object> progressMap = new HashMap<>();
                    progressMap.put(minecraftKey, progress);

                    Set<Object> addedSet = new HashSet<>();
                    Object serializedAdv = SerializedAdvancement.getConstructor().newInstance();
                    SerializedAdvancement.getMethod("a", advancementDisplayClass).invoke(serializedAdv, display);
                    SerializedAdvancement.getMethod("a", advancementRequirementsClass).invoke(serializedAdv, requirements);
                    Object holder = SerializedAdvancement.getMethod("b", minecraftKeyClass).invoke(serializedAdv, minecraftKey);
                    addedSet.add(holder);

                    Set<Object> removedSet = new HashSet<>();
                    removedSet.add(minecraftKey);

                    if (Version.isCurrentEqualOrHigher(Version.v1_21_R4)) {
                        try {
                            if (toastPacketPlayOutAdvancements == null)
                                toastPacketPlayOutAdvancements = packetPlayOutAdvancementsClass.getConstructor(boolean.class, Collection.class, Set.class, Map.class, boolean.class);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            return;
                        }
                        for (Player player : players) {
                            Object connection = CMILib.getInstance().getReflectionManager().getPlayerConnection(player);
                            sendPacket(connection, toastPacketPlayOutAdvancements.newInstance(false, addedSet, new HashSet<>(), progressMap, true));
                            sendPacket(connection, toastPacketPlayOutAdvancements.newInstance(false, new HashSet<>(), removedSet, new HashMap<>(), true));
                        }
                    } else {

                        try {
                            if (toastPacketPlayOutAdvancements == null)
                                toastPacketPlayOutAdvancements = packetPlayOutAdvancementsClass.getConstructor(boolean.class, Collection.class, Set.class, Map.class);
                        } catch (Throwable e) {
                            e.printStackTrace();
                            return;
                        }

                        for (Player player : players) {
                            Object connection = CMILib.getInstance().getReflectionManager().getPlayerConnection(player);
                            try {
                                sendPacket(connection, toastPacketPlayOutAdvancements.newInstance(false, addedSet, new HashSet<>(), progressMap));
                                sendPacket(connection, toastPacketPlayOutAdvancements.newInstance(false, new HashSet<>(), removedSet, new HashMap<>()));
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else {

                    Class<?> advClass = Class.forName("net.minecraft.advancements.Advancement");
                    Method aMethod = advClass.getMethod("a", com.google.gson.JsonObject.class, LootDeserializationContext);
                    Object adv = aMethod.invoke(null, advancement.getJSONObject(), null);

                    Class<?> progressClass = Class.forName("net.minecraft.advancements.AdvancementProgress");
                    Object progress = progressClass.getConstructor().newInstance();

                    Object requirements = adv.getClass().getMethod("g").invoke(adv);
                    Method init = progressClass.getMethod("a", requirements.getClass());
                    Method c = progressClass.getMethod("c", String.class);
                    Method b = progressClass.getMethod("b");

                    Object minecraftKey = getKey(advancement.getId().getNamespace(), advancement.getId().getKey());

                    Set<Object> removed = new HashSet<>();
                    removed.add(minecraftKey);

                    Set<Object> addedSet = new HashSet<>();
                    Map<Object, Object> progressMap = new HashMap<>();
                    init.invoke(progress, requirements);
                    c.invoke(progress, CMIAdvancement.identificator);
                    b.invoke(progress);
                    progressMap.put(minecraftKey, progress);

                    Class<?> holderClass = Class.forName("net.minecraft.advancements.AdvancementHolder");
                    Constructor<?> holderCtor = holderClass.getConstructor(minecraftKey.getClass(), adv.getClass());
                    Object advancementHolder = holderCtor.newInstance(minecraftKey, adv);
                    addedSet.add(advancementHolder);

                    try {
                        if (toastPacketPlayOutAdvancements == null)
                            toastPacketPlayOutAdvancements = packetPlayOutAdvancementsClass.getConstructor(boolean.class, Collection.class, Set.class, Map.class);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        return;
                    }

                    for (Player player : players) {
                        Object connection = CMILib.getInstance().getReflectionManager().getPlayerConnection(player);
                        try {
                            sendPacket(connection, toastPacketPlayOutAdvancements.newInstance(false, addedSet, new HashSet<>(), progressMap));
                            sendPacket(connection, toastPacketPlayOutAdvancements.newInstance(false, new HashSet<>(), removed, new HashMap<>()));
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private Method serverGetMethod = null;
    private final Map<String, Method> lootPredicateManagerMethods = new HashMap<>();

    private Object getLootPredicateManager() {
        try {

            if (serverGetMethod == null)
                serverGetMethod = Class.forName("net.minecraft.server.MinecraftServer").getMethod("getServer");
            Object server = serverGetMethod.invoke(null);

            String versionMethod = "getLootPredicateManager";
            if (Version.isCurrentEqualOrLower(Version.v1_18_R1))
                versionMethod = "aH";
            else if (Version.isCurrentEqualOrLower(Version.v1_18_R2))
                versionMethod = "aG";
            else if (Version.isCurrentEqualOrLower(Version.v1_19_R1))
                versionMethod = "aI";
            else if (Version.isCurrentEqualOrLower(Version.v1_19_R2))
                versionMethod = "aH";
            else if (Version.isCurrentEqualOrLower(Version.v1_19_R3))
                versionMethod = "aI";

            Method lootMethod = lootPredicateManagerMethods.get(versionMethod);
            if (lootMethod == null) {
                lootMethod = server.getClass().getMethod(versionMethod);
                lootPredicateManagerMethods.put(versionMethod, lootMethod);
            }

            return lootMethod.invoke(server);

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadAdvancement(net.Zrips.CMILib.Advancements.CMIAdvancement ad, String advancement) {
        if (Version.isCurrentLower(Version.v1_12_R1) || Version.isCurrentEqualOrHigher(Version.v1_20_R2))
            return;

        if (Bukkit.getAdvancement(ad.getId()) != null)
            return;

        NamespacedKey key = ad.getId();

        try {
            Class<?> craftKey = getBukkitClass("util.CraftNamespacedKey");
            Method toMinecraft = craftKey.getMethod("toMinecraft", NamespacedKey.class);
            Object mcKey = toMinecraft.invoke(null, key);

            Class<?> jsonElementClass = Class.forName("com.google.gson.JsonElement");

            if (Version.isCurrentEqualOrHigher(Version.v1_20_R1)) {
                Object deser = AdvancementDataWorld.getField("b").get(null);

                Object jsonElement = deser.getClass().getMethod("fromJson", String.class, Class.class).invoke(deser, advancement, jsonElementClass);
                Object jsonObject = ChatDeserializer.getMethod("m", jsonElementClass, String.class).invoke(null, jsonElement, "advancement");

                Object predicateMgr = getLootPredicateManager();
                Constructor<?> ctor = LootDeserializationContext.getConstructor(mcKey.getClass(), predicateMgr.getClass());
                Object ldc = ctor.newInstance(mcKey, predicateMgr);

                Method deserialize = SerializedAdvancement.getMethod("a", jsonObject.getClass(), LootDeserializationContext);
                Object nms = deserialize.invoke(null, jsonObject, ldc);

                if (nms != null) {
                    Object ax = MinecraftServer.getClass().getMethod("az").invoke(MinecraftServer);
                    Object c = ax.getClass().getField("c").get(ax);
                    c.getClass().getMethod("a", Map.class).invoke(c, Collections.singletonMap(mcKey, nms));
                }

            } else if (Version.isCurrentEqualOrHigher(Version.v1_18_R1)) {
                Object deser = AdvancementDataWorld.getField("b").get(null);
                Object jsonElement = deser.getClass().getMethod("fromJson", String.class, Class.class).invoke(deser, advancement, jsonElementClass);
                Object jsonObject = ChatDeserializer.getMethod("m", jsonElementClass, String.class).invoke(null, jsonElement, "advancement");

                Object predicateMgr = getLootPredicateManager();
                if (predicateMgr == null)
                    return;

                Constructor<?> ctor = LootDeserializationContext.getConstructor(mcKey.getClass(), predicateMgr.getClass());
                Object ldc = ctor.newInstance(mcKey, predicateMgr);

                Method deserialize = SerializedAdvancement.getMethod("a", jsonObject.getClass(), LootDeserializationContext);
                Object nms = deserialize.invoke(null, jsonObject, ldc);

                if (nms != null) {
                    Object access = null;
                    if (Version.isCurrentEqualOrHigher(Version.v1_19_R3)) {
                        access = MinecraftServer.getClass().getMethod("az").invoke(MinecraftServer);
                    } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R2)) {
                        access = MinecraftServer.getClass().getMethod("ay").invoke(MinecraftServer);
                    } else if (Version.isCurrentEqualOrHigher(Version.v1_19_R1)) {
                        access = MinecraftServer.getClass().getMethod("az").invoke(MinecraftServer);
                    } else {
                        access = MinecraftServer.getClass().getMethod("ax").invoke(MinecraftServer);
                    }

                    Object c = access.getClass().getField("c").get(access);
                    c.getClass().getMethod("a", Map.class).invoke(c, Collections.singletonMap(mcKey, nms));
                }

            } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                Object deser = AdvancementDataWorld.getField("b").get(null);

                Object jsonElement = deser.getClass().getMethod("fromJson", String.class, Class.class).invoke(deser, advancement, jsonElementClass);
                Object jsonObject = ChatDeserializer.getMethod("m", jsonElementClass, String.class).invoke(null, jsonElement, "advancement");

                Object predicateMgr = getLootPredicateManager();
                Constructor<?> ctor = LootDeserializationContext.getConstructor(mcKey.getClass(), predicateMgr.getClass());
                Object ldc = ctor.newInstance(mcKey, predicateMgr);

                Method deserialize = SerializedAdvancement.getMethod("a", jsonObject.getClass(), LootDeserializationContext);
                Object nms = deserialize.invoke(null, jsonObject, ldc);

                advancementRegistry.getClass().getMethod("a", Map.class).invoke(advancementRegistry, Collections.singletonMap(mcKey, nms));

            } else if (Version.isCurrentEqualOrHigher(Version.v1_16_R1)) {
                Object deser = AdvancementDataWorld.getField("DESERIALIZER").get(null);

                Object jsonElement = deser.getClass().getMethod("fromJson", String.class, Class.class).invoke(deser, advancement, jsonElementClass);
                Object jsonObject = ChatDeserializer.getMethod("m", jsonElementClass, String.class).invoke(null, jsonElement, "advancement");

                Object server = MinecraftServerClass.getDeclaredMethod("getServer").invoke(null);
                Object predicateMgr = getLootPredicateManager();

                Method deserialize = SerializedAdvancement.getMethod("a", jsonObject.getClass(), LootDeserializationContext);
                Object ldc = LootDeserializationContext.getConstructor(mcKey.getClass(), predicateMgr.getClass()).newInstance(mcKey, predicateMgr);
                Object nms = deserialize.invoke(null, jsonObject, ldc);

                if (nms != null) {
                    Object advData = server.getClass().getMethod("getAdvancementData").invoke(server);
                    Object registry = advData.getClass().getField("REGISTRY").get(advData);
                    registry.getClass().getMethod("a", Map.class).invoke(registry, Collections.singletonMap(mcKey, nms));
                }

            } else {
                Object deser = AdvancementDataWorld.getField("DESERIALIZER").get(null);
                Object nms = deser.getClass().getMethod("fromJson", String.class, Class.class).invoke(deser, advancement, SerializedAdvancement);
                Object registry = AdvancementDataWorld.getField("REGISTRY").get(null);
                registry.getClass().getMethod("a", Map.class).invoke(registry, Collections.singletonMap(mcKey, nms));
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public ItemStack addAttributes(List<Attribute> ls, ItemStack item) {
        if (ls.isEmpty())
            return item;

        if (Version.isCurrentEqualOrHigher(Version.v1_21_R5)) {

            return item;
        }

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
}
