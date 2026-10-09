package cn.afeibaili.jump.desktop.input

import cn.afeibaili.gl.input.Scroll
import cn.afeibaili.gl.input.ScrollBind
import cn.afeibaili.jump.common.Identifier
import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.desktop.Application


/**
 * # 滚动按键集合
 *
 * @author AfeiBaili
 * @version 2026/10/9 10:09
 */

class ScrollSet(id: String) {
    val identifier = Identifier("scroll", id)
    var enabled = false
    val set = HashSet<Pair<ScrollBind, ScrollBind.() -> Unit>>()

    init {
        addSet(this)
    }

    fun bind(scroll: Scroll, callback: ScrollBind.() -> Unit) {
        logger.info("registering mouse scroll set for ${scroll.id}")
        set.add(ScrollBind(scroll, Application.window) to callback)
    }

    fun on() {
        enabled = true
    }

    fun off() {
        enabled = false
    }

    companion object {
        private val logger = logger { "ScrollSet" }
        val all = mutableSetOf<ScrollSet>()
        fun addSet(sc: ScrollSet) {
            all.add(sc)
        }
    }
}