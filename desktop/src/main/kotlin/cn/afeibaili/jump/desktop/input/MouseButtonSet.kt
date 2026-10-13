package cn.afeibaili.jump.desktop.input

import cn.afeibaili.gl.input.MouseButton
import cn.afeibaili.gl.input.MouseButtonBind
import cn.afeibaili.jump.common.Identifier
import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.logic.LogicTickHandler


/**
 * # 鼠标按键集合设置
 *
 * @author AfeiBaili
 * @version 2026/9/2 12:13
 */

data class MouseButtonSet(val identifier: Identifier) : LogicTickHandler {
    companion object {
        private val logger = logger { "MouseButtonSet" }
        val all = HashSet<MouseButtonSet>()
        fun addSet(mouseButtonSet: MouseButtonSet) {
            all.add(mouseButtonSet)
        }
    }

    init {
        LogicTickHandler.addHandler(this)
        addSet(this)
    }

    var enabled = false
    val set = HashSet<Pair<MouseButtonBind, MouseButtonBind.() -> Unit>>()

    override fun tick() {
        for ((bind, action) in set) {
            if (enabled) action(bind)
        }
    }

    fun bind(mouseButton: MouseButton, callback: MouseButtonBind.() -> Unit) {
        logger.info("registering mouse button set for ${mouseButton.id}")
        set.add(Pair(MouseButtonBind(mouseButton, Application.window), callback))
    }

    fun on() {
        enabled = true
    }

    fun off() {
        enabled = false
    }
}