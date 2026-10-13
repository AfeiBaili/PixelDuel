package cn.afeibaili.jump.desktop.world

import cn.afeibaili.jump.common.world.Chunk
import cn.afeibaili.jump.common.world.World
import cn.afeibaili.jump.desktop.entity.Player


/**
 * # 世界处理器
 *
 * @author AfeiBaili
 * @version 2026/10/14 00:51
 */

class WorldHandler(val world: World) : WorldTickHandler {
    val player = Player.self
    val loadedChunk = mutableMapOf<String, Chunk>()

    init {
        WorldTickHandler.addHandler(this)
    }

    override fun tick() {

    }
}