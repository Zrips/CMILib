package net.Zrips.CMILib.Entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import net.Zrips.CMILib.CMILib;
import net.Zrips.CMILib.Container.CMIText;
import net.Zrips.CMILib.Items.CMIMaterial;
import net.Zrips.CMILib.Locale.LC;

public enum CMIEntityType {

    ITEM("DROPPED_ITEM"),
    EXPERIENCE_ORB(),
    AREA_EFFECT_CLOUD(),
    ELDER_GUARDIAN(Arrays.asList(
        "MWM3OTc0ODJhMTRiZmNiODc3MjU3Y2IyY2ZmMWI2ZTZhOGI4NDEzMzM2ZmZiNGMyOWE2MTM5Mjc4YjQzNmIifX19")),
    WITHER_SKELETON(Arrays.asList(
        "Nzk1M2I2YzY4NDQ4ZTdlNmI2YmY4ZmIyNzNkNzIwM2FjZDhlMWJlMTllODE0ODFlYWQ1MWY0NWRlNTlhOCJ9fX0=",
        "NDk2YmM4ZWJkNGUxM2Y0OTZkOGQ3NGM1NjVkZDU2ZTk5YTRhZjJlMmVhN2EyN2E5NmMxYWJkMjg0MTg0YiJ9fX0=",
        "ZjVlYzk2NDY0NWE4ZWZhYzc2YmUyZjE2MGQ3Yzk5NTYzNjJmMzJiNjUxNzM5MGM1OWMzMDg1MDM0ZjA1MGNmZiJ9fX0=")),
    STRAY(Arrays.asList(
        "NzhkZGY3NmU1NTVkZDVjNGFhOGEwYTVmYzU4NDUyMGNkNjNkNDg5YzI1M2RlOTY5ZjdmMjJmODVhOWEyZDU2In19fQ==")),
    EGG("THROWN_EGG"),
    LEASH_KNOT("LEASH_HITCH"),
    PAINTING(),
    ARROW(),
    SNOWBALL(),
    FIREBALL(),
    SMALL_FIREBALL(),
    ENDER_PEARL("THROWN_ENDER_PEARL"),
    ENDER_SIGNAL("END_SIGNAL"),
    EYE_OF_ENDER(),
    POTION(),
    LIGHTNING_BOLT(),
    SPLASH_POTION(),
    EXPERIENCE_BOTTLE("THROWN_EXP_BOTTLE"),
    ITEM_FRAME(),
    WITHER_SKULL(),
    TNT("PRIMED_TNT"),
    FALLING_BLOCK(),
    FIREWORK_ROCKET("FIREWORK"),
    HUSK(Arrays.asList(
        "Nzc3MDY4MWQxYTI1NWZiNGY3NTQ3OTNhYTA1NWIyMjA0NDFjZGFiOWUxMTQxZGZhNTIzN2I0OTkzMWQ5YjkxYyJ9fX0=")),
    SPECTRAL_ARROW(),
    SHULKER_BULLET(),
    DRAGON_FIREBALL(),
    ZOMBIE_VILLAGER(Arrays.asList(
        "NDRmMDhlYmQ0ZTI1Y2RhM2FkZTQ1Yjg2MzM3OGFkMzc3ZjE4YzUxMGRiNGQyOGU4MmJiMjQ0NTE0MzliMzczNCJ9fX0=",
        "OTYxZjE5ZmZkOGFlNDI1NzkyYzRiMTgxNzU2YWRmZjhkNDgxNzRhZWVmNThhMGYzMjdhMjhjNzQyYzcyNDQyIn19fQ==",
        "NTI4YzJiYWQ1Mzg5Y2IzNTkyYjU2NWIzYzQ3ZWNjMTg5ZTA1NDJhODc4MzUwMjhkNjE0OGJiZTMzNDU2NDUifX19",
        "YTE2MTU1ZmNmMzY2Y2Y0ZTA2Y2U1ZGZmYzQ4Y2E1NGU4ZWE0OGRmZTUyNTM1OGI2MTJkYzQ0ZmQ0MzIifX19",
        "Y2ZmMDQ4MmZkMzJmYWIyY2U5ZjVmYTJlMmQ5YjRkYzc1NjFkYTQyMjE1MmM5OWZjODA0YjkxMzljYWYyNTZiIn19fQ==",
        "MzdlODM4Y2NjMjY3NzZhMjE3YzY3ODM4NmY2YTY1NzkxZmU4Y2RhYjhjZTljYTRhYzZiMjgzOTdhNGQ4MWMyMiJ9fX0=")),
    SKELETON_HORSE(Arrays.asList(
        "NDdlZmZjZTM1MTMyYzg2ZmY3MmJjYWU3N2RmYmIxZDIyNTg3ZTk0ZGYzY2JjMjU3MGVkMTdjZjg5NzNhIn19fQ==")),
    ZOMBIE_HORSE(Arrays.asList(
        "ZDIyOTUwZjJkM2VmZGRiMThkZTg2ZjhmNTVhYzUxOGRjZTczZjEyYTZlMGY4NjM2ZDU1MWQ4ZWI0ODBjZWVjIn19fQ==")),
    ARMOR_STAND(),
    DONKEY(Arrays.asList(
        "ZGZiNmMzYzA1MmNmNzg3ZDIzNmEyOTE1ZjgwNzJiNzdjNTQ3NDk3NzE1ZDFkMmY4Y2JjOWQyNDFkODhhIn19fQ==")),
    MULE(Arrays.asList(
        "YTA0ODZhNzQyZTdkZGEwYmFlNjFjZTJmNTVmYTEzNTI3ZjFjM2IzMzRjNTdjMDM0YmI0Y2YxMzJmYjVmNWYifX19")),
    EVOKER_FANGS(),
    EVOKER(Arrays.asList(
        "YTAwZDNmZmYxNmMyZGNhNTliOWM1OGYwOTY1MjVjODY5NzExNjZkYmFlMTMzYjFiMDUwZTVlZTcxNjQ0MyJ9fX0=")),
    VEX(Arrays.asList(
        "NWU3MzMwYzdkNWNkOGEwYTU1YWI5ZTk1MzIxNTM1YWM3YWUzMGZlODM3YzM3ZWE5ZTUzYmVhN2JhMmRlODZiIn19fQ==")),
    VINDICATOR(Arrays.asList(
        "YTAwZDNmZmYxNmMyZGNhNTliOWM1OGYwOTY1MjVjODY5NzExNjZkYmFlMTMzYjFiMDUwZTVlZTcxNjQ0MyJ9fX0=")),
    ILLUSIONER(Arrays.asList(
        "NTEyNTEyZTdkMDE2YTIzNDNhN2JmZjFhNGNkMTUzNTdhYjg1MTU3OWYxMzg5YmQ0ZTNhMjRjYmViODhiIn19fQ==",
        "MmYyODgyZGQwOTcyM2U0N2MwYWI5NjYzZWFiMDgzZDZhNTk2OTI3MzcwNjExMGM4MjkxMGU2MWJmOGE4ZjA3ZSJ9fX0=")),
    COMMAND_BLOCK_MINECART("COMMAND_MINECART", "MINECART_COMMAND"),
    BOAT(),
    MINECART(),
    CHEST_MINECART("MINECART_CHEST"),
    FURNACE_MINECART("MINECART_FURNACE"),
    TNT_MINECART("MINECART_TNT"),
    HOPPER_MINECART("MINECART_HOPPER"),
    SPAWNER_MINECART("MINECART_MOB_SPAWNER"),
    CREEPER(Arrays.asList(
        "ZjQyNTQ4MzhjMzNlYTIyN2ZmY2EyMjNkZGRhYWJmZTBiMDIxNWY3MGRhNjQ5ZTk0NDQ3N2Y0NDM3MGNhNjk1MiJ9fX0=",
        "YTNmMTcyZDI5Y2Y5NGJjODk1NjA4YjdhNWRjMmFmMGRlNDljNzg4ZDViZWNiMTYwNWYxZjUzNDg4YTAxNzBiOCJ9fX0=",
        "Charged Creeper:c-powered:ZjJjZWIzOWRkNGRlMjRhN2FkZmUyOTFhM2EwY2ZjN2NmNGY2NDVkZTU5YjYwM2ZjZmUwNmM2YjVhMDZlMjYifX19")),
    SKELETON(Arrays.asList(
        "MzAxMjY4ZTljNDkyZGExZjBkODgyNzFjYjQ5MmE0YjMwMjM5NWY1MTVhN2JiZjc3ZjRhMjBiOTVmYzAyZWIyIn19fQ==",
        "Yjk1MDc4ZDNiM2IxNzAxZDQ1NzI5ZDNhMTQyMjQ2N2IyOWRiYjJlMWE5MTI4MTMzYTJmMTYzZWJlODVkMmRiOSJ9fX0=")),
    SPIDER(Arrays.asList(
        "Y2Q1NDE1NDFkYWFmZjUwODk2Y2QyNThiZGJkZDRjZjgwYzNiYTgxNjczNTcyNjA3OGJmZTM5MzkyN2U1N2YxIn19fQ==")),
    GIANT(Arrays.asList(
        "ZWM0NTViMzgzNjhkYWU3MzBlZjE0ODNjMWRmMjVjZDg3YjQxYmVlNDQxZWYzYWIxZjNjNjBmMjFiZmUwZTUxMSJ9fX0=")),
    ZOMBIE(Arrays.asList(
        "NTZmYzg1NGJiODRjZjRiNzY5NzI5Nzk3M2UwMmI3OWJjMTA2OTg0NjBiNTFhNjM5YzYwZTVlNDE3NzM0ZTExIn19fQ==",
        "MzExZGQ5MWVlNGQzMWRkZDU5MWQyODMyZWExZWMwODBmMmVkZWQzM2FiODllZTFkYjhiMDRiMjZhNjhhIn19fQ==")),
    SLIME(Arrays.asList(
        "MTZhZDIwZmMyZDU3OWJlMjUwZDNkYjY1OWM4MzJkYTJiNDc4YTczYTY5OGI3ZWExMGQxOGM5MTYyZTRkOWI1In19fQ==")),
    GHAST(Arrays.asList(
        "OGI2YTcyMTM4ZDY5ZmJiZDJmZWEzZmEyNTFjYWJkODcxNTJlNGYxYzk3ZTVmOTg2YmY2ODU1NzFkYjNjYzAifX19")),
    PIG_ZOMBIE("ZOMBIE_PIGMAN", Arrays.asList(
        "NzRlOWM2ZTk4NTgyZmZkOGZmOGZlYjMzMjJjZDE4NDljNDNmYjE2YjE1OGFiYjExY2E3YjQyZWRhNzc0M2ViIn19fQ==")),
    ENDERMAN(Arrays.asList(
        "N2E1OWJiMGE3YTMyOTY1YjNkOTBkOGVhZmE4OTlkMTgzNWY0MjQ1MDllYWRkNGU2YjcwOWFkYTUwYjljZiJ9fX0=")),
    CAVE_SPIDER(Arrays.asList(
        "NDE2NDVkZmQ3N2QwOTkyMzEwN2IzNDk2ZTk0ZWViNWMzMDMyOWY5N2VmYzk2ZWQ3NmUyMjZlOTgyMjQifX19")),
    SILVERFISH(Arrays.asList(
        "ZGE5MWRhYjgzOTFhZjVmZGE1NGFjZDJjMGIxOGZiZDgxOWI4NjVlMWE4ZjFkNjIzODEzZmE3NjFlOTI0NTQwIn19fQ==")),
    BLAZE(Arrays.asList(
        "Yjc4ZWYyZTRjZjJjNDFhMmQxNGJmZGU5Y2FmZjEwMjE5ZjViMWJmNWIzNWE0OWViNTFjNjQ2Nzg4MmNiNWYwIn19fQ==")),
    MAGMA_CUBE(Arrays.asList(
        "Mzg5NTdkNTAyM2M5MzdjNGM0MWFhMjQxMmQ0MzQxMGJkYTIzY2Y3OWE5ZjZhYjM2Yjc2ZmVmMmQ3YzQyOSJ9fX0=")),
    ENDER_DRAGON(Arrays.asList(
        "ZmZjZGFlNTg2YjUyNDAzYjkyYjE4NTdlZTQzMzFiYWM2MzZhZjA4YmFiOTJiYTU3NTBhNTRhODMzMzFhNjM1MyJ9fX0=")),
    WITHER(Arrays.asList(
        "ZGRhZmIyM2VmYzU3ZjI1MTg3OGU1MzI4ZDExY2IwZWVmODdiNzljODdiMjU0YTdlYzcyMjk2ZjkzNjNlZjdjIn19fQ==",
        "M2U0ZjQ5NTM1YTI3NmFhY2M0ZGM4NDEzM2JmZTgxYmU1ZjJhNDc5OWE0YzA0ZDlhNGRkYjcyZDgxOWVjMmIyYiJ9fX0=",
        "OTY0ZTFjM2UzMTVjOGQ4ZmZmYzM3OTg1YjY2ODFjNWJkMTZhNmY5N2ZmZDA3MTk5ZThhMDVlZmJlZjEwMzc5MyJ9fX0=",
        "Y2RmNzRlMzIzZWQ0MTQzNjk2NWY1YzU3ZGRmMjgxNWQ1MzMyZmU5OTllNjhmYmI5ZDZjZjVjOGJkNDEzOWYifX19",
        "YTQzNTE2NGMwNWNlYTI5OWEzZjAxNmJiYmVkMDU3MDZlYmI3MjBkYWM5MTJjZTQzNTFjMjI5NjYyNmFlY2Q5YSJ9fX0=")),
    BAT(Arrays.asList(
        "NGNmMWIzYjNmNTM5ZDJmNjNjMTcyZTk0Y2FjZmFhMzkxZThiMzg1Y2RkNjMzZjNiOTkxYzc0ZTQ0YjI4In19fQ==")),
    WITCH(Arrays.asList(
        "ODllOGI1ZjE1YTliMjlhMWUzODljOTUyMThmZDM3OTVmMzI4NzJlNWFlZTk0NjRhNzY0OTVjNTI3ZDIyNDUifX19")),
    ENDERMITE(Arrays.asList(
        "ODRhYWZmYTRjMDllMmVhZmI4NWQzNTIyMTIyZGIwYWE0NTg3NGJlYTRlM2Y1ZTc1NjZiNGQxNjZjN2RmOCJ9fX0=")),
    GUARDIAN(Arrays.asList(
        "ZGZiNjc1Y2I1YTdlM2ZkMjVlMjlkYTgyNThmMjRmYzAyMGIzZmE5NTAzNjJiOGJjOGViMjUyZTU2ZTc0In19fQ==")),
    SHULKER(Arrays.asList(
        "MWU3MzgzMmUyNzJmODg0NGM0NzY4NDZiYzQyNGEzNDMyZmI2OThjNThlNmVmMmE5ODcxYzdkMjlhZWVhNyJ9fX0=")),
    PIG(Arrays.asList(
        "NjIxNjY4ZWY3Y2I3OWRkOWMyMmNlM2QxZjNmNGNiNmUyNTU5ODkzYjZkZjRhNDY5NTE0ZTY2N2MxNmFhNCJ9fX0=",
        "YzNhYmQ0NGFlNjdkOWM5MjU0ZDE3N2U5NjU4ZGE4NDg0MzM4OWQ1ZTFmZmQyYWYxZmI1MTIxN2M3NWMyOTgifX19")),
    SHEEP(Arrays.asList(
        "Yellow Sheep:c-yellow:MjZhNDExMmRmMWU0YmNlMmE1ZTI4NDE3ZjNhYWZmNzljZDY2ZTg4NWMzNzI0NTU0MTAyY2VmOGViOCJ9fX0=",
        "White Sheep:c-white:ZjMxZjljY2M2YjNlMzJlY2YxM2I4YTExYWMyOWNkMzNkMThjOTVmYzczZGI4YTY2YzVkNjU3Y2NiOGJlNzAifX19",
        "Light Gray Sheep:c-lightgray:Y2UxYWM2ODM5OTNiZTM1NTEyZTFiZTMxZDFmNGY5OGU1ODNlZGIxNjU4YTllMjExOTJjOWIyM2I1Y2NjZGMzIn19fQ==",
        "Red Sheep:c-red:ODM5YWY0NzdlYjYyNzgxNWY3MjNhNTY2MjU1NmVjOWRmY2JhYjVkNDk0ZDMzOGJkMjE0MjMyZjIzZTQ0NiJ9fX0=",
        "Purple Sheep:c-purple:YWU1Mjg2N2FmZWYzOGJiMTRhMjZkMTQyNmM4YzBmMTE2YWQzNDc2MWFjZDkyZTdhYWUyYzgxOWEwZDU1Yjg1In19fQ==",
        "Pink Sheep:c-pink:MmFjNzRhMmI5YjkxNDUyZTU2ZmExZGRhNWRiODEwNzc4NTZlNDlmMjdjNmUyZGUxZTg0MWU1Yzk1YTZmYzVhYiJ9fX0=",
        "Orange Sheep:c-orange:ZjA5ODM5N2EyNzBiNGMzZDJiMWU1NzRiOGNmZDNjYzRlYTM0MDkwNjZjZWZlMzFlYTk5MzYzM2M5ZDU3NiJ9fX0=",
        "Magenta Sheep:c-magenta:MTgzNjU2NWM3ODk3ZDQ5YTcxYmMxODk4NmQxZWE2NTYxMzIxYTBiYmY3MTFkNDFhNTZjZTNiYjJjMjE3ZTdhIn19fQ==",
        "Lime Sheep:c-lime:OTJhMjQ0OGY1OGE0OTEzMzI0MzRlODVjNDVkNzg2ZDg3NDM5N2U4MzBhM2E3ODk0ZTZkOTI2OTljNDJiMzAifX19",
        "Cyan Sheep:c-cyan:MWM4YTk3YTM4ODU2NTE0YTE2NDEzZTJjOTk1MjEyODlmNGM1MzYzZGM4MmZjOWIyODM0Y2ZlZGFjNzhiODkifX19",
        "Green Sheep:c-green:YTAxNzIxNWM3ZjhkYjgyMDQwYWEyYzQ3Mjk4YjY2NTQxYzJlYjVmN2Y5MzA0MGE1ZDIzZDg4ZjA2ODdkNGIzNCJ9fX0=",
        "Gray Sheep:c-gray:NDI4N2ViNTAxMzkxZjI3NTM4OWYxNjZlYzlmZWJlYTc1ZWM0YWU5NTFiODhiMzhjYWU4N2RmN2UyNGY0YyJ9fX0=",
        "Light Blue Sheeep:c-lightblue:NDZmNmM3ZTdmZDUxNGNlMGFjYzY4NTkzMjI5ZTQwZmNjNDM1MmI4NDE2NDZlNGYwZWJjY2NiMGNlMjNkMTYifX19",
        "Brown Sheep:c-brown:YTU1YWQ2ZTVkYjU2OTJkODdmNTE1MTFmNGUwOWIzOWZmOWNjYjNkZTdiNDgxOWE3Mzc4ZmNlODU1M2I4In19fQ==",
        "Blue Sheep:c-blue:ZDllYzIyODE4ZDFmYmZjODE2N2ZiZTM2NzI4YjI4MjQwZTM0ZTE2NDY5YTI5MjlkMDNmZGY1MTFiZjJjYTEifX19",
        "Black Sheep:c-black:MzI2NTIwODNmMjhlZDFiNjFmOWI5NjVkZjFhYmYwMTBmMjM0NjgxYzIxNDM1OTUxYzY3ZDg4MzY0NzQ5ODIyIn19fQ==")),
    COW(Arrays.asList(
        "NWQ2YzZlZGE5NDJmN2Y1ZjcxYzMxNjFjNzMwNmY0YWVkMzA3ZDgyODk1ZjlkMmIwN2FiNDUyNTcxOGVkYzUifX19",
        "YzVhOWNkNThkNGM2N2JjY2M4ZmIxZjVmNzU2YTJkMzgxYzlmZmFjMjkyNGI3ZjRjYjcxYWE5ZmExM2ZiNWMifX19")),
    CHICKEN(Arrays.asList(
        "MTYzODQ2OWE1OTljZWVmNzIwNzUzNzYwMzI0OGE5YWIxMWZmNTkxZmQzNzhiZWE0NzM1YjM0NmE3ZmFlODkzIn19fQ==")),
    SQUID(Arrays.asList(
        "MDE0MzNiZTI0MjM2NmFmMTI2ZGE0MzRiODczNWRmMWViNWIzY2IyY2VkZTM5MTQ1OTc0ZTljNDgzNjA3YmFjIn19fQ==")),
    WOLF(Arrays.asList(
        "NjlkMWQzMTEzZWM0M2FjMjk2MWRkNTlmMjgxNzVmYjQ3MTg4NzNjNmM0NDhkZmNhODcyMjMxN2Q2NyJ9fX0=",
        "Angry Wolf:c-angry:ZTk1Y2JiNGY3NWVhODc2MTdmMmY3MTNjNmQ0OWRhYzMyMDliYTFiZDRiOTM2OTY1NGIxNDU5ZWExNTMxNyJ9fX0=")),
    MUSHROOM_COW("MOOSHROOM", Arrays.asList(
        "ZDBiYzYxYjk3NTdhN2I4M2UwM2NkMjUwN2EyMTU3OTEzYzJjZjAxNmU3YzA5NmE0ZDZjZjFmZTFiOGRiIn19fQ==")),
    SNOW_GOLEM("SNOWMAN", Arrays.asList(
        "MWZkZmQxZjc1MzhjMDQwMjU4YmU3YTkxNDQ2ZGE4OWVkODQ1Y2M1ZWY3MjhlYjVlNjkwNTQzMzc4ZmNmNCJ9fX0=")),
    OCELOT("CAT", Arrays.asList(
        "YWI4ODFjMzliM2FmZGNjNzlmOTFmZTVkZTNjZGQwMTViYzMzNTM4NDNmNTkxZjRkMjNjZDMwMjdkZTRlNiJ9fX0=",
        "YTc1NWU3ZGYwNGQxOGIzMWQ2M2MxN2Y0YTdiNGM3MzkyNGJkNjI2NWRhNjllMTEzZWRkZDk3NTE2ZmM3In19fQ==",
        "ZjJhNjYyZjJhZTdkZWJlZTY1MjkyYzJiZjQyZmJiMDliOTdiMmZmYmRiMjcwNTIwYzJkYjk2ZTUxZDg5NDUifX19",
        "NTY1N2NkNWMyOTg5ZmY5NzU3MGZlYzRkZGNkYzY5MjZhNjhhMzM5MzI1MGMxYmUxZjBiMTE0YTFkYjEifX19")),
    IRON_GOLEM(Arrays.asList(
        "ODkwOTFkNzllYTBmNTllZjdlZjk0ZDdiYmE2ZTVmMTdmMmY3ZDQ1NzJjNDRmOTBmNzZjNDgxOWE3MTQifX19")),
    HORSE(Arrays.asList(
        "NjE5MDI4OTgzMDg3MzBjNDc0NzI5OWNiNWE1ZGE5YzI1ODM4YjFkMDU5ZmU0NmZjMzY4OTZmZWU2NjI3MjkifX19")),
    RABBIT(Arrays.asList(
        "Y2I4Y2ZmNGIxNWI4Y2EzN2UyNTc1MGYzNDU3MThmMjg5Y2IyMmM1YjNhZDIyNjI3YTcxMjIzZmFjY2MifX19",
        "NzJjNTgxMTZhMTQ3ZDFhOWEyNjI2OTIyNGE4YmUxODRmZThlNWYzZjNkZjliNjE3NTEzNjlhZDg3MzgyZWM5In19fQ==",
        "Yzk3N2EzMjY2YmYzYjllYWYxN2U1YTAyZWE1ZmJiNDY4MDExNTk4NjNkZDI4OGI5M2U2YzEyYzljYiJ9fX0=")),
    POLAR_BEAR(Arrays.asList(
        "ZDQ2ZDIzZjA0ODQ2MzY5ZmEyYTM3MDJjMTBmNzU5MTAxYWY3YmZlODQxOTk2NjQyOTUzM2NkODFhMTFkMmIifX19")),
    LLAMA(Arrays.asList(
        "ODAyNzdlNmIzZDlmNzgxOWVmYzdkYTRiNDI3NDVmN2FiOWE2M2JhOGYzNmQ2Yjg0YTdhMjUwYzZkMWEzNThlYiJ9fX0=",
        "Y2YyNGU1NmZkOWZmZDcxMzNkYTZkMWYzZTJmNDU1OTUyYjFkYTQ2MjY4NmY3NTNjNTk3ZWU4MjI5OWEifX19",
        "YzJiMWVjZmY3N2ZmZTNiNTAzYzMwYTU0OGViMjNhMWEwOGZhMjZmZDY3Y2RmZjM4OTg1NWQ3NDkyMTM2OCJ9fX0=",
        "NGQ2N2ZkNGJmZjI5MzI2OWNiOTA4OTc0ZGNhODNjMzM0ODVlNDM1ZWQ1YThlMWRiZDY1MjFjNjE2ODcxNDAifX19")),
    LLAMA_SPIT(),
    PARROT(Arrays.asList(
        "YTRiYThkNjZmZWNiMTk5MmU5NGI4Njg3ZDZhYjRhNTMyMGFiNzU5NGFjMTk0YTI2MTVlZDRkZjgxOGVkYmMzIn19fQ==",
        "Yjc4ZTFjNWY0OGE3ZTEyYjI2Mjg1MzU3MWVmMWY1OTdhOTJlZjU4ZGE4ZmFhZmUwN2JiN2MwZTY5ZTkzIn19fQ==",
        "YWI5YTM2YzU1ODlmM2EyZTU5YzFjYWE5YjNiODhmYWRhNzY3MzJiZGI0YTc5MjYzODhhOGMwODhiYmJjYiJ9fX0=",
        "M2Q2ZjRhMjFlMGQ2MmFmODI0Zjg3MDhhYzYzNDEwZjFhMDFiYmI0MWQ3ZjRhNzAyZDk0NjljNjExMzIyMiJ9fX0=",
        "MmI5NGYyMzZjNGE2NDJlYjJiY2RjMzU4OWI5YzNjNGEwYjViZDVkZjljZDVkNjhmMzdmOGM4M2Y4ZTNmMSJ9fX0=",
        "ZGFjNjcwM2RlZDQ2ZDkzOWE2MjBmZTIyYzQzZTE4Njc0ZTEzZDIzYzk3NDRiZTAzNmIzNDgzYzFkMWNkZCJ9fX0=",
        "ZjBiZmE4NTBmNWRlNGIyOTgxY2NlNzhmNTJmYzJjYzdjZDdiNWM2MmNhZWZlZGRlYjljZjMxMWU4M2Q5MDk3In19fQ==",
        "ZjhhODJjOGI3NWRkMWMyY2U4MTMzYzBiYTkzOWI4YzUyZTQ3ZDNlYzM3NDk1MGY0N2RkZGJiZTM0NWUyMCJ9fX0=",
        "YWNhNTgwYjA1MWM2M2JlMjlkYTU0NWE5YWE3ZmY3ZTEzNmRmNzdhODFjNjdkYzFlZTllNjE3MGMxNGZiMzEwIn19fQ==")),
    VILLAGER(Arrays.asList(
        "ODIyZDhlNzUxYzhmMmZkNGM4OTQyYzQ0YmRiMmY1Y2E0ZDhhZThlNTc1ZWQzZWIzNGMxOGE4NmU5M2IifX19")),
    ENDER_CRYSTAL("END_CRYSTAL"),
    TURTLE(Arrays.asList(
        "MGE0MDUwZTdhYWNjNDUzOTIwMjY1OGZkYzMzOWRkMTgyZDdlMzIyZjlmYmNjNGQ1Zjk5YjU3MThhIn19fQ==")),
    PHANTOM(Arrays.asList(
        "NDExZDI1YmNkYWJhZmFkNWZkNmUwMTBjNWIxY2Y3YTAwYzljY2E0MGM1YTQ2NzQ3ZjcwNmRjOWNiM2EifX19",
        "YWQyZmE1NjE4NDQ3NzYyZTI2MTExZTA2MGRjNTkzZWE2MjJkNWRkZmMzODVkN2U0MjUzMmU0NjMyN2Y4MDdjMCJ9fX0=")),
    TRIDENT(),
    COD(Arrays.asList(
        "NmY5OWI1ODBkNDVhNzg0ZTdhOTY0ZTdkM2IxZjk3Y2VjZTc0OTExMTczYmQyMWMxZDdjNTZhY2RjMzg1ZWQ1In19fQ==")),
    SALMON(Arrays.asList(
        "YWRmYzU3ZDA5MDU5ZTQ3OTlmYTkyYzE1ZTI4NTEyYmNmYWExMzE1NTc3ZmUzYTI3YWVkMzg5ZTRmNzUyMjg5YSJ9fX0=")),
    PUFFERFISH(Arrays.asList(
        "YTk1NTkzODg5OTNmZTc4MmY2N2JkNThkOThjNGRmNTZiY2Q0MzBlZGNiMmY2NmVmNTc3N2E3M2MyN2RlMyJ9fX0=")),
    TROPICAL_FISH(Arrays.asList(
        "MzZkMTQ5ZTRkNDk5OTI5NjcyZTI3Njg5NDllNjQ3Nzk1OWMyMWU2NTI1NDYxM2IzMjdiNTM4ZGYxZTRkZiJ9fX0=")),
    DROWNED(Arrays.asList(
        "YzNmN2NjZjYxZGJjM2Y5ZmU5YTYzMzNjZGUwYzBlMTQzOTllYjJlZWE3MWQzNGNmMjIzYjNhY2UyMjA1MSJ9fX0=",
        "MWY4YmFhNDhiOGY1MTE5OTBlNDdkYjk2ODMyNGMxNTJiZDExNjc3MzFkZGYwMzQ1MzAwNDQ3MzVhNmJkMmVkNCJ9fX0=",
        "YzFhNzMyNTI0MDFhNmU5NDZmNjFkYmFjMGUwMjdkMTgzZTBhY2U1ODc1MmZhMTVhNjRkMjQ0OWZjZjUwODdiNyJ9fX0=",
        "Yzg0ZGY3OWM0OTEwNGIxOThjZGFkNmQ5OWZkMGQwYmNmMTUzMWM5MmQ0YWI2MjY5ZTQwYjdkM2NiYmI4ZTk4YyJ9fX0=",
        "ZmIxNTMxYzA0ZTI1ZDdmYTY0NTc2OTgyNjg0OTFjYjg5NmQzMzAyZDI2ODg0ZmNmZGYxYTBiMmY5MmQ3N2M4ZiJ9fX0=",
        "NTZkYWY1MGVhZjc2YzNhNmQ1YWQzOWM5NjZmMjk4NzdiOTFkOTUwZGQxZTM3MTIyZTljODE5NTg1Yzg5ZDkyZSJ9fX0=")),
    DOLPHIN(Arrays.asList(
        "OGU5Njg4Yjk1MGQ4ODBiNTViN2FhMmNmY2Q3NmU1YTBmYTk0YWFjNmQxNmY3OGU4MzNmNzQ0M2VhMjlmZWQzIn19fQ==")),
    LINGERING_POTION(),
    FISHING_HOOK(),
    LIGHTNING(),
    WEATHER(),
    PLAYER(),
    COMPLEX_PART(),
    TIPPED_ARROW(),

