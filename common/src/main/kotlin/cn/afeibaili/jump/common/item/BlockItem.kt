package cn.afeibaili.jump.common.item

/**
 * # 方块物品
 *
 * @author AfeiBaili
 * @version 2026/9/29 17:32
 */

class BlockItem(quantity: Int, val blockItemType: BlockItemType) : Item(quantity, blockItemType.itemType)