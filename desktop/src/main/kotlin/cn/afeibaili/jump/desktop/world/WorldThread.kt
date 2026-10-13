package cn.afeibaili.jump.desktop.world

import cn.afeibaili.gl.util.Time
import cn.afeibaili.jump.desktop.Application


/**
 * # 世界操作进程
 *
 * @author AfeiBaili
 * @version 2026/10/13 23:15
 */

class WorldThread {
    var isActive: Boolean = true

    val tick = 20f
    val step = 1f / tick
    var accumulator = 0f
    var lastMillis = Time.millis()

    val thread = Thread({
        while (isActive) {
            val currentMillis: Long = Time.millis()
            val deltaSecondes: Float = (currentMillis - lastMillis) / 1000f
            lastMillis = currentMillis
            accumulator += deltaSecondes
            while (accumulator >= step) {
                accumulator -= step
                update()
            }
            Thread.sleep(1) // 放缓CPU
        }
    }, "WorldThread")

    fun update() {
        WorldTickHandler.all.forEach { it.tick() }
    }

    fun start() {
        thread.start()
    }

    fun stop() {
        isActive = false
    }

    companion object {
        val self get() = Application.worldThread
    }
}