    PANDA(Arrays.asList(
        "ZDE4OGM5ODBhYWNmYTk0Y2YzMzA4ODUxMmIxYjk1MTdiYTgyNmIxNTRkNGNhZmMyNjJhZmY2OTc3YmU4YSJ9fX0=")),
    PILLAGER(Arrays.asList(
        "NGFlZTZiYjM3Y2JmYzkyYjBkODZkYjVhZGE0NzkwYzY0ZmY0NDY4ZDY4Yjg0OTQyZmRlMDQ0MDVlOGVmNTMzMyJ9fX0=")),
    RAVAGER(Arrays.asList(
        "MWNiOWYxMzlmOTQ4OWQ4NmU0MTBhMDZkOGNiYzY3MGM4MDI4MTM3NTA4ZTNlNGJlZjYxMmZlMzJlZGQ2MDE5MyJ9fX0=",
        "M2I2MjUwMWNkMWI4N2IzN2Y2MjgwMTgyMTBlYzU0MDBjYjY1YTRkMWFhYjc0ZTZhM2Y3ZjYyYWE4NWRiOTdlZSJ9fX0=")),
    TRADER_LLAMA(Arrays.asList(
        "ODQyNDc4MGIzYzVjNTM1MWNmNDlmYjViZjQxZmNiMjg5NDkxZGY2YzQzMDY4M2M4NGQ3ODQ2MTg4ZGI0Zjg0ZCJ9fX0=",
        "NzA4N2E1NTZkNGZmYTk1ZWNkMjg0NGYzNTBkYzQzZTI1NGU1ZDUzNWZhNTk2ZjU0MGQ3ZTc3ZmE2N2RmNDY5NiJ9fX0=",
        "YmU0ZDhhMGJjMTVmMjM5OTIxZWZkOGJlMzQ4MGJhNzdhOThlZTdkOWNlMDA3MjhjMGQ3MzNmMGEyZDYxNGQxNiJ9fX0=")),
    WANDERING_TRADER(Arrays.asList(
        "NWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0=")),
    FOX(Arrays.asList(
        "YjZmZWI3NjFiMmY1OWZhYmU1Y2MzY2M4MmE5MzRiNTM0ZWE5OWVkYjkxMzJjY2RhOWY0ODRiZDU5ODZkNyJ9fX0=",
        "MjRhMDM0NzQzNjQzNGViMTNkNTM3YjllYjZiNDViNmVmNGM1YTc4Zjg2ZTkxODYzZWY2MWQyYjhhNTNiODIifX19",
        "MTZkYjdkNTA3Mzg5YTE0YmJlYzM5ZGU2OTIyMTY1YjMyZDQzNjU3YmNiNmFhZjRiNTE4MjgyNWIyMmI0In19fQ==")),
    CAT(Arrays.asList(
        "N2M5Yjc0MDllN2I1MzgzYzE5YjM2MmIyYTBjYjQzZDUwOTNiMTNlMmIyMzRlOGExODkxNTYzZTU1ZWFlOWQ2OCJ9fX0=",
        "NTg4MDNmMDI3MGY4Y2RmNGUwZmU5NzMyZDQ5NjdjY2NjMGEyZjRmY2QxMThjZDE1MDAwOTc5YjE4ODg1MTQ0ZiJ9fX0=")),
    BEE(Arrays.asList(
        "OTQ3MzIyZjgzMWUzYzE2OGNmYmQzZTI4ZmU5MjUxNDRiMjYxZTc5ZWIzOWM3NzEzNDlmYWM1NWE4MTI2NDczIn19fQ==",
        "OTlkYzNmMDBlY2FiMjI0OWJiNmExNmM4YzUxMTVjZWI5ZjIzMjA1YTBkNTVjYzBlOWJhYmQyNTYyZjc5NTljNCJ9fX0==",
        "ZTZiNzRlMDUyYjc0Mjg4Nzk5YmE2ZDlmMzVjNWQwMjIxY2Y4YjA0MzMxNTQ3ZWMyZjY4ZDczNTk3YWUyYzliIn19fQ==",
        "YmIxNzc3NDY2MjUxMmQ3ODdlZjc3YjFhNDZhMDRlMmM2ZmQ2Nzc5NGJmN2Y3Nzk1NjZlYjIxYzgxNDNhYWQ5ZSJ9fX0=")),

