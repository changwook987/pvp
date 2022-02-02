package io.github.changwook987.pvp.util

import org.bukkit.Material

val Material.isOre: Boolean
    get() = when (this) {
        //<editor-fold desc="Ores">
        Material.COPPER_ORE,
        Material.COAL_ORE,
        Material.GOLD_ORE,
        Material.IRON_ORE,
        Material.LAPIS_ORE,
        Material.REDSTONE_ORE,
        Material.EMERALD_ORE,
        Material.DIAMOND_ORE,
        Material.DEEPSLATE_COPPER_ORE,
        Material.DEEPSLATE_COAL_ORE,
        Material.DEEPSLATE_IRON_ORE,
        Material.DEEPSLATE_GOLD_ORE,
        Material.DEEPSLATE_LAPIS_ORE,
        Material.DEEPSLATE_REDSTONE_ORE,
        Material.DEEPSLATE_EMERALD_ORE,
        Material.DEEPSLATE_DIAMOND_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE
            /*</editor-fold>*/ -> true
        else -> false
    }
val Material.isPickaxe: Boolean
    get() = when (this) {
        //<editor-fold desc="Pickaxes">
        Material.DIAMOND_PICKAXE,
        Material.IRON_PICKAXE,
        Material.GOLDEN_PICKAXE,
        Material.NETHERITE_PICKAXE,
        Material.STONE_PICKAXE,
        Material.WOODEN_PICKAXE
            /*</editor-fold>*/ -> true
        else -> false
    }
