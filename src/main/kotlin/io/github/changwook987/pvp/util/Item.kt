package io.github.changwook987.pvp.util

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable

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

val Material.isArmor: Boolean
    get() = when (this) {
        //<editor-fold desc="Armors">
        Material.DIAMOND_HELMET,
        Material.GOLDEN_HELMET,
        Material.IRON_HELMET,
        Material.LEATHER_HELMET,
        Material.NETHERITE_HELMET,
        Material.TURTLE_HELMET,
        Material.CHAINMAIL_HELMET,

        Material.CHAINMAIL_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE,
        Material.GOLDEN_CHESTPLATE,
        Material.IRON_CHESTPLATE,
        Material.LEATHER_CHESTPLATE,
        Material.NETHERITE_CHESTPLATE,

        Material.CHAINMAIL_LEGGINGS,
        Material.DIAMOND_LEGGINGS,
        Material.GOLDEN_LEGGINGS,
        Material.IRON_LEGGINGS,
        Material.LEATHER_LEGGINGS,
        Material.NETHERITE_LEGGINGS,

        Material.CHAINMAIL_BOOTS,
        Material.DIAMOND_BOOTS,
        Material.GOLDEN_BOOTS,
        Material.IRON_BOOTS,
        Material.LEATHER_BOOTS,
        Material.NETHERITE_BOOTS
            //</editor-fold>
        -> true
        else -> false
    }

fun ItemStack.applyDamage() {
    val im = (itemMeta ?: return) as Damageable

    val durabilityLevel = im.getEnchantLevel(Enchantment.DURABILITY)

    itemMeta = im.apply {
        val chance = if (type.isArmor) {
            60 + (100 / durabilityLevel.plus(1.0))
        } else {
            (100 / durabilityLevel.plus(1.0) / 100)
        }

        if (chance >= Math.random()) {
            damage++
        }
    }

    if (isBroken) {
        amount = 0

    }
}

val ItemStack.isBroken: Boolean
    get() {
        val im = itemMeta
        if (im !is Damageable) return false

        return im.damage >= type.maxDurability.toInt()
    }