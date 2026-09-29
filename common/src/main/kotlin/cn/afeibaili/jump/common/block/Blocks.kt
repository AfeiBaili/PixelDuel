package cn.afeibaili.jump.common.block

import cn.afeibaili.jump.common.item.BlockItemType


/**
 * # 方块
 *
 * @author AfeiBaili
 * @version 2026/9/29 17:18
 */

object Blocks {
    val ERROR = BlockItemType.register("error")
    val AIR = BlockItemType.register("air")
    val DIRT = BlockItemType.register("dirt")
    val GRASS_DIRT = BlockItemType.register("grass_dirt")
    val GRASS = BlockItemType.register("grass")
    val GRASS_TALL = BlockItemType.register("grass_tall")
    val STONE = BlockItemType.register("stone")
}