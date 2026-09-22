package cn.afeibaili.jump.desktop.window

import cn.afeibaili.gl.Window
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.jump.desktop.Application
import org.lwjgl.glfw.GLFW

/**
 * # 窗口系统管理
 *
 * @author AfeiBaili
 * @version 2026/6/6 20:43
 */

class WindowSystem(val window: Window = Application.window) {
    var cursorX: Int = 0
    var cursorY: Int = 0
    var aspect = Application.screenWidth.toFloat() / Application.screenHeight.toFloat()

    fun init() {

        GLFW.glfwSetWindowSizeCallback(window.pointer) { _, w, h ->
            Application.screenWidth = w
            Application.screenHeight = h
            val h = if (h == 0) 1 else h
            aspect = w.toFloat() / h.toFloat()
            window.setViewport(w, h)
            val worldCamera = Application.camera
            worldCamera// 世界摄像机
                .ortho(
                    0f,
                    worldCamera.zoom * aspect,
                    0f,
                    worldCamera.zoom,
                    -1f,
                    1f
                )

            // setWidthHeightOrtho(Application.rendererSystem.debugRenderer.textCamera, 0f, w.toFloat(), 0f, h.toFloat())
            setWidthHeightOrtho(Application.rendererSystem.uiRenderer.rectCamera, 0f, w.toFloat(), h.toFloat(), 0f)
            setWidthHeightOrtho(Application.rendererSystem.uiRenderer.textCamera, 0f, w.toFloat(), h.toFloat(), 0f)

            Application.screen.update(w.toFloat(), h.toFloat()) //屏幕大小更新
            Application.rendererSystem.uiRenderer.update() // ui数据更新
        }

        GLFW.glfwSetCursorPosCallback(window.pointer) { _, x, y ->
            cursorX = x.toInt()
            cursorY = y.toInt()
        }

        GLFW.glfwSetWindowCloseCallback(window.pointer) {
            Application.stop()
        }
    }

    private fun setWidthHeightOrtho(camera: Camera, left: Float, right: Float, bottom: Float, top: Float) {
        camera.ortho(left, right, bottom, top, -1f, 0f)
    }
}