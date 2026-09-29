package cn.afeibaili.jump.desktop.entity

import cn.afeibaili.gl.input.Key
import cn.afeibaili.jump.desktop.input.KeySet
import cn.afeibaili.jump.desktop.logic.LogicThread
import cn.afeibaili.jump.desktop.logic.TickHandler
import org.lwjgl.glfw.GLFW
import kotlin.math.abs


/**
 * # 玩家类
 *
 * @author AfeiBaili
 * @version 2026/6/27 19:35
 */

class Player(
    override var x: Float,
    override var y: Float,
) : Entity, TickHandler {
    companion object {
        val self = Player(0f, 0f)
    }

    override var xv: Float = 0f
    override var yv: Float = 0f
    override val speed: Float = 1f
    override val maxSpeed: Float = 5f
    override val smooth: Float = 0.85f
    override var pvx: Float = 0f
    override var pvy: Float = 0f
    val keySet = KeySet("player")

    init {
        TickHandler.addHandler(this)
    }

    override fun tick() {
        pvx = x
        pvy = y

        xv *= smooth
        yv *= smooth

        x += xv
        y += yv

        if (abs(xv) < 0.0001) xv = 0f
        if (abs(yv) < 0.0001) yv = 0f
    }

    fun loadKeyBind() {
        keySet.bind(Key("move_left", GLFW.GLFW_KEY_A)) {
            if (this.keyPressed()) {
                xv -= speed * LogicThread.self.step
            }
        }
        keySet.bind(Key("move_right", GLFW.GLFW_KEY_D)) {
            if (this.keyPressed()) {
                xv += speed * LogicThread.self.step
            }
        }
        keySet.bind(Key("move_up", GLFW.GLFW_KEY_W)) {
            if (this.keyPressed()) {
                yv += speed * LogicThread.self.step

            }
        }
        keySet.bind(Key("move_down", GLFW.GLFW_KEY_S)) {
            if (this.keyPressed()) {
                yv -= speed * LogicThread.self.step
            }
        }
    }

    fun init() {
        loadKeyBind()
        keySet.on()
    }
}