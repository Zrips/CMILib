package net.Zrips.CMILib.Container;

import java.awt.Color;
import java.util.HashMap;

public enum CMIBiomeType {

    OCEAN(0, "000070"),
    PLAINS(1, "8DB360"),
    DESERT(2, "FA9418"),
    MOUNTAINS(3, "606060"),
    FOREST(4, "08812b"),
    TAIGA(5, "0B6659"),
    SWAMP(6, "097f5c"),
    RIVER(7, "0000FF"),
    NETHER(8, "FF0000"),
    THE_END(9, "8080FF"),
    FROZEN_OCEAN(10, "9090A0"),
    FROZEN_RIVER(11, "A0A0FF"),
    SNOWY_TUNDRA(12, "FFFFFF"),
    SNOWY_MOUNTAINS(13, "A0A0A0"),
    MUSHROOM_FIELDS(14, "FF00FF"),
    MUSHROOM_FIELD_SHORE(15, "A000FF"),
    BEACH(16, "FADE55"),
    DESERT_HILLS(17, "D25F12"),
    WOODED_HILLS(18, "22551C"),
    TAIGA_HILLS(19, "163933"),
    MOUNTAIN_EDGE(20, "72789A"),
    JUNGLE(21, "537B09"),
    JUNGLE_HILLS(22, "2C4205"),
    JUNGLE_EDGE(23, "628B17"),
    DEEP_OCEAN(24, "000030"),
    STONE_SHORE(25, "A2A284"),
    SNOWY_BEACH(26, "FAF0C0"),
    BIRCH_FOREST(27, "307444"),
    BIRCH_FOREST_HILLS(28, "1F5F32"),
    DARK_FOREST(29, "40511A"),
    SNOWY_TAIGA(30, "31554A"),
    SNOWY_TAIGA_HILLS(31, "243F36"),
    GIANT_TREE_TAIGA(32, "596651"),
    GIANT_TREE_TAIGA_HILLS(33, "545F3E"),
    WOODED_MOUNTAINS(34, "507050"),
    SAVANNA(35, "BDB25F"),
    SAVANNA_PLATEAU(36, "A79D64"),
    BADLANDS(37, "D94515"),
    WOODED_BADLANDS_PLATEAU(38, "B09765"),
    BADLANDS_PLATEAU(39, "CA8C65"),

    SMALL_END_ISLANDS(40, "8080FF"),
    END_MIDLANDS(41, "8080FF"),
    END_HIGHLANDS(42, "8080FF"),
    END_BARRENS(43, "8080FF"),
    WARM_OCEAN(44, "000070"),
    LUKEWARM_OCEAN(45, "000070"),
    COLD_OCEAN(46, "000070"),
    DEEP_WARM_OCEAN(47, "000070"),
    DEEP_LUKEWARM_OCEAN(48, "000070"),
    DEEP_COLD_OCEAN(49, "000070"),
    DEEP_FROZEN_OCEAN(50, "000070"),

    THE_VOID(51, "000000"),
    SUNFLOWER_PLAINS(52, "B5DB88"),
    DESERT_LAKES(53, "FFBC40"),
    GRAVELLY_MOUNTAINS(54, "888888"),
    FLOWER_FOREST(55, "6A7425"),
    TAIGA_MOUNTAINS(56, "596651"),
    SWAMP_HILLS(57, "2FFFDA"),
    ICE_SPIKES(58, "B4DCDC"),
    MODIFIED_JUNGLE(59, "7BA331"),
    MODIFIED_JUNGLE_EDGE(60, "8AB33F"),
    TALL_BIRCH_FOREST(61, "589C6C"),
    TALL_BIRCH_HILLS(62, "47875A"),
    DARK_FOREST_HILLS(63, "687942"),
    SNOWY_TAIGA_MOUNTAINS(64, "597D72"),
    GIANT_SPRUCE_TAIGA(65, "6B5F4C"),
    GIANT_SPRUCE_TAIGA_HILLS(66, "6D7766"),
    MODIFIED_GRAVELLY_MOUNTAINS(67, "789878"),
    SHATTERED_SAVANNA(68, "E5DA87"),
    SHATTERED_SAVANNA_PLATEAU(69, "CFC58C"),
    ERODED_BADLANDS(70, "FF6D3D"),
    MODIFIED_WOODED_BADLANDS_PLATEAU(71, "D8BF8D"),
    MODIFIED_BADLANDS_PLATEAU(72, "F2B48D"),

    GROVE("A7CC9A"),
    JAGGED_PEAKS("D9E5E3"),
    SOUL_SAND_VALLEY("705D56"),
    CHERRY_GROVE("FFB7C5"),
    OLD_GROWTH_PINE_TAIGA("556B2F"),
    WINDSWEPT_HILLS("9BA3B5"),
    STONY_PEAKS("D7D7D7"),
    WARPED_FOREST("3D6768"),
    CRIMSON_FOREST("A71F25"),
    LUSH_CAVES("7FB77E"),
    WINDSWEPT_GRAVELLY_HILLS("8D8D8D"),
    STONY_SHORE("B1B1B1"),
    FROZEN_PEAKS("E0F1FF"),
    MANGROVE_SWAMP("6A8F4E"),
    NETHER_WASTES("4A2E29"),
    WINDSWEPT_SAVANNA("B7843F"),
    OLD_GROWTH_SPRUCE_TAIGA("6B8E23"),
    DEEP_DARK("1E1E1E"),
    WOODED_BADLANDS("AB4F41"),
    WINDSWEPT_FOREST("6E7C47"),
    SNOWY_SLOPES("D4F1F9"),
    BASALT_DELTAS("2B2B2B"),
    SPARSE_JUNGLE("8EBF77"),
    MEADOW("C6E3C1"),
    DRIPSTONE_CAVES("967C66"),
    OLD_GROWTH_BIRCH_FOREST("7E9A5B"),
    SNOWY_PLAINS("E6F2FA"),
    BAMBOO_JUNGLE("A1C551"),

    //1.21.6
    PALE_GARDEN("AAC8A4");

    private Color color = new Color(0, 0, 0);
    private int id = -1;

    CMIBiomeType(String colorCode) {
        this(-1, colorCode);
    }

    CMIBiomeType(int id, String colorCode) {
        this.color = Color.decode("#" + colorCode);
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private static HashMap<String, CMIBiomeType> types = new HashMap<String, CMIBiomeType>();

    static {
        for (CMIBiomeType one : values()) {
            types.put(one.toString().toLowerCase().replace("_", ""), one);
        }
    }

    public static CMIBiomeType get(String name) {
        return types.get(name.toLowerCase().replace("_", "").replace(" ", ""));
    }
}
