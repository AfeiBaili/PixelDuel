package cn.afeibaili.jump.desktop.input

import cn.afeibaili.gl.input.Key
import cn.afeibaili.gl.input.KeyBind
import cn.afeibaili.jump.common.identifier
import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.logic.TickHandler

/**
 * # 一个按键集合，统一进行开关管理
 *
 * 已自动装载至KeySet.all集合中，无需手动装载
 *
 * 支持开启和关闭，使用`onKey()`和`offKey()`开启和关闭
 *
 * ## 使用
 *
 * 创建`KeySet(identifier)`对象并使用绑定方法，进行按键绑定
 *
 * ```
 * ketSet.bind(Key("move_left", GLFW.GLFW_KEY_A)) {
 *     if (this.keyPress()) {
 *         xv += speed * LogicThread.self.step
 *     }
 * }
 * ```
 *
 * ## 开启
 *
 * 使用`keySet.onKey()`方法开启按键监听，否则无法监听
 *
 * ```
 * val keySet = KeySet("system" identifier "window")
 * init {
 *     keySet.onKey()
 * }
 * ```
 *
 * @author AfeiBaili
 * @version 2026/6/27 19:53
 */

data class KeySet(val id: String) : TickHandler {
    val identifier = "key" identifier id

    companion object {
        @JvmStatic
        private val logger = logger { "KeySet" }
        val all = HashSet<KeySet>()
        fun addSet(keySet: KeySet) {
            all.add(keySet)
        }
    }

    init {
        TickHandler.addHandler(this)
        addSet(this)
    }

    var keyable: Boolean = false
    val set: MutableSet<Pair<KeyBind, KeyBind.() -> Unit>> = HashSet()

    override fun tick() {
        for (bind in set) {
            val (keybind, callback) = bind
            if (keyable) callback(keybind)
        }
    }

    fun bind(key: Key, callback: KeyBind.() -> Unit) {
        logger.info("registering key set for ${key.id}")
        set.add(KeyBind(key, Application.window) to callback)
    }

    fun on() {
        keyable = true
    }

    fun off() {
        keyable = false
    }
}