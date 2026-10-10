package cn.afeibaili.jump.desktop.render

import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.desktop.render.text.FpsTimer
import cn.afeibaili.jump.desktop.render.texture.TextureManager


/**
 * # 渲染器系统
 *
 * @author AfeiBaili
 * @version 2026/6/6 18:49
 */

class RendererSystem {
    private val logger = logger { "RendererSystem" }
    val worldRenderer = WorldRenderer()
    val playerRenderer = PlayerRenderer()
    val blockModelUpdater = BlockModelUpdater()
    val uiRenderer = UIRenderer()
    // val debugRenderer = DebugRenderer()
    val fps = FpsTimer()

    fun init() {
        logger.info("initialize world renderer")
        worldRenderer.init()
        logger.info("initialize block renderer")
        blockModelUpdater.init()
        logger.info("initialize ui renderer")
        uiRenderer.init()
    }

    fun frame() {
        fps.update()
        blockModelUpdater.update()
        TextureManager.blockImageList.updateDynamicImage()
        worldRenderer.render()
        playerRenderer.render()
        uiRenderer.render()
    }
}