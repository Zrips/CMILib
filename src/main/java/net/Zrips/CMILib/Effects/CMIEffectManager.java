package net.Zrips.CMILib.Effects;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle.Trail;
import org.bukkit.entity.Player;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Reflections;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Logs.CMIDebug;
import net.Zrips.CMILib.Version.Version;

public class CMIEffectManager {

    public enum CMIParticleType {
        SOUND, VISUAL, PARTICLE, NONE;
    }

    public enum CMIParticleDataType {
        Void,
        DustOptions(CMIEffectDataValueTypes.ColorFrom, CMIEffectDataValueTypes.Size),
        ItemStack(CMIEffectDataValueTypes.Material),
        BlockData(CMIEffectDataValueTypes.Material),
        MaterialData(CMIEffectDataValueTypes.Material),
        EntityData,
        Color(CMIEffectDataValueTypes.ColorFrom),
        DustTransition(CMIEffectDataValueTypes.ColorFrom, CMIEffectDataValueTypes.ColorTo, CMIEffectDataValueTypes.Size),
        Vibration(CMIEffectDataValueTypes.Duration, CMIEffectDataValueTypes.Location),
        Float(CMIEffectDataValueTypes.Speed),
        Int(CMIEffectDataValueTypes.Speed),
        Trail(CMIEffectDataValueTypes.ColorFrom, CMIEffectDataValueTypes.Duration, CMIEffectDataValueTypes.Location);

        private Set<CMIEffectDataValueTypes> dataTypes = new HashSet<>();

        private CMIParticleDataType(CMIEffectDataValueTypes... cmiEffectDataValueTypes) {
            this.dataTypes.addAll(Arrays.asList(cmiEffectDataValueTypes));
        }

        public Set<CMIEffectDataValueTypes> getDataTypes() {
            return dataTypes;
        }

        public boolean contains(CMIEffectDataValueTypes type) {
            return dataTypes.contains(type);
        }
    }

