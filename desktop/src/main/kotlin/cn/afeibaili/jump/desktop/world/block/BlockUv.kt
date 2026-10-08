package cn.afeibaili.jump.desktop.world.block

import cn.afeibaili.gl.util.Time


/**
 * # 存放方块纹理和UV信息
 *
 * @author AfeiBaili
 * @version 2026/6/30 12:32
 */

class BlockUv(val uv: List<FloatArray>, var switchIntervalMilli: Int = 500) {
    var indexUv = 0
    var lastMillis = Time.millis()
    var accumulator = 0f
    var changed = false

    private fun getNext(): FloatArray {
        if (++indexUv >= uv.size) {
            indexUv = 0
        }
        return get()
    }

    fun get(): FloatArray {
        changed = false
        return uv[indexUv]
    }

    fun update() {
        val currentMillis = Time.millis()
        val delta = currentMillis - lastMillis
        lastMillis = currentMillis
        accumulator += delta
        while (accumulator > switchIntervalMilli) {
            accumulator -= switchIntervalMilli
            getNext()
            changed = true
        }
    }
}