    ZOMBIFIED_PIGLIN(Arrays.asList("N2VhYmFlY2M1ZmFlNWE4YTQ5Yzg4NjNmZjQ4MzFhYWEyODQxOThmMWEyMzk4ODkwYzc2NWUwYThkZTE4ZGE4YyJ9fX0=")),
    HOGLIN(Arrays.asList("OWJiOWJjMGYwMWRiZDc2MmEwOGQ5ZTc3YzA4MDY5ZWQ3Yzk1MzY0YWEzMGNhMTA3MjIwODU2MWI3MzBlOGQ3NSJ9fX0=")),
    PIGLIN(Arrays.asList("OWYxODEwN2QyNzVmMWNiM2E5Zjk3M2U1OTI4ZDU4NzlmYTQwMzI4ZmYzMjU4MDU0ZGI2ZGQzZTdjMGNhNjMzMCJ9fX0=")),
    STRIDER(Arrays.asList("MThhOWFkZjc4MGVjN2RkNDYyNWM5YzA3NzkwNTJlNmExNWE0NTE4NjY2MjM1MTFlNGM4MmU5NjU1NzE0YjNjMSJ9fX0=")),
    ZOGLIN(Arrays.asList("ZTY3ZTE4NjAyZTAzMDM1YWQ2ODk2N2NlMDkwMjM1ZDg5OTY2NjNmYjllYTQ3NTc4ZDNhN2ViYmM0MmE1Y2NmOSJ9fX0=")),

