package cn.afeibaili.jump.desktop.logic

import cn.afeibaili.gl.util.Time


/**
 * # 逻辑专用线程
 *
 * @author AfeiBaili
 * @version 2026/6/23 23:09
 */

class LogicThread {
    var isActive: Boolean = true

    val tick = 60f

    val step = 1f / tick
    var accumulationSecondes = 0f
    var lastTime = Time.nano()
    val thread = Thread({
        while (isActive) {
            val currentNano: Long = Time.nano()
            val deltaSecondes: Float = (currentNano - lastTime) / 1_000_000_000f
            lastTime = currentNano
            accumulationSecondes += deltaSecondes
            while (accumulationSecondes >= step) {
                accumulationSecondes -= step
                update()
            }
            Thread.sleep(1) // 放缓CPU
        }
    }, "LogicThread")

    /**
     * ## 刻更新方法，已用**TickHandler**类进行封装
     *
     * 每秒60次
     *
     * @see LogicTickHandler
     */
    fun update() {
        LogicTickHandler.all.forEach { it.tick() }
    }

    fun start() {
        thread.start()
    }

    fun stop() {
        isActive = false
    }

    companion object {
        val self = LogicThread()
    }
}