    public enum CMIParticle {
        CLICK2(CMIParticleType.SOUND),
        CLICK1(CMIParticleType.SOUND),
        BOW_FIRE(CMIParticleType.SOUND),
        DOOR_TOGGLE(CMIParticleType.SOUND),
        IRON_DOOR_TOGGLE(CMIParticleType.SOUND),
        TRAPDOOR_TOGGLE(CMIParticleType.SOUND),
        IRON_TRAPDOOR_TOGGLE(CMIParticleType.SOUND),
        FENCE_GATE_TOGGLE(CMIParticleType.SOUND),
        DOOR_CLOSE(CMIParticleType.SOUND),
        IRON_DOOR_CLOSE(CMIParticleType.SOUND),
        TRAPDOOR_CLOSE(CMIParticleType.SOUND),
        IRON_TRAPDOOR_CLOSE(CMIParticleType.SOUND),
        FENCE_GATE_CLOSE(CMIParticleType.SOUND),
        EXTINGUISH(CMIParticleType.SOUND),
        RECORD_PLAY(CMIParticleType.SOUND),
        GHAST_SHRIEK(CMIParticleType.SOUND),
        GHAST_SHOOT(CMIParticleType.SOUND),
        BLAZE_SHOOT(CMIParticleType.SOUND),
        ZOMBIE_CHEW_WOODEN_DOOR(CMIParticleType.SOUND),
        ZOMBIE_CHEW_IRON_DOOR(CMIParticleType.SOUND),
        ZOMBIE_DESTROY_DOOR(CMIParticleType.SOUND),
        SMOKE(CMIParticleType.VISUAL),
        STEP_SOUND(CMIParticleType.SOUND),
        POTION_BREAK(CMIParticleType.VISUAL),
        ENDER_SIGNAL(CMIParticleType.VISUAL),
        MOBSPAWNER_FLAMES(CMIParticleType.VISUAL),
        BREWING_STAND_BREW(CMIParticleType.SOUND),
        CHORUS_FLOWER_GROW(CMIParticleType.SOUND),
        CHORUS_FLOWER_DEATH(CMIParticleType.SOUND),
        PORTAL_TRAVEL(CMIParticleType.SOUND),
        ENDEREYE_LAUNCH(CMIParticleType.SOUND),
        FIREWORK_SHOOT(CMIParticleType.SOUND),
        VILLAGER_PLANT_GROW(CMIParticleType.VISUAL),
        DRAGON_BREATH(CMIParticleType.VISUAL),
        ANVIL_BREAK(CMIParticleType.SOUND),
        ANVIL_USE(CMIParticleType.SOUND),
        ANVIL_LAND(CMIParticleType.SOUND),
        ENDERDRAGON_SHOOT(CMIParticleType.SOUND),
        WITHER_BREAK_BLOCK(CMIParticleType.SOUND),
        WITHER_SHOOT(CMIParticleType.SOUND),
        ZOMBIE_INFECT(CMIParticleType.SOUND),
        ZOMBIE_CONVERTED_VILLAGER(CMIParticleType.SOUND),
        BAT_TAKEOFF(CMIParticleType.SOUND),
        END_GATEWAY_SPAWN(CMIParticleType.VISUAL),
        ENDERDRAGON_GROWL(CMIParticleType.SOUND),
        FIREWORKS_SPARK(CMIMaterial.FIRE_CHARGE),
        CRIT(CMIMaterial.IRON_SWORD),
        MAGIC_CRIT("CRIT_MAGIC", CMIMaterial.POTION),
        POTION_SWIRL("MOB_SPELL", "SPELL_MOB", CMIMaterial.BLAZE_ROD),
        POTION_SWIRL_TRANSPARENT("MOB_SPELL_AMBIENT", "SPELL_MOB_AMBIENT", CMIMaterial.BLAZE_POWDER),
        SPELL(CMIMaterial.MILK_BUCKET),
        INSTANT_SPELL("SPELL_INSTANT", CMIMaterial.GLASS_BOTTLE),
        WITCH_MAGIC("SPELL_WITCH", CMIMaterial.SPIDER_EYE),
        NOTE(CMIMaterial.NOTE_BLOCK),
        PORTAL(CMIMaterial.OBSIDIAN),
        @Deprecated
        FLYING_GLYPH("ENCHANTMENT_TABLE", "ENCHANT", CMIMaterial.ENCHANTING_TABLE),
        FLAME(CMIMaterial.FIRE_CHARGE),
        LAVA_POP("LAVA", CMIMaterial.FLINT_AND_STEEL),
        FOOTSTEP(CMIMaterial.IRON_BOOTS),
        SPLASH("WATER_SPLASH", CMIMaterial.STICK),
        PARTICLE_SMOKE("SMOKE", "SMOKE_NORMAL", CMIMaterial.ANVIL),
        EXPLOSION_HUGE("HUGE_EXPLOSION", CMIMaterial.FURNACE),
        EXPLOSION_LARGE("LARGE_EXPLODE", CMIMaterial.FURNACE),
        EXPLOSION("EXPLODE", "EXPLOSION_NORMAL", CMIMaterial.TNT),
        VOID_FOG("DEPTH_SUSPEND", "SUSPENDED_DEPTH", CMIMaterial.SALMON),
        SMALL_SMOKE("TOWN_AURA", CMIMaterial.MYCELIUM),
        CLOUD(CMIMaterial.COBWEB),
        @Deprecated
        COLOURED_DUST(Arrays.asList("RED_DUST", "REDSTONE"), CMIMaterial.REDSTONE, CMIParticleDataType.DustOptions),
        DUST(Arrays.asList("RED_DUST", "REDSTONE", "COLOURED_DUST"), CMIMaterial.REDSTONE, CMIParticleDataType.DustOptions),
        SNOWBALL_BREAK("SNOW_BALL_POOF", "SNOW_BALL", CMIMaterial.SNOWBALL),
        WATERDRIP("DRIP_WATER", "WATER_DROP", CMIMaterial.WATER_BUCKET),
        LAVADRIP("DRIP_LAVA", CMIMaterial.LAVA_BUCKET),
        SNOW_SHOVEL(CMIMaterial.DIAMOND_SHOVEL),
        SLIME(CMIMaterial.SLIME_BALL),
        HEART(CMIMaterial.ROSE_RED),
        VILLAGER_THUNDERCLOUD("ANGRY_VILLAGER", "VILLAGER_ANGRY", CMIParticleType.PARTICLE, CMIMaterial.EMERALD),
        HAPPY_VILLAGER("VILLAGER_HAPPY", CMIParticleType.PARTICLE, CMIMaterial.BOOK),
        LARGE_SMOKE("SMOKE_LARGE", CMIParticleType.PARTICLE, CMIMaterial.FURNACE),
        ITEM_BREAK("ICON_CRACK", CMIParticleType.NONE, CMIMaterial.DIAMOND_BOOTS),

        // 1.13
        WATER_BUBBLE(CMIMaterial.PRISMARINE_CRYSTALS),
        WATER_WAKE(CMIMaterial.SEAGRASS),
        SUSPENDED(CMIMaterial.KELP),
        BARRIER(CMIMaterial.BARRIER),
        MOB_APPEARANCE(CMIMaterial.ZOMBIE_HEAD),
        END_ROD(CMIMaterial.END_ROD),
        DAMAGE_INDICATOR(CMIMaterial.RED_DYE),
        SWEEP_ATTACK(CMIMaterial.IRON_SWORD),
        TOTEM(CMIMaterial.TOTEM_OF_UNDYING),
        SPIT(CMIMaterial.LLAMA_SPAWN_EGG),
        SQUID_INK(CMIMaterial.INK_SAC),
        BUBBLE_POP(CMIMaterial.PUFFERFISH),
        CURRENT_DOWN(CMIMaterial.HEART_OF_THE_SEA),
        BUBBLE_COLUMN_UP(CMIMaterial.CONDUIT),
        NAUTILUS(CMIMaterial.NAUTILUS_SHELL),
        DOLPHIN(CMIMaterial.DOLPHIN_SPAWN_EGG),

