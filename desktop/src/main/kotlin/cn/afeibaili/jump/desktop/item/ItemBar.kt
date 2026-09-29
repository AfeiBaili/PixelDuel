package cn.afeibaili.jump.desktop.item

import cn.afeibaili.jump.common.block.Blocks
import cn.afeibaili.jump.common.item.BlockItem


/**
 * # 物品栏
 *
 * @author AfeiBaili
 * @version 2026/9/29 13:50
 */

class ItemBar {
    val length = 10
    var pointer = 0

    val bar = arrayOf(
        ItemGrid(BlockItem(99, Blocks.ERROR)),
        ItemGrid(BlockItem(99, Blocks.DIRT)),
        ItemGrid(BlockItem(99, Blocks.GRASS_DIRT)),
        ItemGrid(BlockItem(99, Blocks.GRASS)),
        ItemGrid(BlockItem(99, Blocks.GRASS_TALL)),
        ItemGrid(BlockItem(99, Blocks.STONE)),
        ItemGrid(BlockItem(99, Blocks.AIR)),
        ItemGrid(BlockItem(99, Blocks.AIR)),
        ItemGrid(BlockItem(99, Blocks.AIR)),
        ItemGrid(BlockItem(99, Blocks.AIR)),
    )

    fun getGrid(): ItemGrid = bar[pointer]

    fun switchNext(): ItemGrid {
        if (pointer < length - 1) pointer++
        else pointer = 0
        return getGrid()
    }

    fun switchPrevious(): ItemGrid {
        if (pointer <= 0) pointer = length - 1
        else pointer--
        return getGrid()
    }

    fun switchGird(index: Int): ItemGrid {
        pointer = if (index in 0..length - 1) index else 0
        return getGrid()
    }
}