    // 1.16.2
    PIGLIN_BRUTE(Arrays.asList("M2UzMDBlOTAyNzM0OWM0OTA3NDk3NDM4YmFjMjllM2E0Yzg3YTg0OGM1MGIzNGMyMTI0MjcyN2I1N2Y0ZTFjZiJ9fX0=")),

    // 1.17.0
    AXOLOTL(Arrays.asList("ZThhOGEyZDdjY2YwYzM3NDZlMjNhYjU0OTEwNzBlMDkyM2YwNWIyMzVmOWEyZjVkNTNkMzg0MzUzODUzYmRkYyJ9fX0=")),
    GLOW_SQUID(Arrays.asList("MmVjZDBiNWViNmIzODRkYjA3NmQ4NDQ2MDY1MjAyOTU5ZGRkZmYwMTYxZTBkNzIzYjNkZjBjYzU4NmQxNmJiZCJ9fX0=")),
    GOAT(Arrays.asList("M2E0OTg2ZmRmMDRjYzg2ZDhhZmYyMDM3YTZiNGRmNTczY2RjMWU3MDhkMDU3OTBjNzY3MjhmYWVmNzk2ZjMzYSJ9fX0=")),
    GLOW_ITEM_FRAME(),

    // 1.19.0
    ALLAY(Arrays.asList("YmVlYTg0NWNjMGI1OGZmNzYzZGVjZmZlMTFjZDFjODQ1YzVkMDljM2IwNGZlODBiMDY2M2RhNWM3YzY5OWViMyJ9fX0==")),
    CHEST_BOAT(),
    FROG(Arrays.asList("ZDBlZGNiODk4MzU2ZmQ0MDBjMjA1YzYxMzRlMDk4NjEwYmFmYmJjNTA0MTc3MjlhMTU1Y2U3N2Q3YmNhOThkNCJ9fX0=")),
    TADPOLE(Arrays.asList("YjIzZWJmMjZiN2E0NDFlMTBhODZmYjVjMmE1ZjNiNTE5MjU4YTVjNWRkZGQ2YTFhNzU1NDlmNTE3MzMyODE1YiJ9fX0=")),
    WARDEN(Arrays.asList("NmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=")),