        // 1.16
        WATER_SPLASH(CMIMaterial.SPLASH_POTION),
        CAMPFIRE_SIGNAL_SMOKE(CMIMaterial.SOUL_CAMPFIRE),
        CAMPFIRE_COSY_SMOKE(CMIMaterial.CAMPFIRE),
        SNEEZE(CMIMaterial.SLIME_BALL),
        COMPOSTER(CMIMaterial.BONE_MEAL),
        FLASH(CMIMaterial.GLOWSTONE),
        FALLING_LAVA(CMIMaterial.MAGMA_BLOCK),
        LANDING_LAVA(CMIMaterial.BLAZE_POWDER),
        FALLING_WATER(CMIMaterial.WATER_BUCKET),
        DRIPPING_HONEY(CMIMaterial.HONEYCOMB),
        FALLING_HONEY(CMIMaterial.HONEY_BLOCK),
        LANDING_HONEY(CMIMaterial.HONEY_BOTTLE),
        FALLING_NECTAR(CMIMaterial.BEEHIVE),
        SOUL_FIRE_FLAME(CMIMaterial.SOUL_TORCH),
        ASH(CMIMaterial.BLACK_DYE),
        CRIMSON_SPORE(CMIMaterial.CRIMSON_FUNGUS),
        WARPED_SPORE(CMIMaterial.WARPED_FUNGUS),
        SOUL(CMIMaterial.SOUL_SOIL),
        DRIPPING_OBSIDIAN_TEAR(CMIMaterial.CRYING_OBSIDIAN),
        FALLING_OBSIDIAN_TEAR(CMIMaterial.ANCIENT_DEBRIS),
        LANDING_OBSIDIAN_TEAR(CMIMaterial.RESPAWN_ANCHOR),
        REVERSE_PORTAL(CMIMaterial.OBSIDIAN),
        WHITE_ASH(CMIMaterial.QUARTZ),

        // 1.17
        LIGHT(CMIMaterial.LIGHT),
        FALLING_SPORE_BLOSSOM(CMIMaterial.SPORE_BLOSSOM),
        SPORE_BLOSSOM_AIR(CMIMaterial.FLOWERING_AZALEA_LEAVES),
        SMALL_FLAME(CMIMaterial.FLINT_AND_STEEL),
        SNOWFLAKE(CMIMaterial.SNOWBALL),
        DRIPPING_DRIPSTONE_LAVA(CMIMaterial.DRIPSTONE_BLOCK),
        FALLING_DRIPSTONE_LAVA(CMIMaterial.POINTED_DRIPSTONE),
        DRIPPING_DRIPSTONE_WATER(CMIMaterial.DRIPSTONE_BLOCK),
        FALLING_DRIPSTONE_WATER(CMIMaterial.POINTED_DRIPSTONE),
        GLOW_SQUID_INK(CMIMaterial.GLOW_INK_SAC),
        GLOW(CMIMaterial.GLOW_BERRIES),
        WAX_ON(CMIMaterial.HONEYCOMB_BLOCK),
        WAX_OFF(CMIMaterial.COPPER_BLOCK),
        ELECTRIC_SPARK(CMIMaterial.AMETHYST_SHARD),
        SCRAPE(CMIMaterial.COPPER_INGOT),

        // 1.18
        BLOCK_MARKER(CMIParticleDataType.BlockData, CMIMaterial.REINFORCED_DEEPSLATE),

        // 1.19
        SONIC_BOOM(CMIMaterial.SCULK_SHRIEKER),
        SCULK_SOUL(CMIMaterial.SCULK_SENSOR),
        SCULK_CHARGE_POP(CMIMaterial.SCULK_CATALYST),
        CHERRY_LEAVES(CMIMaterial.CHERRY_LEAVES),

        // 1.20.5
        SMALL_GUST(CMIMaterial.FEATHER),
        TRIAL_SPAWNER_DETECTION_OMINOUS(CMIMaterial.OAK_TRAPDOOR),
        VAULT_CONNECTION(CMIMaterial.ENDER_CHEST),
        INFESTED(CMIMaterial.INFESTED_STONE),
        ITEM_COBWEB(CMIMaterial.COBWEB),
        OMINOUS_SPAWNING(CMIMaterial.WITHER_SKELETON_SKULL),
        RAID_OMEN(CMIMaterial.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE),
        TRIAL_OMEN(CMIMaterial.BREEZE_SPAWN_EGG),

