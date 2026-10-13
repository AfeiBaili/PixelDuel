package cn.afeibaili.jump.desktop.world


/**
 * # 世界Tick处理器
 *
 * @author AfeiBaili
 * @version 2026/10/13 23:16
 */

interface WorldTickHandler {
    companion object {
        val all = ArrayList<WorldTickHandler>()

        fun addHandler(handle: WorldTickHandler) {
            all.add(handle)
        }
    }

    fun tick()
}