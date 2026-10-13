package cn.afeibaili.jump.common.world

import cn.afeibaili.jump.common.util.logger


/**
 * # 世界层级，可能是背景层也可能是玩家层
 *
 * 世界中不光只有一个玩家层，还有多个背景层可以用来交互
 *
 * @author AfeiBaili
 * @version 2026/8/27 22:41
 */

class Layer(val name: String) {
    private val logger = logger { "WorldLayer" }

    val chunks: Array<Chunk> = Array(LAYER_SIDE * LAYER_SIDE) { index ->
        val x: Int = index % LAYER_SIDE
        val y: Int = index / LAYER_SIDE
        logger.debug("create $name layer chunk: [x:${x}, y:${y}]")
        Chunk.createEmpty(x, y)
    }

    fun getChunkAt(x: Int, y: Int): Chunk? {
        if (x < 0 || x >= LAYER_SIDE || y < 0 || y >= LAYER_SIDE) return null
        return chunks[y * LAYER_SIDE + x]
    }

    companion object {
        var LAYER_SIDE = 4
    }
}