        // 1.21
        POOF(CMIMaterial.FIREWORK_STAR),
        EXPLOSION_EMITTER(CMIMaterial.TNT),
        FIREWORK(CMIMaterial.FIREWORK_ROCKET),
        BUBBLE(CMIMaterial.BUBBLE_CORAL),
        FISHING(CMIMaterial.FISHING_ROD),
        UNDERWATER(CMIMaterial.TURTLE_EGG),
        ENCHANTED_HIT(CMIMaterial.ENCHANTED_BOOK),
        EFFECT(CMIMaterial.POTION),
        INSTANT_EFFECT(CMIMaterial.GOLDEN_APPLE),
        ENTITY_EFFECT(CMIParticleDataType.Color, CMIMaterial.GLOWSTONE_DUST),
        WITCH(CMIMaterial.POTION),
        DRIPPING_WATER(CMIMaterial.ICE),
        DRIPPING_LAVA(CMIMaterial.BASALT),
        MYCELIUM(CMIMaterial.MYCELIUM),
        ENCHANT("FLYING_GLYPH", "ENCHANTMENT_TABLE", CMIMaterial.ENCHANTED_BOOK),
        ITEM_SNOWBALL(CMIMaterial.SNOWBALL),
        ITEM_SLIME(CMIMaterial.SLIME_BALL),
        ITEM(CMIParticleDataType.ItemStack),
        BLOCK(CMIParticleDataType.BlockData),
        RAIN(CMIMaterial.BLUE_STAINED_GLASS),
        ELDER_GUARDIAN(CMIMaterial.PRISMARINE_SHARD),
        FALLING_DUST(CMIParticleDataType.BlockData),
        TOTEM_OF_UNDYING(CMIMaterial.TOTEM_OF_UNDYING),
        DUST_COLOR_TRANSITION(CMIParticleDataType.DustTransition),
        VIBRATION(CMIParticleDataType.Vibration),
        SCULK_CHARGE(CMIParticleDataType.Float),
        SHRIEK(CMIParticleDataType.Int),
        EGG_CRACK(CMIMaterial.TURTLE_EGG),
        DUST_PLUME(CMIMaterial.BREEZE_ROD),
        WHITE_SMOKE(CMIMaterial.HEAVY_CORE),
        GUST(CMIMaterial.OMINOUS_BOTTLE),
        GUST_EMITTER_LARGE(CMIMaterial.POLISHED_TUFF_STAIRS),
        GUST_EMITTER_SMALL(CMIMaterial.POLISHED_TUFF_WALL),
        TRIAL_SPAWNER_DETECTION(CMIMaterial.TRIAL_SPAWNER),
        DUST_PILLAR(CMIParticleDataType.BlockData, CMIMaterial.RESIN_CLUMP),

        // 1.21.5
        PALE_OAK_LEAVES(CMIMaterial.PALE_OAK_LEAVES),
        TINTED_LEAVES(CMIParticleDataType.Color, CMIMaterial.LEAF_LITTER),
        BLOCK_CRUMBLE(CMIParticleDataType.BlockData, CMIMaterial.BROWN_EGG),
        TRAIL(CMIParticleDataType.Trail, CMIMaterial.BUSH),
        FIREFLY(CMIMaterial.FIREFLY_BUSH);

        static HashMap<String, CMIParticle> byName = new HashMap<String, CMIParticle>();
        static HashMap<Object, CMIParticle> byType = new HashMap<Object, CMIParticle>();

        static {
            for (CMIParticle one : CMIParticle.values()) {

                if (Version.isCurrentEqualOrHigher(Version.v1_20_R4)) {
                    if (one.equals(CMIParticle.COLOURED_DUST) || one.equals(CMIParticle.FLYING_GLYPH))
                        continue;
                }

                if (Version.isCurrentEqualOrLower(Version.v1_20_R3)) {
                    if (one.equals(CMIParticle.DUST) || one.equals(CMIParticle.FLYING_GLYPH))
                        continue;
                }

                byName.put(one.toString().replace("_", "").toLowerCase(), one);
                byName.put(one.getName().replace("_", "").replace(" ", "").toLowerCase(), one);

                if (!one.getSecondaryNames().isEmpty()) {
                    for (String name : one.getSecondaryNames()) {
                        byName.put(name.replace("_", "").toLowerCase(), one);
                    }
                }

                if (one.getParticle() != null)
                    byType.put(one.getParticle(), one);
            }
        }

        private String name;
        private List<String> secondaryNames = new ArrayList<>();

        private CMIParticleType type;
        private CMIMaterial icon;
        private Object particle;
        private Effect effect;
        @Deprecated
        private Object EnumParticle;
        @Deprecated
        private int[] extra;
        private CMIParticleDataType dataType = CMIParticleDataType.Void;

        CMIParticle() {
            this(new ArrayList<>(), null, null, null);
        }

        CMIParticle(CMIMaterial material) {
            this(new ArrayList<>(), null, material.getMaterial() == null ? CMIMaterial.STONE : material, null);
        }

        CMIParticle(CMIParticleType type) {
            this(new ArrayList<>(), type, null, null);
        }

        CMIParticle(CMIParticleType type, CMIMaterial icon) {
            this(new ArrayList<>(), type, icon, CMIParticleDataType.Void);
        }

        CMIParticle(CMIParticleDataType dataType, CMIMaterial icon) {
            this(new ArrayList<>(), null, icon, dataType);
        }

        CMIParticle(CMIParticleDataType dataType) {
            this(new ArrayList<>(), null, null, dataType);
        }

        CMIParticle(CMIParticleType type, CMIMaterial icon, CMIParticleDataType dataType) {
            this(new ArrayList<>(), type, icon, dataType);
        }

        CMIParticle(String secondaryName, String name) {
            this(Arrays.asList(secondaryName, name), null, null, null);
        }

        CMIParticle(String secondaryName) {
            this(Arrays.asList(secondaryName), null, null, null);
        }

        CMIParticle(String secondaryName, CMIMaterial icon) {
            this(Arrays.asList(secondaryName), null, icon, null);
        }

