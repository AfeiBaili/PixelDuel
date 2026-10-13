package cn.afeibaili.jump.desktop.world

import cn.afeibaili.jump.common.world.World


/**
 * # 世界处理器
 *
 * @author AfeiBaili
 * @version 2026/10/14 00:51
 */

class WorldHandler(val world: World) : WorldTickHandler {
    init {
        WorldTickHandler.addHandler(this)
    }

    override fun tick() {
    }
}