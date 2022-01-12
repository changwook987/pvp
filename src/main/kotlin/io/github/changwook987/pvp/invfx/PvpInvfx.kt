package io.github.changwook987.pvp.invfx

import io.github.monun.invfx.InvFX
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component.text
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object PvpInvfx {
    fun createEnchantFrame(items: Collection<ItemStack>): InvFrame {
        return InvFX.frame(items.size / 9 + 1, text("인챈트")) {
            list(0, 0, 9, items.size / 9 + 1, true, { items.toList() }) {
                transform { it }
                onClickItem { _, _, (a, _), event ->
                    event.whoClicked.inventory.apply {
                        itemInMainHand.addUnsafeEnchantments(
                            (a.itemMeta as EnchantmentStorageMeta).storedEnchants
                        )

                        with(iterator()) {
                            while (hasNext()) {
                                val item = next()
                                if (item == a) {
                                    item.amount--
                                    return@with
                                }
                            }
                        }
                    }
                    event.whoClicked.apply {
                        closeInventory()
                        if (this is Player) {
                            openFrame(createEnchantFrame(items.minus(a)))
                        }
                    }
                }
            }
        }
    }
}