        CMIParticle(String secondaryName, CMIParticleType type, CMIMaterial icon) {
            this(Arrays.asList(secondaryName), type, icon, null);
        }

        CMIParticle(String name, String secondaryName, CMIParticleType type, CMIMaterial icon) {
            this(Arrays.asList(name, secondaryName), type, icon, CMIParticleDataType.Void);
        }

        CMIParticle(String name, String secondaryName, CMIMaterial icon) {
            this(Arrays.asList(name, secondaryName), null, icon, CMIParticleDataType.Void);
        }

        CMIParticle(List<String> secondaryNames, CMIMaterial icon, CMIParticleDataType dataType) {
            this(secondaryNames, null, icon, dataType);
        }

        CMIParticle(List<String> secondaryNames, CMIParticleType type, CMIMaterial icon, CMIParticleDataType dataType) {
            this.name = name == null ? CMIText.everyFirstToUpperCase(this.toString()) : name;
            this.secondaryNames = secondaryNames;

            this.type = type == null ? CMIParticleType.PARTICLE : type;
            this.icon = icon == null ? CMIMaterial.STONE : icon;
            this.dataType = dataType == null ? CMIParticleDataType.Void : dataType;
        }

        public String getName() {
            return name;
        }

        @Deprecated
        public int getId() {
            return 0;
        }

        public CMIParticleType getType() {
            return type;
        }

        public boolean isParticle() {
            return type == CMIParticleType.PARTICLE;
        }

        public boolean isColored() {
            return dataType.contains(CMIEffectDataValueTypes.ColorFrom);
        }

        public static boolean isParticle(Effect effect) {
            if (effect == null)
                return false;
            CMIParticle cmiEffect = get(effect.toString());
            if (cmiEffect == null)
                return false;
            return cmiEffect.isParticle();
        }

        public static Material getSafeIcon(Effect effect) {
            CMIParticle cmiEffect = get(effect.toString());
            if (cmiEffect == null)
                return Material.STONE;
            return cmiEffect.getIcon() == null ? Material.STONE : cmiEffect.getIcon();
        }

        public Material getSafeIcon() {
            return getIcon() == null ? Material.STONE : getIcon();
        }

        @Deprecated
        public static CMIParticle getCMIParticle(String name) {
            return get(name);
        }

        public static CMIParticle get(String name) {
            if (name == null)
                return null;

            name = name.replace("_", "").replace(" ", "").toLowerCase();

            return byName.get(name);
        }

        public static CMIEffect getCMIEffect(String name) {
            return CMIEffect.get(name);
        }

        public Effect getEffect() {
            if (effect != null)
                return effect;
            if (!isParticle())
                return null;

            String n1 = this.toString().replace("_", "").toLowerCase();
            String n2 = this.name().replace("_", "").toLowerCase();
            String n3 = this.getName().replace("_", "").toLowerCase();

            for (Effect one : Effect.values()) {
                String name1 = one.toString().toLowerCase().replace("_", "");
                String name2 = one.name().toLowerCase().replace("_", "");
                if (!name1.equalsIgnoreCase(n1) &&
                        !name1.equalsIgnoreCase(n2) &&
                        !name1.equalsIgnoreCase(n3) &&
                        !name2.equalsIgnoreCase(n1) &&
                        !name2.equalsIgnoreCase(n2) &&
                        !name2.equalsIgnoreCase(n3))
                    continue;
                effect = one;
                break;
            }

            if (effect != null)
                return effect;

            main: for (String oneS : getSecondaryNames()) {
                String n = oneS.replace("_", "").toLowerCase();
                if (n.isEmpty())
                    continue;

                for (Effect one : Effect.values()) {
                    if (!one.toString().toLowerCase().replace("_", "").equalsIgnoreCase(n))
                        continue;
                    effect = one;
                    break main;
                }
            }

            return effect;
        }

        public Material getIcon() {
            return icon == null ? Material.STONE : icon.getMaterial();
        }

        static List<CMIParticle> ls = new ArrayList<CMIParticle>();

        public static List<CMIParticle> getParticleList() {
            if (!ls.isEmpty())
                return ls;

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

            List<CMIParticle> list = getParticleList();

            CMIParticle first = null;

            for (int i = 0; i < list.size(); i++) {
                CMIParticle next = list.get(i);

                if (next == null || !next.isParticle() || Version.isCurrentEqualOrHigher(Version.v1_9_R1) && next.getParticle() == null)
                    continue;

                if (first == null)
                    first = next;

                if (next.equals(this)) {
                    if (i == list.size() - 1)
                        return list.get(0);
                    return list.get(i + 1);
                }
            }
            return first == null ? this : first;
        }

        public CMIParticle getPrevParticleEffect() {
            List<CMIParticle> list = getParticleList();

            CMIParticle first = null;

            for (int i = 0; i < list.size(); i++) {
                CMIParticle next = list.get(i);

                if (next == null || !next.isParticle() || Version.isCurrentEqualOrHigher(Version.v1_9_R1) && next.getParticle() == null)
                    continue;

                if (first == null)
                    first = next;

                if (next.equals(this)) {
                    if (i == 0)
                        return list.get(list.size() - 1);
                    return list.get(i - 1);
                }
            }
            return first == null ? this : first;
        }

