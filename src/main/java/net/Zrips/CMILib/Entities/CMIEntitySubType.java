package net.Zrips.CMILib.Entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;

import net.Zrips.CMILib.Version.Version;

public enum CMIEntitySubType {

    TAMED,
    UNTAMED,
    BABY,
    ADULT,
    NOAI,
    AI,
    ANGRY,
    PASIVE,
    RED_CAT,
    SIAMESE_CAT,
    WILD_OCELOT,
    BLACK_CAT,

    WHITE,
    ORANGE,
    MAGENTA,
    LIGHT_BLUE,
    YELLOW,
    LIME,
    PINK,
    GRAY,
    LIGHT_GRAY,
    CYAN,
    PURPLE,
    BLUE,
    BROWN,
    GREEN,
    RED,
    BLACK,
    RAINBOW,

    CHESTNUT,
    CREAMY,
    DARK_BROWN,

    // Rabbit
    BLACK_AND_WHITE,
    GOLD,
    SALT_AND_PEPPER,
    THE_KILLER_BUNNY,

    //Fox
    SNOW,

    //Goat
    SCREAMING,

    // axolotl
    LUCY, WILD,

    // Panda
    AGGRESSIVE, LAZY, PLAYFUL, WEAK, WORRIED,

    //Cat    
    ALL_BLACK, BRITISH_SHORTHAIR, CALICO, JELLIE, PERSIAN, RAGDOLL, SIAMESE, TABBY,

    //Slimes
    SIZE1,
    SIZE2,
    SIZE3,
    SIZE4,
    SIZE5,
    SIZE6,
    SIZE7,
    SIZE8,
    SIZE9,
    SIZE10,

    //Creeper
    POWERED,
    UNPOWERED,

    // Villagers
    NORMAL, FARMER, LIBRARIAN, PRIEST, BLACKSMITH, BUTCHER, NITWIT, HUSK,
    ARMORER, CARTOGRAPHER, CLERIC, FISHERMAN, FLETCHER, LEATHERWORKER, MASON, SHEPHERD, TOOLSMITH, WEAPONSMITH,
    DESERT, JUNGLE, PLAINS, SAVANNA, SWAMP, TAIGA;

    public static CMIEntitySubType getByName(String name) {
	name = name.replace("_", "");
	for (CMIEntitySubType one : CMIEntitySubType.values()) {
	    if (one.toString().replace("_", "").equalsIgnoreCase(name))
		return one;
	}

	return null;
    }

