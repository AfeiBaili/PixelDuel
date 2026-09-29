package cn.afeibaili.jump.common.item

import cn.afeibaili.jump.common.Identifier
import cn.afeibaili.jump.common.util.logger


/**
 * # 物品
 *
 * @author AfeiBaili
 * @version 2026/9/29 13:52
 */
class ItemType(val identifier: Identifier) {
    companion object {
        val all = HashMap<Identifier, ItemType>()
        private val logger = logger { "Item" }


        fun register(id: String): ItemType {
            val identifier = Identifier("item", id)
            logger.info("registering $identifier")
            return ItemType(identifier).also { all[identifier] = it }
        }
    }
}