    // 1.19.4
    BLOCK_DISPLAY(),
    TEXT_DISPLAY(),
    ITEM_DISPLAY(),
    INTERACTION(),
    MARKER(),

    // 1.20
    CAMEL(Arrays.asList("NzRiOGEzMzNkZmE5MmU3ZTVhOTVhZDRhZTJkODRiMWJhZmEzM2RjMjhjMDU0OTI1Mjc3ZjYwZTc5ZGFmYzhjNCJ9fX0=")),
    SNIFFER(Arrays.asList("ODdhZDkyMGE2NmUzOGNjMzQyNmE1YmZmMDg0NjY3ZTg3NzIxMTY5MTVlMjk4MDk4NTY3YzEzOWYyMjJlMmM0MiJ9fX0=")),

    // 1.20.5
    ARMADILLO(Arrays.asList("OTE2NGVkMGUwZWY2OWIwY2U3ODE1ZTQzMDBiNDQxM2E0ODI4ZmNiMDA5MjkxODU0MzU0NWE0MThhNDhlMGMzYyJ9fX0=")),

    //1.21
    BREEZE(Arrays.asList("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI3NTcyOGFmN2U2YTI5Yzg4MTI1YjY3NWEzOWQ4OGFlOTkxOWJiNjFmZGMyMDAzMzdmZWQ2YWIwYzQ5ZDY1YyJ9fX0=")),
    WIND_CHARGE(),
    BREEZE_WIND_CHARGE(),
    BOGGED(Arrays.asList("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTNiOTAwM2JhMmQwNTU2MmM3NTExOWI4YTYyMTg1YzY3MTMwZTkyODJmN2FjYmFjNGJjMjgyNGMyMWViOTVkOSJ9fX0=")),
    OMINOUS_ITEM_SPAWNER(),
    FISHING_BOBBER(),