    public static List<CMIEntitySubType> getSubTypes(Entity ent) {
	List<CMIEntitySubType> types = new ArrayList<CMIEntitySubType>();
	try {
	    if (ent instanceof LivingEntity) {
		LivingEntity lentity = (LivingEntity) ent;

		if (lentity instanceof Tameable) {
		    Tameable tamable = (Tameable) ent;
		    types.add(tamable.isTamed() ? CMIEntitySubType.TAMED : CMIEntitySubType.UNTAMED);
		}
		try {
		    if (lentity instanceof Ageable) {
			Ageable ageable = (Ageable) ent;
			types.add(ageable.isAdult() ? CMIEntitySubType.ADULT : CMIEntitySubType.BABY);
		    }
		} catch (Exception e) {
		}
	    }

	    if (ent instanceof Villager) {
		Villager villager = (Villager) ent;
		CMIEntitySubType c = CMIEntitySubType.getByName(villager.getProfession().toString());
		if (c != null)
		    types.add(c);

		if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
		    c = CMIEntitySubType.getByName(villager.getVillagerType().toString());
		    if (c != null)
			types.add(c);
		}

	    } else if (ent instanceof Ocelot) {
		Ocelot ocelot = (Ocelot) ent;

		CMIEntitySubType c = CMIEntitySubType.getByName(ocelot.getCatType().toString());
		if (c != null)
		    types.add(c);

	    } else if (ent instanceof Sheep) {
		Sheep sheep = (Sheep) ent;
		if (sheep.getCustomName() != null && sheep.getCustomName().equalsIgnoreCase("jeb_"))
		    types.add(CMIEntitySubType.RAINBOW);

		CMIEntitySubType c = CMIEntitySubType.getByName(sheep.getColor().toString());
		if (c != null)
		    types.add(c);

	    } else if (ent instanceof Wolf) {
		Wolf wolf = (Wolf) ent;
		types.add(wolf.isAngry() ? CMIEntitySubType.ANGRY : CMIEntitySubType.PASIVE);
	    } else if (ent instanceof Horse) {
		Horse horse = (Horse) ent;
		CMIEntitySubType c = CMIEntitySubType.getByName(horse.getColor().toString());
		if (c != null)
		    types.add(c);
	    } else if (ent instanceof Slime) {
		Slime slime = (Slime) ent;
		try {
		    CMIEntitySubType c = CMIEntitySubType.getByName("SIZE" + slime.getSize());
		    if (c != null)
			types.add(c);
		} catch (NumberFormatException e) {
		}
	    } else if (ent instanceof Creeper) {
		Creeper creeper = (Creeper) ent;

		types.add(creeper.isPowered() ? CMIEntitySubType.POWERED : CMIEntitySubType.UNPOWERED);
	    } else if (ent instanceof MagmaCube) {
		MagmaCube slime = (MagmaCube) ent;
		try {
		    CMIEntitySubType c = CMIEntitySubType.getByName("SIZE" + slime.getSize());
		    if (c != null)
			types.add(c);
		} catch (NumberFormatException e) {
		}
	    } else if (ent instanceof Rabbit) {
		Rabbit rabbit = (Rabbit) ent;
		try {
		    CMIEntitySubType c = CMIEntitySubType.getByName(rabbit.getRabbitType().toString());
		    if (c != null)
			types.add(c);
		} catch (NumberFormatException e) {
		}
	    }

	    if (Version.isCurrentEqualOrHigher(Version.v1_11_R1)) {
		if (ent instanceof org.bukkit.entity.Llama) {
		    org.bukkit.entity.Llama lam = (org.bukkit.entity.Llama) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(lam.getColor().toString());
		    if (c != null)
			types.add(c);
		    return types;
		} else if (ent instanceof org.bukkit.entity.TraderLlama) {
		    org.bukkit.entity.TraderLlama lam = (org.bukkit.entity.TraderLlama) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(lam.getColor().toString());
		    if (c != null)
			types.add(c);
		    return types;
		} else if (ent instanceof org.bukkit.entity.ZombieVillager) {
		    org.bukkit.entity.ZombieVillager zvil = (org.bukkit.entity.ZombieVillager) ent;

		    CMIEntitySubType c = CMIEntitySubType.getByName(zvil.getVillagerProfession().toString());
		    if (c != null)
			types.add(c);
		    return types;
		}
	    }
	    if (Version.isCurrentEqualOrHigher(Version.v1_12_R1)) {
		if (ent instanceof org.bukkit.entity.Shulker) {
		    org.bukkit.entity.Shulker sl = (org.bukkit.entity.Shulker) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(sl.getColor().toString());
		    if (c != null)
			types.add(c);
		    return types;
		} else if (ent instanceof org.bukkit.entity.Parrot) {
		    org.bukkit.entity.Parrot par = (org.bukkit.entity.Parrot) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(par.getVariant().toString());
		    if (c != null)
			types.add(c);
		    return types;
		}
	    }
	    if (Version.isCurrentEqualOrHigher(Version.v1_14_R1)) {
		if (ent instanceof org.bukkit.entity.MushroomCow) {
		    org.bukkit.entity.MushroomCow cow = (org.bukkit.entity.MushroomCow) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(cow.getVariant().toString());
		    if (c != null)
			types.add(c);
		    return types;
		} else if (ent instanceof org.bukkit.entity.Fox) {
		    org.bukkit.entity.Fox fox = (org.bukkit.entity.Fox) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(fox.getFoxType().toString());
		    if (c != null)
			types.add(c);
		    return types;
		} else if (ent instanceof org.bukkit.entity.Panda) {
		    org.bukkit.entity.Panda panda = (org.bukkit.entity.Panda) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(panda.getMainGene().toString());
		    if (c != null)
			types.add(c);

		    c = CMIEntitySubType.getByName(panda.getHiddenGene().toString());
		    if (c != null)
			types.add(c);

		    return types;
		} else if (ent instanceof org.bukkit.entity.Cat) {
		    org.bukkit.entity.Cat cat = (org.bukkit.entity.Cat) ent;

		    CMIEntitySubType c = CMIEntitySubType.getByName(cat.getCatType().toString());
		    if (c != null)
			types.add(c);
		    return types;
		}
	    }
	    if (Version.isCurrentEqualOrHigher(Version.v1_15_R1)) {
		if (ent instanceof org.bukkit.entity.Bee) {
		    org.bukkit.entity.Bee bee = (org.bukkit.entity.Bee) ent;

		    if (bee.getAnger() > 0)
			types.add(CMIEntitySubType.ANGRY);
		    else
			types.add(CMIEntitySubType.PASIVE);

		    return types;
		}
	    }
	    if (Version.isCurrentEqualOrHigher(Version.v1_17_R1)) {
		if (ent instanceof org.bukkit.entity.Goat) {
		    org.bukkit.entity.Goat goat = (org.bukkit.entity.Goat) ent;
		    if (goat.isScreaming())
			types.add(CMIEntitySubType.SCREAMING);
		    return types;
		} else if (ent instanceof org.bukkit.entity.Axolotl) {
		    org.bukkit.entity.Axolotl axolotl = (org.bukkit.entity.Axolotl) ent;
		    CMIEntitySubType c = CMIEntitySubType.getByName(axolotl.getVariant().toString());
		    if (c != null)
			types.add(c);
		    return types;
		}
	    }
	} catch (Exception | Error e) {
	}
	return types;
    }

}
