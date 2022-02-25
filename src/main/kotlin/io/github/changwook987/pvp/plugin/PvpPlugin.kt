package io.github.changwook987.pvp.plugin

import io.github.changwook987.pvp.StaticItems.fireBookRecipe
import io.github.changwook987.pvp.StaticItems.luckPickaxeRecipe
import io.github.changwook987.pvp.StaticItems.protectionBookRecipe
import io.github.changwook987.pvp.StaticItems.quickSmeltRecipe
import io.github.changwook987.pvp.StaticItems.sharpnessBookRecipe
import io.github.changwook987.pvp.command.PvpCommand
import io.github.changwook987.pvp.eventListener.ChopOre
import io.github.monun.kommand.kommand
import org.bukkit.GameRule
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.plugin.java.JavaPlugin

class PvpPlugin : JavaPlugin() {
    override fun onEnable() {

        //<editor-fold desc="EventListener">
        ChopOre(this).register()
        //</editor-fold>

        for (world in server.worlds) {
            world.worldBorder.apply {
                setCenter(0.0, 0.0)
                size = 1000.0
            }
        }

        val pvpWorld = WorldCreator("pvp_world")

        pvpWorld.apply {
            type(WorldType.FLAT)
            environment(World.Environment.NORMAL)
            generateStructures(false)

            createWorld()?.apply {
                setGameRule(GameRule.DO_MOB_SPAWNING, false)
                setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
            }
        }

        addRecipe()

        kommand { PvpCommand.register(this@PvpPlugin, this) }
    }

    private fun addRecipe() {
        server.addRecipe(fireBookRecipe(this))
        server.addRecipe(sharpnessBookRecipe(this))
        server.addRecipe(protectionBookRecipe(this))
        server.addRecipe(luckPickaxeRecipe(this))

        with(server.recipeIterator()) {
            while (hasNext()) {
                val recipe = next()
                if (recipe is FurnaceRecipe) {
                    val materialChoice = recipe.inputChoice
                    if (materialChoice is RecipeChoice.MaterialChoice) {
                        for (input in materialChoice.choices) {
                            server.addRecipe(quickSmeltRecipe(input, recipe.result.type, this@PvpPlugin))
                        }
                    }
                }
            }
        }
    }

    private fun ChopOre.register() = server.pluginManager.registerEvents(this, this@PvpPlugin)
}