    // if possible we can remove this string for each texture to save up some space
    // eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv
    UNKNOWN();

    private String name;
    private List<String> secondaryNames = new ArrayList<>();
    EntityType type = null;
    private HashMap<String, MobHeadInfo> headTextures = new HashMap<String, MobHeadInfo>();
    public static HashMap<String, ItemStack> cache = new HashMap<String, ItemStack>();
    static HashMap<String, CMIEntityType> byName = new HashMap<String, CMIEntityType>();
    static HashMap<EntityType, CMIEntityType> byType = new HashMap<EntityType, CMIEntityType>();
    static HashMap<String, CMIEntityType> byTexture = new HashMap<String, CMIEntityType>();

    static {
        for (CMIEntityType one : CMIEntityType.values()) {
            byName.put(one.toString().replace("_", "").toLowerCase(), one);
            byName.put(one.getName().replace("_", "").replace(" ", "").toLowerCase(), one);

            if (one.secondaryNames != null) {
                for (String secondaryName : one.secondaryNames) {
                    byName.put(secondaryName.replace("_", "").replace(" ", "").toLowerCase(), one);
                }
            }

            for (String texture : one.getHeadTextures()) {
                byTexture.put(texture, one);
            }

            if (one.getType() != null)
                byType.put(one.getType(), one);
        }
    }

