package net.Zrips.CMILib.Effects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Effect.Type;
import org.bukkit.Material;

import net.Zrips.CMILib.Colors.CMIChatColor;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Version.Version;

public class CMIEffectManager {

    public enum CMIParticleType {
        SOUND, VISUAL, PARTICLE, NONE;
    }

    public enum CMIParticleDataType {
        Void, DustOptions, ItemStack, BlockData, MaterialData;
    }

    public enum CMIParticle {
        CLICK2("null", 0, CMIParticleType.SOUND, null),
        CLICK1("null", 1, CMIParticleType.SOUND, null),
        BOW_FIRE("null", 2, CMIParticleType.SOUND, null),
        DOOR_TOGGLE("null", 3, CMIParticleType.SOUND, null),
        IRON_DOOR_TOGGLE("null", 4, CMIParticleType.SOUND, null),
        TRAPDOOR_TOGGLE("null", 5, CMIParticleType.SOUND, null),
        IRON_TRAPDOOR_TOGGLE("null", 6, CMIParticleType.SOUND, null),
        FENCE_GATE_TOGGLE("null", 7, CMIParticleType.SOUND, null),
        DOOR_CLOSE("null", 8, CMIParticleType.SOUND, null),
        IRON_DOOR_CLOSE("null", 9, CMIParticleType.SOUND, null),
        TRAPDOOR_CLOSE("null", 10, CMIParticleType.SOUND, null),
        IRON_TRAPDOOR_CLOSE("null", 11, CMIParticleType.SOUND, null),
        FENCE_GATE_CLOSE("null", 12, CMIParticleType.SOUND, null),
        EXTINGUISH("null", 13, CMIParticleType.SOUND, null),
        RECORD_PLAY("null", 14, CMIParticleType.SOUND, null),
        GHAST_SHRIEK("null", 15, CMIParticleType.SOUND, null),
        GHAST_SHOOT("null", 16, CMIParticleType.SOUND, null),
        BLAZE_SHOOT("null", 17, CMIParticleType.SOUND, null),
        ZOMBIE_CHEW_WOODEN_DOOR("null", 18, CMIParticleType.SOUND, null),
        ZOMBIE_CHEW_IRON_DOOR("null", 19, CMIParticleType.SOUND, null),
        ZOMBIE_DESTROY_DOOR("null", 20, CMIParticleType.SOUND, null),
        SMOKE("null", 21, CMIParticleType.VISUAL, null),
        STEP_SOUND("null", 22, CMIParticleType.SOUND, null),
        POTION_BREAK("null", 23, CMIParticleType.VISUAL, null),
        ENDER_SIGNAL("null", 24, CMIParticleType.VISUAL, null),
        MOBSPAWNER_FLAMES("null", 25, CMIParticleType.VISUAL, null),
        BREWING_STAND_BREW("null", 26, CMIParticleType.SOUND, null),
        CHORUS_FLOWER_GROW("null", 27, CMIParticleType.SOUND, null),
        CHORUS_FLOWER_DEATH("null", 28, CMIParticleType.SOUND, null),
        PORTAL_TRAVEL("null", 29, CMIParticleType.SOUND, null),
        ENDEREYE_LAUNCH("null", 30, CMIParticleType.SOUND, null),
        FIREWORK_SHOOT("null", 31, CMIParticleType.SOUND, null),
        VILLAGER_PLANT_GROW("null", 32, CMIParticleType.VISUAL, null),
        DRAGON_BREATH("null", 33, CMIParticleType.VISUAL, null),
        ANVIL_BREAK("null", 34, CMIParticleType.SOUND, null),
        ANVIL_USE("null", 35, CMIParticleType.SOUND, null),
        ANVIL_LAND("null", 36, CMIParticleType.SOUND, null),
        ENDERDRAGON_SHOOT("null", 37, CMIParticleType.SOUND, null),
        WITHER_BREAK_BLOCK("null", 38, CMIParticleType.SOUND, null),
        WITHER_SHOOT("null", 39, CMIParticleType.SOUND, null),
        ZOMBIE_INFECT("null", 40, CMIParticleType.SOUND, null),
        ZOMBIE_CONVERTED_VILLAGER("null", 41, CMIParticleType.SOUND, null),
        BAT_TAKEOFF("null", 42, CMIParticleType.SOUND, null),
        END_GATEWAY_SPAWN("null", 43, CMIParticleType.VISUAL, null),
        ENDERDRAGON_GROWL("null", 44, CMIParticleType.SOUND, null),
        FIREWORKS_SPARK("FireworksSpark", 45, CMIParticleType.PARTICLE, CMIMaterial.FIRE_CHARGE.getMaterial()),
        CRIT("Crit", 46, CMIParticleType.PARTICLE, Material.IRON_SWORD),
        MAGIC_CRIT("CritMagic", 47, CMIParticleType.PARTICLE, Material.POTION),
        POTION_SWIRL("MobSpell", "SPELL_MOB", 48, CMIParticleType.PARTICLE, Material.BLAZE_ROD),
        POTION_SWIRL_TRANSPARENT("MobSpellAmbient", "SPELL_MOB_AMBIENT", 49, CMIParticleType.PARTICLE, Material.BLAZE_POWDER),
        SPELL("Spell", 50, CMIParticleType.PARTICLE, Material.MILK_BUCKET),
        INSTANT_SPELL("InstantSpell", "SPELL_INSTANT", 51, CMIParticleType.PARTICLE, Material.GLASS_BOTTLE),
        WITCH_MAGIC("WitchMagic", "SPELL_WITCH", 52, CMIParticleType.PARTICLE, Material.SPIDER_EYE),
        NOTE("Note", 53, CMIParticleType.PARTICLE, Material.NOTE_BLOCK),
        PORTAL("Portal", 54, CMIParticleType.PARTICLE, Material.OBSIDIAN),
        FLYING_GLYPH("EnchantmentTable", 55, CMIParticleType.PARTICLE, CMIMaterial.ENCHANTING_TABLE.getMaterial()),
        FLAME("Flame", 56, CMIParticleType.PARTICLE, CMIMaterial.FIRE_CHARGE.getMaterial()),
        LAVA_POP("Lava", 57, CMIParticleType.PARTICLE, Material.FLINT_AND_STEEL),
        FOOTSTEP("FootStep", 58, CMIParticleType.PARTICLE, Material.IRON_BOOTS),
        SPLASH("Splash", "water splash", 59, CMIParticleType.PARTICLE, Material.STICK),
        PARTICLE_SMOKE("Smoke", "SMOKE_NORMAL", 60, CMIParticleType.PARTICLE, Material.ANVIL),
        EXPLOSION_HUGE("HugeExplosion", 61, CMIParticleType.PARTICLE, Material.FURNACE),
        EXPLOSION_LARGE("LargeExplode", 62, CMIParticleType.PARTICLE, Material.FURNACE),
        EXPLOSION("Explode", "EXPLOSION_NORMAL", 63, CMIParticleType.PARTICLE, Material.TNT),
        VOID_FOG("DepthSuspend", "SUSPENDED_DEPTH", 64, CMIParticleType.PARTICLE, CMIMaterial.SALMON.getMaterial()),
        SMALL_SMOKE("TownAura", 65, CMIParticleType.PARTICLE, CMIMaterial.MYCELIUM.getMaterial()),
        CLOUD("Cloud", 66, CMIParticleType.PARTICLE, CMIMaterial.COBWEB.getMaterial()),
        COLOURED_DUST("Reddust", "dust", 67, CMIParticleType.PARTICLE, Material.REDSTONE, CMIParticleDataType.DustOptions),
        SNOWBALL_BREAK("SnowBallPoof", "SNOWBALL", 68, CMIParticleType.PARTICLE, CMIMaterial.SNOWBALL.getMaterial()),
        WATERDRIP("DripWater", "WATER_DROP", 69, CMIParticleType.PARTICLE, Material.WATER_BUCKET),
        LAVADRIP("DripLava", 70, CMIParticleType.PARTICLE, Material.LAVA_BUCKET),
        SNOW_SHOVEL("SnowShovel", 71, CMIParticleType.PARTICLE, CMIMaterial.DIAMOND_SHOVEL.getMaterial()),
        SLIME("Slime", 72, CMIParticleType.PARTICLE, Material.SLIME_BALL),
        HEART("Heart", 73, CMIParticleType.PARTICLE, CMIMaterial.ROSE_RED.getMaterial()),
        VILLAGER_THUNDERCLOUD("AngryVillager", "VILLAGER_ANGRY", 74, CMIParticleType.PARTICLE, Material.EMERALD),
        HAPPY_VILLAGER("VillagerHappy", 75, CMIParticleType.PARTICLE, Material.BOOK),
        LARGE_SMOKE("LargeSmoke", "SMOKE_LARGE", 76, CMIParticleType.PARTICLE, Material.FURNACE),
        ITEM_BREAK("Iconcrack", 77, CMIParticleType.NONE, Material.DIAMOND_BOOTS),

