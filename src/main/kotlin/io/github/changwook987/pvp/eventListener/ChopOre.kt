package io.github.changwook987.pvp.eventListener

import io.github.changwook987.pvp.util.applyDamage
import io.github.changwook987.pvp.util.isOre
import io.github.changwook987.pvp.util.isPickaxe
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ChopOre(private val plugin: JavaPlugin) : Listener {

    //chop ore : 클릭 한 번에 광물이 사라진다?
    @EventHandler(ignoreCancelled = false)
    fun onBreakBlock(event: BlockBreakEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        if (item.type.isPickaxe && event.block.type.isOre) {
            event.isCancelled = true

            fun Block.getNearbyBlocks(): List<Block> {
                val i = location.x - 1
                val j = location.y - 1
                val k = location.z - 1

                return List(27) {
                    Location(
                        world,
                        it % 3 + i,
                        it / 9 + j,
                        it % 9 / 3 + k
                    ).block
                }
            }

            object : BukkitRunnable() {
                override fun run() {
                    val queue: Queue<Location> = LinkedList(listOf(event.block.location))
                    val firstType = event.block.type

                    while (queue.isNotEmpty()) {
                        val block = queue.poll().block

                        block.breakNaturally(item)

                        item.applyDamage()

                        queue += block.getNearbyBlocks()
                            .filter { it.type == firstType && it.location !in queue }
                            .run { List(size) { get(it).location } }
                    }
                }
            }.runTask(plugin)
        }
    }
}