    CMIEntityType() {
        this(new ArrayList<String>(), new ArrayList<String>());
    }

    CMIEntityType(List<String> headTextures) {
        this(new ArrayList<String>(), headTextures);
    }

    CMIEntityType(String secondaryName, List<String> headTextures) {
        this(Arrays.asList(secondaryName), headTextures);
    }

    CMIEntityType(String secondaryName) {
        this(Arrays.asList(secondaryName), new ArrayList<String>());
    }

    CMIEntityType(String name, String secondaryName) {
        this(Arrays.asList(name, secondaryName), new ArrayList<String>());
    }

    CMIEntityType(List<String> secondaryNames, List<String> headTextures) {
        this.name = CMIText.everyFirstToUpperCase(this.toString());
        this.secondaryNames = secondaryNames == null ? new ArrayList<>() : secondaryNames;
        for (String one : headTextures) {
            String text = one;
//	    if (text.length() < 150)
//		text = "" + text;

            MobHeadInfo mobHed = new MobHeadInfo();

            if (text.contains(":")) {
                String[] split = text.split(":");
                text = split[split.length - 1];

                for (int i = 0; i < split.length - 1; i++) {
                    String oneOp = split[i];

                    if (oneOp.startsWith("c-")) {
                        for (String oneCrit : oneOp.substring("c-".length()).split(",")) {
                            CMIEntitySubType criteria = CMIEntitySubType.getByName(oneCrit);
                            if (criteria != null)
                                mobHed.addCriterias(criteria);
                        }
                    } else {
                        String customName = null;
                        customName = oneOp;
                        customName = customName.equalsIgnoreCase("Default") ? null : customName;
                        mobHed.setCustomName(customName);
                    }

                }

            }

            this.headTextures.put(text, mobHed);
        }
    }