        @Deprecated
        public String getSecondaryName() {
            return this.secondaryNames.isEmpty() ? "" : secondaryNames.get(0);
        }

        @Deprecated
        public void setSecondaryName(String secondaryName) {
            this.secondaryNames.clear();
            this.secondaryNames.add(secondaryName);
        }

        public boolean is(String name) {

            name = name.replace("_", "");

            if (toString().replace("_", "").equalsIgnoreCase(name))
                return true;

            if (getName().replace("_", "").equalsIgnoreCase(name))
                return true;

            for (String one : this.getSecondaryNames()) {

                if (one.replace("_", "").equalsIgnoreCase(name))
                    return true;
            }
            return false;
        }

        public List<String> getSecondaryNames() {
            return this.secondaryNames;
        }

        public org.bukkit.Particle getParticle() {

            if (Version.isCurrentEqualOrLower(Version.v1_8_R3))
                return null;

            if (particle != null)
                return (org.bukkit.Particle) particle;

            String n1 = this.toString().replace("_", "").toLowerCase();
            String n2 = this.name().replace("_", "").toLowerCase();
            String n3 = this.getName().replace("_", "").toLowerCase();

            for (org.bukkit.Particle one : org.bukkit.Particle.values()) {
                String name1 = one.toString().toLowerCase().replace("_", "");
                String name2 = one.name().toLowerCase().replace("_", "");
                if (!name1.equalsIgnoreCase(n1)
                        && !name1.equalsIgnoreCase(n2)
                        && !name1.equalsIgnoreCase(n3)
                        && !name2.equalsIgnoreCase(n1)
                        && !name2.equalsIgnoreCase(n2)
                        && !name2.equalsIgnoreCase(n3))
                    continue;
                particle = one;
                return one;
            }

            for (String oneS : getSecondaryNames()) {
                String n = oneS.replace("_", "").toLowerCase();
                if (n.isEmpty())
                    continue;
                for (org.bukkit.Particle one : org.bukkit.Particle.values()) {
                    if (!one.toString().toLowerCase().replace("_", "").equalsIgnoreCase(n))
                        continue;
                    particle = one;
                    return one;
                }
            }

            return particle == null ? null : (org.bukkit.Particle) particle;
        }

        @Deprecated
        public Object getEnumParticle() {
            return EnumParticle;
        }

        @Deprecated
        public void setEnumParticle(Object enumParticle) {
            EnumParticle = enumParticle;
        }

        @Deprecated
        public int[] getExtra() {
            return extra;
        }

        @Deprecated
        public void setExtra(int[] extra) {
            this.extra = extra;
        }

        public CMIParticleDataType getDataType() {
            return dataType;
        }
    }

    private CMILib plugin;
    private static Class<?> PacketPlayOutWorldParticles;
    private static Class<?> EnumParticle;
    private static Class<?> CraftParticle;
    private static Class<?> ParticleParam;

    private static Constructor<?> effectConstructor = null;
    private static Method CraftParticleMethod = null;
    private static Constructor<?> vibrationConstructor = null;
    private static Constructor<?> destinationConstructor = null;

    public CMIEffectManager(CMILib plugin) {
        this.plugin = plugin;
    }

