package cn.afeibaili.jump.desktop

import cn.afeibaili.gl.Window
import cn.afeibaili.gl.logger.Logger
import cn.afeibaili.gl.render.layout.RootLayout
import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.common.world.World
import cn.afeibaili.jump.desktop.entity.Player
import cn.afeibaili.jump.desktop.logic.LogicThread
import cn.afeibaili.jump.desktop.render.RendererSystem
import cn.afeibaili.jump.desktop.window.WindowSystem
import cn.afeibaili.jump.desktop.world.WorldEditor
import cn.afeibaili.jump.desktop.world.WorldHandler
import cn.afeibaili.jump.desktop.world.WorldThread
import cn.afeibaili.jump.desktop.world.model.WorldModel


/**
 * # 应用程序入口
 *
 *@author AfeiBaili
 *@version 2026/6/1 22:57
 */

class Application {
    companion object {
        init {
            Logger.printDebug = true
            Logger.writeFile = false
        }

        var screenWidth = 800
        var screenHeight = 800
        private val logger = logger { "Application" } //日志器
        var running = true
        val window: Window = Window.builder() //窗口构建器
            .buildTitle("Anying: Fight")
            .buildWidth(screenWidth)
            .buildHeight(screenHeight)
            .withVerticalSync(false)
            .withClearColor(0.1f, 0.1f, 0.1f, 1f)
            .build()
        val windowSystem = WindowSystem(window) //窗口管理器
        val screen = RootLayout(screenWidth.toFloat(), screenHeight.toFloat())
        val rendererSystem = RendererSystem() //渲染系统
        val logicThread get() = LogicThread.self
        val worldThread get() = WorldThread.self
        val camera get() = rendererSystem.worldRenderer.camera
        val player get() = Player.self
        lateinit var worldEditor: WorldEditor
        lateinit var world: WorldModel
        lateinit var worldHandler: WorldHandler

        @JvmStatic
        fun main(args: Array<String>) {
            runCatching {
                world = loadWorld()
                setup()
                logicThread.start()
                worldThread.start()
                window.loopFrame({ running }) {
                    rendererSystem.frame()
                }
                logicThread.thread.join()
                worldThread.thread.join()
            }.onFailure {
                logger.error("渲染线程出错, 退出程序: ${it.stackTraceToString()}")
                stop()
            }
        }

        fun setup() {
            logger.info("initialize world editor")
            worldEditor = WorldEditor(world.world)
            logger.info("initialize window system")
            windowSystem.init()
            logger.info("initialize renderer system")
            rendererSystem.init()
            logger.info("initialize player")
            player.init()
            logger.info("application is initialized")
        }

        fun stop() {
            running = false
            logicThread.stop()
            worldThread.stop()
            window.close()
            logger.info("windows is destroy")
        }

        fun loadWorld(): WorldModel {
            val default = World("default")
            worldHandler = WorldHandler(default)
            return WorldModel.of(default)
        }
    }
}