        // 1.13
        WATER_BUBBLE("WaterBubble", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        WATER_WAKE("WaterWake", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SUSPENDED("Suspended", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        BARRIER("Barrier", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        MOB_APPEARANCE("MobAppearance", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        END_ROD("EndRod", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DAMAGE_INDICATOR("DamageIndicator", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SWEEP_ATTACK("SweepAttack", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        TOTEM("Totem", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SPIT("Spit", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SQUID_INK("SquidInk", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        BUBBLE_POP("BubblePop", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        CURRENT_DOWN("CurrentDown", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        BUBBLE_COLUMN_UP("BubbleColumnUp", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        NAUTILUS("Nautilus", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DOLPHIN("Dolphin", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),

//	Requires extra data when displaying
//	ITEM_CRACK("ItemCrack", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
//	BLOCK_DUST("block_dust", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
//	FALLING_DUST("falling_dust", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),

        //1.16
        WATER_SPLASH("WaterSplash", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        CAMPFIRE_SIGNAL_SMOKE("CampfireSignalSmoke", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        CAMPFIRE_COSY_SMOKE("CampfireCosySmoke", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SNEEZE("sneeze", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        COMPOSTER("composter", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FLASH("flash", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_LAVA("falling_lava", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        LANDING_LAVA("landing_lava", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_WATER("falling_water", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DRIPPING_HONEY("dripping_honey", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_HONEY("falling_honey", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        LANDING_HONEY("landing_honey", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_NECTAR("falling_nectar", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SOUL_FIRE_FLAME("soul_fire_flame", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ASH("ash", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        CRIMSON_SPORE("crimson_spore", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        WARPED_SPORE("warped_spore", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SOUL("soul", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DRIPPING_OBSIDIAN_TEAR("dripping_obsidian_tear", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_OBSIDIAN_TEAR("falling_obsidian_tear", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        LANDING_OBSIDIAN_TEAR("landing_obsidian_tear", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        REVERSE_PORTAL("reverse_portal", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        WHITE_ASH("white_ash", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),

        // 1.17
        LIGHT("light", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
//	Requires extra data when displaying
//	DUST_COLOR_TRANSITION("dust_color_transition", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
//	VIBRATION("vibration", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_SPORE_BLOSSOM("falling_spore_blossom", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SPORE_BLOSSOM_AIR("spore_blossom_air", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SMALL_FLAME("small_flame", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SNOWFLAKE("snowflake", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DRIPPING_DRIPSTONE_LAVA("dripping_dripstone_lava", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_DRIPSTONE_LAVA("falling_dripstone_lava", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DRIPPING_DRIPSTONE_WATER("dripping_dripstone_water", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_DRIPSTONE_WATER("falling_dripstone_water", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        GLOW_SQUID_INK("glow_squid_ink", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        GLOW("glow", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        WAX_ON("wax_on", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        WAX_OFF("wax_off", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ELECTRIC_SPARK("electric_spark", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SCRAPE("scrape", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),

        // 1.18
        BLOCK_MARKER("Block Marker", -1, CMIParticleType.PARTICLE, CMIMaterial.BARRIER.getMaterial(), CMIParticleDataType.BlockData),

        // 1.19
        SONIC_BOOM("sonic_boom", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SCULK_SOUL("sculk_soul", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
//	SCULK_CHARGE("sculk_charge", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SCULK_CHARGE_POP("sculk_charge_pop", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
//	SHRIEK("shriek", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),        
        CHERRY_LEAVES("cherry_leaves", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),

        // 1.20.5
        POOF("poof", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        EXPLOSION_EMITTER("explosion_emitter", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FIREWORK("firework", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        BUBBLE("bubble", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FISHING("fishing", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        UNDERWATER("underwater", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ENCHANTED_HIT("enchanted_hit", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        EFFECT("effect", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        INSTANT_EFFECT("instant_effect", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ENTITY_EFFECT("entity_effect", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        WITCH("witch", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DRIPPING_WATER("dripping_water", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DRIPPING_LAVA("dripping_lava", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        MYCELIUM("mycelium", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ENCHANT("enchant", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DUST("dust", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ITEM_SNOWBALL("item_snowball", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ITEM_SLIME("item_slime", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ITEM("item", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        BLOCK("block", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        RAIN("rain", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ELDER_GUARDIAN("elder_guardian", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        FALLING_DUST("falling_dust", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        TOTEM_OF_UNDYING("totem_of_undying", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DUST_COLOR_TRANSITION("dust_color_transition", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        VIBRATION("vibration", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SCULK_CHARGE("sculk_charge", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SHRIEK("shriek", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        EGG_CRACK("egg_crack", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DUST_PLUME("dust_plume", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        WHITE_SMOKE("white_smoke", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        GUST("gust", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        SMALL_GUST("small_gust", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        GUST_EMITTER_LARGE("gust_emitter_large", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        GUST_EMITTER_SMALL("gust_emitter_small", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        TRIAL_SPAWNER_DETECTION("trial_spawner_detection", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        TRIAL_SPAWNER_DETECTION_OMINOUS("trial_spawner_detection_ominous", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        VAULT_CONNECTION("vault_connection", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        INFESTED("infested", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        ITEM_COBWEB("item_cobweb", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        DUST_PILLAR("dust_pillar", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        OMINOUS_SPAWNING("ominous_spawning", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        RAID_OMEN("raid_omen", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),
        TRIAL_OMEN("trial_omen", -1, CMIParticleType.PARTICLE, Material.STONE, CMIParticleDataType.Void),

        ;

        private String name;
        private String secondaryName = "";
        private int id;
        private CMIParticleType type;
        private Material icon;
        private Object particle;
        private Effect effect;
        private Object EnumParticle;
        private int[] extra;
        private CMIParticleDataType dataType = CMIParticleDataType.Void;

        CMIParticle(String name, int id, CMIParticleType type) {
            this(name, null, id, type, null);
        }

        CMIParticle(String name, int id, CMIParticleType type, Material icon) {
            this(name, null, id, type, icon);
        }

        CMIParticle(String name, String secondaryName, int id, CMIParticleType type, Material icon) {
            this(name, secondaryName, id, type, icon, CMIParticleDataType.Void);
        }

        CMIParticle(String name, int id, CMIParticleType type, Material icon, CMIParticleDataType dataType) {
            this(name, null, id, type, icon, dataType);
        }

        CMIParticle(String name, String secondaryName, int id, CMIParticleType type, Material icon, CMIParticleDataType dataType) {
            this.name = name;
            this.secondaryName = secondaryName;
            this.id = id;
            this.type = type;
            this.icon = icon;
            this.dataType = dataType;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public CMIParticleType getType() {
            return type;
        }

        public boolean isParticle() {
            return type == CMIParticleType.PARTICLE;
        }

        public boolean isColored() {
            return this.equals(COLOURED_DUST);
        }

        public static boolean isParticle(Effect effect) {
            if (effect == null)
                return false;
            CMIParticle cmiEffect = getCMIParticle(effect.toString());
            if (cmiEffect == null)
                return false;
            return cmiEffect.isParticle();
        }

        public static Material getSafeIcon(Effect effect) {
            CMIParticle cmiEffect = getCMIParticle(effect.toString());
            if (cmiEffect == null)
                return Material.STONE;
            return cmiEffect.getIcon() == null ? Material.STONE : cmiEffect.getIcon();
        }

        public Material getSafeIcon() {
            return getIcon() == null ? Material.STONE : getIcon();
        }

        public static CMIParticle getCMIParticle(String name) {
            CMIParticle cmiEffect = null;
            if (name == null)
                return null;
            name = name.replace("_", "").toLowerCase();
            for (CMIParticle one : CMIParticle.values()) {
                if (one.getName() != null && one.getName().replace("_", "").equalsIgnoreCase(name)) {
                    cmiEffect = one;
                    break;
                }

                if (!one.getSecondaryName().isEmpty() && one.getSecondaryName().replace("_", "").equalsIgnoreCase(name)) {
                    cmiEffect = one;
                    break;
                }
                if (one.name().replace("_", "").equalsIgnoreCase(name)) {
                    cmiEffect = one;
                    break;
                }
            }
            if (cmiEffect != null && Version.isCurrentEqualOrHigher(Version.v1_9_R1) && cmiEffect.getParticle() == null)
                return null;
            if (Version.isCurrentLower(Version.v1_13_R1) && cmiEffect != null && cmiEffect.getEffect() == null)
                return null;
            return cmiEffect;
        }

        public static CMIEffect getCMIEffect(String name) {
            CMIEffect cmiEffect = null;
            if (name == null)
                return null;
            name = name.replace("_", "").toLowerCase();
            CMIMaterial mat = null;
            Color color = null;
            if (name.contains(":")) {
                String sub = name.split(":", 2)[1];

                for (String one : sub.split(":")) {
                    if (color == null) {
                        if (one.contains(",")) {
                            String[] split = one.split(",");
                            try {
                                color = Color.fromRGB(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
                            } catch (Throwable e) {

                            }
                        }
                        if (color == null) {
                            CMIChatColor c = CMIChatColor.getColor(one);
                            if (c != null) {
                                color = c.getRGBColor();
                            }
                        }
                        if (color == null) {
                            CMIChatColor cmicolor = CMIChatColor.getColor(one);
                            if (cmicolor != null) {
                                color = Color.fromRGB(cmicolor.getRed(), cmicolor.getGreen(), cmicolor.getBlue());
                            }
                        }
                        if (color != null)
                            continue;
                    }
                    mat = CMIMaterial.get(one);
                }

                name = name.split(":", 2)[0];
            }

            for (CMIParticle one : CMIParticle.values()) {
                if (one.getName() != null && one.getName().replace("_", "").equalsIgnoreCase(name)) {
                    cmiEffect = new CMIEffect(one);
                    break;
                }

                if (!one.getSecondaryName().isEmpty() && one.getSecondaryName().replace("_", "").equalsIgnoreCase(name)) {
                    cmiEffect = new CMIEffect(one);
                    break;
                }
                if (one.name().replace("_", "").equalsIgnoreCase(name)) {
                    cmiEffect = new CMIEffect(one);
                    break;
                }
            }
            if (cmiEffect != null && Version.isCurrentEqualOrHigher(Version.v1_9_R1) && cmiEffect.getParticle() == null)
                return null;
            if (Version.isCurrentLower(Version.v1_13_R1) && cmiEffect != null && cmiEffect.getParticle().getEffect() == null)
                return null;

            if (cmiEffect != null) {
                if (color != null)
                    cmiEffect.setColor(color);
                if (mat != null && mat.isBlock())
                    cmiEffect.setMaterial(mat);
            }

            return cmiEffect;
        }

//	public static Effect getEffect(String name) {
//	    CMIParticle cmiEffect = getCMIParticle(name);
////	    Bukkit.getConsoleSender().sendMessage("1 "+name);
////	    Bukkit.getConsoleSender().sendMessage("2 "+cmiEffect);
//
//	    if (cmiEffect != null) {
//		if (!cmiEffect.getType().equals(CMIParticleType.PARTICLE))
//		    return null;
//		for (Effect one : Effect.values()) {
//		    if (one.toString().equalsIgnoreCase(cmiEffect.name()))
//			return one;
//		    if (one.toString().equalsIgnoreCase(cmiEffect.getName()))
//			return one;
//		}
//	    } else {
//		for (Effect one : Effect.values()) {
//		    if (one.toString().replace("_", "").equalsIgnoreCase(name)) {
//			try {
//			    if (one.getType() != Type.VISUAL)
//				return null;
//			} catch (Exception | NoSuchMethodError e) {
//			    return null;
//			}
//			return one;
//		    }
//		}
//	    }
//	    return null;
//	}

        public Effect getEffect() {
            if (effect != null)
                return effect;
            if (!isParticle())
                return null;
            for (Effect one : Effect.values()) {
                if (one.toString().replace("_", "").equalsIgnoreCase(name().replace("_", ""))) {
                    effect = one;
                    return one;
                }
                if (one.toString().replace("_", "").equalsIgnoreCase(getName())) {
                    effect = one;
                    return one;
                }
            }

            for (Effect one : Effect.values()) {
                if (one.toString().replace("_", "").equalsIgnoreCase(name.replace("_", ""))) {
                    try {
                        if (one.getType() != Type.VISUAL)
                            return null;
                    } catch (Exception | NoSuchMethodError e) {
                        return null;
                    }
                    effect = one;
                    return one;
                }
            }
            return null;
        }

        public Material getIcon() {
            return icon == null ? Material.STONE : icon;
        }

        public static List<CMIParticle> getParticleList() {
            List<CMIParticle> ls = new ArrayList<CMIParticle>();
            for (CMIParticle one : CMIParticle.values()) {
                if (!one.isParticle())
                    continue;
                if (Version.isCurrentEqualOrHigher(Version.v1_9_R1) && one.getParticle() == null)
                    continue;
                if (Version.isCurrentLower(Version.v1_13_R1) && one.getEffect() == null)
                    continue;
                ls.add(one);
            }
            return ls;
        }

        public CMIParticle getNextPartcileEffect() {

            List<CMIParticle> ls = getParticleList();
            for (int i = 0; i < ls.size(); i++) {
                CMIParticle next = ls.get(i);
                if (next == null)
                    continue;

                if (!next.isParticle())
                    continue;
                if (Version.isCurrentEqualOrHigher(Version.v1_9_R1) && next.getParticle() == null)
                    continue;

                if (next.equals(this)) {
                    if (i == ls.size() - 1)
                        return ls.get(0);
                    return ls.get(i + 1);
                }
            }
            return this;
        }

        public CMIParticle getPrevParticleEffect() {
            List<CMIParticle> ls = getParticleList();
            for (int i = 0; i < ls.size(); i++) {
                CMIParticle next = ls.get(i);

                if (next == null)
                    continue;

                if (Version.isCurrentEqualOrHigher(Version.v1_9_R1) && next.getParticle() == null)
                    continue;

                if (!next.isParticle())
                    continue;
                if (next.equals(this)) {
                    if (i == 0)
                        return ls.get(ls.size() - 1);
                    return ls.get(i - 1);
                }
            }
            return this;
        }

        public String getSecondaryName() {
            return secondaryName == null ? "" : secondaryName;
        }

        public void setSecondaryName(String secondaryName) {
            this.secondaryName = secondaryName;
        }

        public org.bukkit.Particle getParticle() {
            if (Version.isCurrentEqualOrLower(Version.v1_8_R3))
                return null;
            if (particle == null) {
                String n = this.toString().replace("_", "").toLowerCase();
                for (org.bukkit.Particle one : org.bukkit.Particle.values()) {
                    String name = one.toString().toLowerCase().replace("_", "");
                    if (name.equalsIgnoreCase(n)) {
                        particle = one;
                        break;
                    }
                }
            }
            if (particle == null) {
                String n = name().replace("_", "").toLowerCase();
                for (org.bukkit.Particle one : org.bukkit.Particle.values()) {
                    String name = one.toString().toLowerCase().replace("_", "");
                    if (name.equalsIgnoreCase(n)) {
                        particle = one;
                        break;
                    }
                }
            }
            if (particle == null) {
                String n = getName().replace("_", "").toLowerCase();
                for (org.bukkit.Particle one : org.bukkit.Particle.values()) {
                    String name = one.toString().toLowerCase().replace("_", "");
                    if (name.equalsIgnoreCase(n)) {
                        particle = one;
                        break;
                    }
                }
            }
            if (particle == null) {
                String n = getSecondaryName().replace("_", "").toLowerCase();
                if (!n.isEmpty()) {
                    for (org.bukkit.Particle one : org.bukkit.Particle.values()) {
                        String name = one.toString().toLowerCase().replace("_", "");
                        if (name.equalsIgnoreCase(n)) {
                            particle = one;
                            break;
                        }
                    }

                    if (particle == null)
                        for (org.bukkit.Particle one : org.bukkit.Particle.values()) {
                            String name = one.toString().toLowerCase().replace("_", "");
                            if (name.contains(n)) {
                                particle = one;
                                break;
                            }
                        }
                }
            }
            return particle == null ? null : (org.bukkit.Particle) particle;
        }

        public Object getEnumParticle() {
            return EnumParticle;
        }

        public void setEnumParticle(Object enumParticle) {
            EnumParticle = enumParticle;
        }

        public int[] getExtra() {
            return extra;
        }

        public void setExtra(int[] extra) {
            this.extra = extra;
        }

        public CMIParticleDataType getDataType() {
            return dataType;
        }
    }

}
