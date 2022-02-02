package io.github.changwook987.pvp.command

import io.github.changwook987.pvp.invfx.Enchant
import io.github.monun.invfx.openFrame
import io.github.monun.kommand.PluginKommand
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

object PvpCommand {
    fun register(plugin: JavaPlugin, kommand: PluginKommand) {
        val logger = plugin.logger
        var teleport: LateTeleport? = null

        kommand.register("pvp") {
            then("goto") {
                then("world" to dimension()) {
                    executes {
                        if (isPlayer) {
                            val world: World = it["world"]

                            player.teleportWorld(world)
                        }
                    }
                    then("players" to players()) {
                        executes {
                            val world: World = it["world"]
                            val players: Collection<Player> = it["players"]

                            for (player in players) {
                                player.teleportWorld(world)
                            }
                        }
                    }
                }
            }
            then("enchant") {
                requires { isPlayer }
                executes {
                    val item = player.inventory.itemInMainHand

                    if (item.type.isCanEnchant) {
                        val allItems = (player.inventory.contents ?: return@executes).toList()

                        val enchantBooks = ArrayList<ItemStack>()

                        for (itemInInventory in allItems) {
                            if (itemInInventory != null && itemInInventory.type == Material.ENCHANTED_BOOK) {
                                enchantBooks.add(itemInInventory)
                            }
                        }

                        player.openFrame(Enchant.create(enchantBooks))
                    } else {
                        player.sendMessage("강화할 아이템을 들고 써라")
                    }
                }
            }
            then("z1전") {
                requires { isPlayer }
                executes {
                    player.inventory.apply {
                        addItem(
                            ItemStack(Material.NETHERITE_HELMET).apply { addUnsafeEnchantments(fullEnchant) },
                            ItemStack(Material.NETHERITE_CHESTPLATE).apply { addUnsafeEnchantments(fullEnchant) },
                            ItemStack(Material.NETHERITE_LEGGINGS).apply { addUnsafeEnchantments(fullEnchant) },
                            ItemStack(Material.NETHERITE_BOOTS).apply { addUnsafeEnchantments(fullEnchant) }
                        )
                    }
                }
            }
            then("timer") {
                then("world" to dimension()) {
                    then("second" to int(0)) {
                        executes {
                            if ((teleport != null) && !teleport!!.isCancelled) {
                                teleport!!.cancel()
                            }

                            teleport = LateTeleport(
                                plugin,
                                it["world"],
                                plugin.server.onlinePlayers,
                                it["second"]
                            )
                        }
                    }
                }
            }
        }
    }

    private fun Player.teleportWorld(world: World) {
        var y = world.maxHeight.toDouble()

        while (y > world.minHeight) {
            for (x in 0..15) {
                for (z in 0..15) {
                    val loc = Location(world, x.toDouble(), y, z.toDouble())

                    if (loc.block.isEmpty) {
                        loc.y -= 1
                        if (loc.block.isEmpty) {
                            loc.y -= 1
                            if (loc.block.isSolid) {
                                loc.y += 1
                                teleport(loc)
                                return
                            }
                        }
                    }
                }
            }
            y -= 1
        }
    }

    private val Material.isCanEnchant: Boolean
        get() =
            when (this) {
                //<editor-fold defaultstate="collapsed" desc="isCanEnchant">
                Material.WOODEN_AXE,
                Material.IRON_AXE,
                Material.GOLDEN_AXE,
                Material.DIAMOND_AXE,
                Material.NETHERITE_AXE,
                Material.STONE_AXE,
                Material.WOODEN_SWORD,
                Material.IRON_SWORD,
                Material.GOLDEN_SWORD,
                Material.DIAMOND_SWORD,
                Material.NETHERITE_SWORD,
                Material.STONE_SWORD,
                Material.WOODEN_PICKAXE,
                Material.IRON_PICKAXE,
                Material.GOLDEN_PICKAXE,
                Material.DIAMOND_PICKAXE,
                Material.NETHERITE_PICKAXE,
                Material.STONE_PICKAXE,
                Material.WOODEN_HOE,
                Material.IRON_HOE,
                Material.GOLDEN_HOE,
                Material.DIAMOND_HOE,
                Material.NETHERITE_HOE,
                Material.STONE_HOE,
                Material.WOODEN_SHOVEL,
                Material.IRON_SHOVEL,
                Material.GOLDEN_SHOVEL,
                Material.DIAMOND_SHOVEL,
                Material.NETHERITE_SHOVEL,
                Material.STONE_SHOVEL,
                Material.LEATHER_HELMET,
                Material.IRON_HELMET,
                Material.GOLDEN_HELMET,
                Material.DIAMOND_HELMET,
                Material.NETHERITE_HELMET,
                Material.LEATHER_CHESTPLATE,
                Material.IRON_CHESTPLATE,
                Material.GOLDEN_CHESTPLATE,
                Material.DIAMOND_CHESTPLATE,
                Material.NETHERITE_CHESTPLATE,
                Material.LEATHER_LEGGINGS,
                Material.IRON_LEGGINGS,
                Material.GOLDEN_LEGGINGS,
                Material.DIAMOND_LEGGINGS,
                Material.NETHERITE_LEGGINGS,
                Material.LEATHER_BOOTS,
                Material.IRON_BOOTS,
                Material.GOLDEN_BOOTS,
                Material.DIAMOND_BOOTS,
                Material.NETHERITE_BOOTS,
                Material.SHIELD,
                Material.CARROT_ON_A_STICK,
                Material.WARPED_FUNGUS_ON_A_STICK,
                Material.BOW,
                Material.TRIDENT,
                Material.ENCHANTED_BOOK
                    //</editor-fold>
                -> true
                else -> false
            }

    private val fullEnchant: Map<Enchantment, Int>
        get() {
            val result = emptyMap<Enchantment, Int>().toMutableMap()
            for (ench in Enchantment.values()) {
                if (!ench.isCursed) {
                    result[ench] = Short.MAX_VALUE.toInt()
                }
            }
            return result
        }

    class LateTeleport(
        plugin: JavaPlugin,
        world: World,
        players: Collection<Player>,
        sec: Int
    ) {
        private var tick: Long
        private val runnable: Runnable

        init {
            tick = sec * 20L

            runnable = Runnable(world, players, tick)
            runnable.runTaskTimer(plugin, 1L, 0L)
        }

        fun cancel() = runnable.cancel()

        val isCancelled: Boolean = runnable.isCancelled

        class Runnable(
            private val world: World,
            private val players: Collection<Player>,
            private var tick: Long
        ) : BukkitRunnable() {
            override fun run() {
                if (tick == 0L) {
                    for (player in players) {
                        player.teleportWorld(world)
                    }
                    cancel()
                } else {
                    tick--
                    for (player in players) {

                        player.sendActionBar(
                            if (tick <= 100)
                                text().content("%.2f초뒤 이동".format(tick.div(20.0)))
                                    .color(NamedTextColor.RED)
                                    .build()
                            else
                                text().content("${tick.div(20)}초뒤 이동")
                                    .color(NamedTextColor.WHITE)
                                    .build()
                        )
                    }
                }
            }
        }
    }
}