    @Deprecated
    public int getId() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public String getTranslatedName() {
        if (!CMILib.getInstance().getLM().isString("info.EntityType." + toString().toLowerCase()))
            return getName();
        return CMILib.getInstance().getLM().getMessage("info.EntityType." + toString().toLowerCase());
    }

    public List<String> getHeadTextures() {
        List<String> t = new ArrayList<String>();
        for (Entry<String, MobHeadInfo> one : headTextures.entrySet()) {
            t.add(one.getKey());
        }
        return t;
    }

    public HashMap<String, MobHeadInfo> getHeadTexturesMap() {
        return headTextures;
    }

    public ItemStack getHead(Integer pos) {
        if (pos == null || pos < 1)
            return getHead();
        if (headTextures == null || headTextures.isEmpty())
            return null;

        if (headTextures.size() < pos)
            return null;

        String text = getHeadTextures().get(pos - 1);
        if (text == null || text.isEmpty())
            return null;
        return getHead(text);
    }

    public ItemStack getHead() {
        if (headTextures == null || headTextures.isEmpty())
            return null;
        List<String> t = getHeadTextures();
        Collections.shuffle(t);
        String text = t.get(0);
        if (text == null || text.isEmpty())
            return null;

        return getHead(text);
    }

    public ItemStack getHead(String texture) {
        if (texture == null || texture.isEmpty())
            return null;

        ItemStack cached = cache.get(texture);
        if (cached != null) {
            return cached.clone();
        }

        ItemStack item = CMIMaterial.PLAYER_HEAD.newItemStack();

        MobHeadInfo headInfo = headTextures.get(texture);

        item = CMILib.getInstance().getReflectionManager().setSkullTexture(item, getTranslatedName(), texture);

        if (headInfo != null && headInfo.getCustomName() != null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(LC.info_mobHeadName.get("[mobName]", headInfo.getCustomName()));
            item.setItemMeta(meta);
        } else {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(LC.info_mobHeadName.get("[mobName]", getTranslatedName()));
            item.setItemMeta(meta);
        }

//        new CMINBT(item).setString("SkullOwner.Name", getTranslatedName());

        cache.put(texture, item);
        return item.clone();
    }

    public MobHeadInfo getHeadInfo(String texture) {
        if (texture == null || texture.isEmpty())
            return null;
        return headTextures.get(texture);
    }

    @Deprecated
    public static CMIEntityType getById(int id) {
        return get(id);
    }

    public static CMIEntityType get(int id) {
        CMIEntityType ttype = getByName(String.valueOf(id));
        return ttype == null ? CMIEntityType.PIG : ttype;
    }

    @Deprecated
    public static CMIEntityType getByType(EntityType entity) {
        return get(entity);
    }

    public static CMIEntityType get(Entity entity) {
        return get(entity.getType());
    }

    public static CMIEntityType get(EntityType entity) {
        if (entity == null)
            return null;

        CMIEntityType cmiType = byType.get(entity);
        if (cmiType != null)
            return cmiType;

        return get(entity.toString());
    }

    @Deprecated
    public static CMIEntityType getByItem(ItemStack item) {
        return get(item);
    }

    public static CMIEntityType get(ItemStack item) {
        if (item == null)
            return null;

        if (CMIMaterial.isMonsterEgg(item.getType())) {
            String name = item.getType().toString().replace("_SPAWN_EGG", "");
            return get(name);
        }

        if (CMIMaterial.SPAWNER.equals(item.getType())) {
            if (item.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta bsm = (BlockStateMeta) item.getItemMeta();
                if (bsm.getBlockState() instanceof CreatureSpawner) {
                    CreatureSpawner bs = (CreatureSpawner) bsm.getBlockState();
                    return CMIEntityType.get(bs.getSpawnedType());
                }
            }
        }
        return null;
    }

    @Deprecated
    public static CMIEntityType getByName(String name) {
        return get(name);
    }

    public static CMIEntityType get(String name) {
        return byName.get(name.toLowerCase().replace("_", ""));
    }

    public EntityType getType() {
        if (type != null)
            return type;
        for (EntityType one : EntityType.values()) {
            if (one.toString().equalsIgnoreCase(this.name())) {
                type = one;
                break;
            }

            if (secondaryNames != null) {
                for (String oneName : secondaryNames) {
                    if (one.toString().equalsIgnoreCase(oneName)) {
                        type = one;
                        break;
                    }
                }
            }
        }
        return type;
    }

    public boolean isAlive() {
        return getType() == null ? false : getType().isAlive();
    }

    public boolean isSpawnable() {
        return getType() == null ? false : getType().isSpawnable();
    }

    public static String getRealNameByType(EntityType type) {
        if (type == null)
            return null;
        CMIEntityType ctype = CMIEntityType.getByType(type);
        if (ctype != null)
            return ctype.getName();
        String name = type.name();

        name = name.toLowerCase().replace("_", " ");
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public CMIMaterial getSpawnEggMaterial() {

        CMIMaterial m = CMIMaterial.get((this.equals(CMIEntityType.MUSHROOM_COW) ? "Mooshroom".toLowerCase() : this.toString().toLowerCase()) + "_spawn_egg");

        if (m != null && m.isMonsterEgg())
            return m;

        return null;
    }

    public void setHeadTextures(HashMap<String, MobHeadInfo> headTextures) {
        this.headTextures = headTextures;
        for (Entry<String, MobHeadInfo> one : headTextures.entrySet()) {
            String texture = one.getKey();
            if (texture.startsWith("eyJ0ZXh0dXJlcyI6ey"))
                texture = texture.substring("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv".length());
            byTexture.put(texture, this);
        }
    }

    public static CMIEntityType getByTexture(String texture) {
        if (texture == null)
            return null;
        if (texture.startsWith("eyJ0ZXh0dXJlcyI6ey"))
            texture = texture.substring("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv".length());
        return byTexture.get(texture);
    }
}