    static {
        if (Version.isMojangMappings()) {

            try {
                PacketPlayOutWorldParticles = Class.forName("net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket");
                ParticleParam = Class.forName("net.minecraft.core.particles.ParticleOptions");
                CraftParticle = Class.forName("org.bukkit.craftbukkit.CraftParticle");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
            try {
                PacketPlayOutWorldParticles = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutWorldParticles");
                ParticleParam = Class.forName("net.minecraft.core.particles.ParticleParam");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            CraftParticle = Reflections.getBukkitClass("CraftParticle");
        } else {
            try {
                PacketPlayOutWorldParticles = CMILib.getInstance().getReflectionManager().getMinecraftClass("PacketPlayOutWorldParticles");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (Version.isCurrentEqualOrLower(Version.v1_12_R1))
                    EnumParticle = CMILib.getInstance().getReflectionManager().getMinecraftClass("EnumParticle");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            if (Version.isCurrentHigher(Version.v1_12_R1)) {
                try {
                    CraftParticle = Reflections.getBukkitClass("CraftParticle");
                } catch (Throwable e) {
                }
                try {
                    ParticleParam = CMILib.getInstance().getReflectionManager().getMinecraftClass("ParticleParam");
                } catch (Throwable e) {
                }
            }
        }
    }

    public static Object getParticleParameter(@Nullable Location location, @Nonnull CMIEffect ef) {
        Object dd = null;
        try {
            switch (ef.getParticle().getDataType()) {
            case BlockData:
                dd = Bukkit.createBlockData(ef.getMaterial() == null ? CMIMaterial.STONE.getMaterial() : ef.getMaterial().getMaterial());
                break;
            case Color:
                dd = ef.getColorFrom();
                break;
            case DustOptions:
                dd = new org.bukkit.Particle.DustOptions(ef.getColorFrom(), ef.getSize());
                break;
            case DustTransition:
                dd = new org.bukkit.Particle.DustTransition(ef.getColorFrom(), ef.getColorTo(), ef.getSize());
                break;
            case EntityData:
                break;
            case Float:
                dd = ef.getSpeed();
                break;
            case Int:
                dd = (int) ef.getSpeed();
                break;
            case ItemStack:
                dd = ef.getMaterial() != null ? ef.getMaterial().newItemStack() : CMIMaterial.OAK_BUTTON.newItemStack();
                break;
            case MaterialData:
                break;
            case Trail:
                if (location != null)
                    dd = new Trail(location, ef.getColorFrom(), ef.getDuration());
                break;
            case Vibration:
                if (destinationConstructor == null)
                    destinationConstructor = Class.forName("org.bukkit.Vibration$Destination$BlockDestination").getConstructor(Location.class);
                if (vibrationConstructor == null) {
                    if (Version.isCurrentEqualOrHigher(Version.v1_21_R1))
                        vibrationConstructor = org.bukkit.Vibration.class.getConstructor(Location.class, Class.forName("org.bukkit.Vibration$Destination"), int.class);
                    else
                        vibrationConstructor = org.bukkit.Vibration.class.getConstructor(Class.forName("org.bukkit.Vibration$Destination$BlockDestination"), int.class);
                }

                if (location != null) {
                    if (Version.isCurrentEqualOrHigher(Version.v1_21_R1))
                        dd = vibrationConstructor.newInstance(location, destinationConstructor.newInstance(location), 20);
                    else
                        dd = vibrationConstructor.newInstance(destinationConstructor.newInstance(location), 20);
                }
                break;
            case Void:
                break;
            default:
                break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return dd;
    }

    public static @Nullable Object getParticleParameters(@Nullable Location location, @Nonnull CMIEffect ef) {

        org.bukkit.Particle particle = ef.getParticle().getParticle();

        if (particle == null)
            return null;

        try {
            Object dd = getParticleParameter(location, ef);

            if (CraftParticleMethod == null) {
                try {
                    CraftParticleMethod = CraftParticle.getMethod("toNMS", org.bukkit.Particle.class, Object.class);
                } catch (Throwable e) {
                    CraftParticleMethod = CraftParticle.getMethod("createParticleParam", org.bukkit.Particle.class, Object.class);
                }
            }

            return CraftParticleMethod.invoke(null, particle, dd);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void playFor1_17Up(Object playerConnection, Location location, CMIEffect ef) {

        Object particleParam = ef.getParticleParameters(location);

        if (particleParam == null) {
            return;
        }

        try {
            Object packet = null;

            if (Version.isMojangMappings()) {

                if (effectConstructor == null) {
                    effectConstructor = PacketPlayOutWorldParticles.getConstructor(
                            ParticleParam,
                            // overrideLimiter
                            boolean.class,
                            // alwaysShow
                            boolean.class,
                            // posX
                            double.class,
							// posY
                            double.class,
							// posZ
                            double.class,
                            // xDist
                            float.class,
                            // yDist
                            float.class,
							// zDist
                            float.class,
                            // maxSpeed
                            float.class,
                            // count
                            int.class);
                }

                packet = effectConstructor.newInstance(
                        particleParam,
                        true,
                        true,
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        (float) ef.getOffset().getX(),
                        (float) ef.getOffset().getY(),
                        (float) ef.getOffset().getZ(),
                        ef.getSpeed(),
                        ef.getAmount());

            } else if (Version.isCurrentEqualOrLower(Version.v1_21_R2)) {
                if (effectConstructor == null) {
                    Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutWorldParticles");
                    Class<?> particleParamClass = Class.forName("net.minecraft.core.particles.ParticleParam");
                    effectConstructor = packetClass.getConstructor(
                            particleParamClass,
                            boolean.class,
                            double.class,
                            double.class,
                            double.class,
                            float.class,
                            float.class,
                            float.class,
                            float.class,
                            int.class);
                }

                packet = effectConstructor.newInstance(
                        particleParam,
                        true,
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        (float) ef.getOffset().getX(),
                        (float) ef.getOffset().getY(),
                        (float) ef.getOffset().getZ(),
                        ef.getSpeed(),
                        ef.getAmount());

            } else {
                if (effectConstructor == null) {
                    Class<?> packetClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutWorldParticles");
                    Class<?> particleParamClass = Class.forName("net.minecraft.core.particles.ParticleParam");
                    effectConstructor = packetClass.getConstructor(
                            particleParamClass,
                            boolean.class,
                            boolean.class,
                            double.class,
                            double.class,
                            double.class,
                            float.class,
                            float.class,
                            float.class,
                            float.class,
                            int.class);
                }

                packet = effectConstructor.newInstance(
                        particleParam,
                        true,
                        false,
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        (float) ef.getOffset().getX(),
                        (float) ef.getOffset().getY(),
                        (float) ef.getOffset().getZ(),
                        ef.getSpeed(),
                        ef.getAmount());
            }

            CMILib.getInstance().getReflectionManager().sendPacket(playerConnection, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void playEffect(Player player, Location location, CMIEffect ef) {

        if (location == null || location.getWorld() == null || player == null || !player.isOnline())
            return;

        if (!location.getWorld().equals(player.getWorld()))
            return;

        playEffect(CMILib.getInstance().getReflectionManager().getPlayerConnection(player), location, ef);
    }

    public static void playEffect(Object playerConnection, Location location, CMIEffect ef) {

        if (location == null || location.getWorld() == null || playerConnection == null)
            return;

        if (ef == null || ef.getParticle() == null)
            return;

        if (!ef.getParticle().isParticle())
            return;

        try {
            if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
                playFor1_17Up(playerConnection, location, ef);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_14_R2)) {
                playFor1_14Up(playerConnection, location, ef);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_13_R1)) {
                playFor1_13Up(playerConnection, location, ef);
            } else if (Version.isCurrentEqualOrHigher(Version.v1_8_R1)) {
                playFor1_8Up(playerConnection, location, ef);
            } else {
                playForAncient(playerConnection, location, ef);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void playForAncient(Object playerConnection, Location location, CMIEffect ef) {
        Effect effect = ef.getParticle().getEffect();

        if (effect == null)
            return;

        try {
            if (effectConstructor == null)
                effectConstructor = PacketPlayOutWorldParticles.getConstructor(String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
            Object newPack = effectConstructor.newInstance(effect.name(), (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) ef.getOffset().getX(),
                    (float) ef.getOffset().getY(), (float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount());
            CMILib.getInstance().getReflectionManager().sendPacket(playerConnection, newPack);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void playFor1_8Up(Object playerConnection, Location location, CMIEffect ef) {

        Effect effect = ef.getParticle().getEffect();

        if (effect == null)
            return;

        try {
            Object particle = ef.getParticle().getEnumParticle() == null ? null : EnumParticle.cast(ef.getParticle().getEnumParticle());
            int[] extra = ef.getParticle().getExtra();

            if (particle == null) {

                for (Object p : EnumParticle.getEnumConstants()) {

                    String name = p.toString().replace("_", "");

                    if (ef.getParticle().is(name)) {
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

            Object newPack = null;
            if (ef.getParticle().isColored())
                newPack = effectConstructor.newInstance(particle,
                        true,
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        ef.getColorFrom().getRed() / 255F,
                        ef.getColorFrom().getGreen() / 255F,
                        ef.getColorFrom().getBlue() / 255F,
                        1,
                        0,
                        extra);
            else
                newPack = effectConstructor.newInstance(particle,
                        true,
                        (float) location.getX(),
                        (float) location.getY(),
                        (float) location.getZ(),
                        (float) ef.getOffset().getX(),
                        (float) ef.getOffset().getY(),
                        (float) ef.getOffset().getZ(),
                        ef.getSpeed(),
                        ef.getAmount(),
                        extra);

            CMILib.getInstance().getReflectionManager().sendPacket(playerConnection, newPack);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void playFor1_13Up(Object playerConnection, Location location, CMIEffect ef) {

        org.bukkit.Particle particle = ef.getParticle().getParticle();

        if (particle == null)
            return;
        try {
            org.bukkit.Particle.DustOptions dd = null;
            if (particle.toString().equals("REDSTONE"))
                dd = new org.bukkit.Particle.DustOptions(ef.getColor(), ef.getSize());

            if (CraftParticleMethod == null)
                CraftParticleMethod = CraftParticle.getMethod("toNMS", org.bukkit.Particle.class, Object.class);

            if (effectConstructor == null)
                effectConstructor = PacketPlayOutWorldParticles.getConstructor(ParticleParam, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class,
                        float.class, int.class);

            Object param = CraftParticleMethod.invoke(null, particle, dd);
            Object packet = effectConstructor.newInstance(param, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (float) ef
                    .getOffset().getX(), (float) ef.getOffset().getY(), (float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount());
            CMILib.getInstance().getReflectionManager().sendPacket(playerConnection, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void playFor1_14Up(Object playerConnection, Location location, CMIEffect ef) {

        org.bukkit.Particle particle = ef.getParticle().getParticle();

        if (particle == null)
            return;

        try {
            org.bukkit.Particle.DustOptions dd = null;
            if (particle.toString().equals("REDSTONE"))
                dd = new org.bukkit.Particle.DustOptions(ef.getColorFrom(), ef.getSize());

            if (CraftParticleMethod == null)
                CraftParticleMethod = CraftParticle.getMethod("toNMS", org.bukkit.Particle.class, Object.class);

            if (effectConstructor == null)
                effectConstructor = PacketPlayOutWorldParticles.getConstructor(ParticleParam, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class,
                        float.class, int.class);

            Object packet = effectConstructor.newInstance(CraftParticleMethod.invoke(null, particle, dd), true, location.getX(), location.getY(), location.getZ(), (float) ef
                    .getOffset().getX(), (float) ef.getOffset().getY(), (float) ef.getOffset().getZ(), ef.getSpeed(), ef.getAmount());
            CMILib.getInstance().getReflectionManager().sendPacket(playerConnection, packet);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
