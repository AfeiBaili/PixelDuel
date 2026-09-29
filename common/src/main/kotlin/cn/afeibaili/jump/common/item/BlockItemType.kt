package cn.afeibaili.jump.common.item

import cn.afeibaili.jump.common.block.BlockType

/**
 * # 方块物品
 *
 * @author AfeiBaili
 * @version 2026/9/29 17:09
 */

class BlockItemType(val itemType: ItemType, val blockType: BlockType) {
    companion object {
        fun register(id: String): BlockItemType {
            val blockType: BlockType = BlockType.Companion.register(id)
            val itemType: ItemType = ItemType.Companion.register(id)
            return BlockItemType(itemType, blockType)
        }
    }
}