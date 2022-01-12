package io.github.changwook987.pvp

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.plugin.java.JavaPlugin

object StaticItems {
    private val luckPickaxe = ItemStack(Material.DIAMOND_PICKAXE).apply {
        addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2)
        itemMeta = (itemMeta as Damageable).apply {
            damage = 1559
        }
    }

    fun luckPickaxeRecipe(plugin: JavaPlugin): ShapedRecipe {
        return ShapedRecipe(NamespacedKey(plugin, "luck_pickaxe"), luckPickaxe).apply {
            shape("ddd", "lsl", " s ")
            setIngredient('d', Material.IRON_INGOT)
            setIngredient('l', Material.LAPIS_LAZULI)
            setIngredient('s', Material.STICK)
        }
    }

    val sharpnessBook = ItemStack(Material.ENCHANTED_BOOK).apply {
        itemMeta = (itemMeta as EnchantmentStorageMeta).apply {
            addStoredEnchant(Enchantment.DAMAGE_ALL, 3, false)
        }
    }

    fun sharpnessBookRecipe(plugin: JavaPlugin): ShapelessRecipe {
        return ShapelessRecipe(NamespacedKey(plugin, "sharpness_book"), sharpnessBook).apply {
            addIngredient(Material.BOOK)
            addIngredient(Material.IRON_SWORD)
        }
    }

    private val protectionBook = ItemStack(Material.ENCHANTED_BOOK).apply {
        itemMeta = (itemMeta as EnchantmentStorageMeta).apply {
            addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false)
        }
    }

    fun protectionBookRecipe(plugin: JavaPlugin): ShapedRecipe {
        return ShapedRecipe(NamespacedKey(plugin, "protection_book"), protectionBook).apply {
            shape(" I ", "IBI", " I ")
            setIngredient('I', Material.IRON_INGOT)
            setIngredient('B', Material.BOOK)
        }
    }

    private val fireBook = ItemStack(Material.ENCHANTED_BOOK).apply {
        itemMeta = (itemMeta as EnchantmentStorageMeta).apply {
            addStoredEnchant(Enchantment.FIRE_ASPECT, 1, false)
        }
    }

    fun fireBookRecipe(plugin: JavaPlugin): ShapelessRecipe {
        return ShapelessRecipe(NamespacedKey(plugin, "fire_book"), fireBook).apply {
            addIngredient(Material.BOOK)
            addIngredient(Material.BLAZE_POWDER)
            addIngredient(Material.BLAZE_POWDER)
        }
    }

    fun quickSmeltRecipe(material: Material, result: Material, plugin: JavaPlugin): ShapedRecipe {
        return ShapedRecipe(
            NamespacedKey(plugin, material.name.lowercase() + "_to_" + result.name.lowercase()),
            ItemStack(result, 8)
        ).apply {
            shape("mmm", "mcm", "mmm")
            setIngredient('m', material)
            setIngredient('c', Material.COAL)
        }
    }
}