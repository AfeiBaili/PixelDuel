package cn.afeibaili.jump.desktop.render.layout

import cn.afeibaili.gl.font.FontFactory
import cn.afeibaili.gl.input.Key
import cn.afeibaili.gl.render.Color
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.adapt.rowAdapt
import cn.afeibaili.gl.render.layout.align.AlignmentSetting
import cn.afeibaili.gl.render.layout.align.AlignmentType
import cn.afeibaili.gl.render.layout.align.block
import cn.afeibaili.gl.render.layout.text.TextUpdater
import cn.afeibaili.gl.render.layout.text.text
import cn.afeibaili.jump.common.identifier
import cn.afeibaili.jump.common.resource.ResourceFileGetter
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.entity.Player
import cn.afeibaili.jump.desktop.input.KeySet
import org.lwjgl.glfw.GLFW

/**
 * # 布局
 *
 * @author AfeiBaili
 * @version 2026/8/26 14:04
 */
class F3Layout {
    val font = FontFactory.create(
        "source", ResourceFileGetter.getResourceFile("font/SourceHanSansHWSC-Regular.otf").canonicalPath, 64
    ).apply { texture.upload() }
    val textUpdater = TextUpdater()
    val window get() = Application.window
    val windowSystem get() = Application.windowSystem
    val worldEditor get() = Application.worldEditor
    val textBackgroundColor = Color.parse("#2B2D3080")
    val keySet = KeySet("f3")
    val scale = 0.4f
    lateinit var f3: Layout

    fun load() {
        loadKey()
        keySet.on()
        layout()
    }

    private fun loadKey() {
        keySet.bind(Key("vsync", GLFW.GLFW_KEY_F1)) {
            released {
                window.vsync = !window.vsync
            }
        }
        keySet.bind(Key("open_f3", GLFW.GLFW_KEY_F3)) {
            released {
                f3.showable = !f3.showable
                Application.rendererSystem.uiRenderer.update()
            }
        }
    }

    private fun layout() = Application.screen.layout {
        f3 = block(setting = { it.maxSize() }) {
            rowAdapt(setting = { it: AlignmentSetting -> it.align(AlignmentType.LEFT_TOP).offsetX(10f).offsetY(10f) }) {
                text(
                    "f3.greet", "press f3 open debug", font, updater = textUpdater, scale = scale,
                    backgroundColor = textBackgroundColor
                )
                text(
                    "f3.fps", "init fps", font, updater = textUpdater, scale = scale,
                    backgroundColor = textBackgroundColor,
                )
                text(
                    "f3.v-sync", "init v-sync", font, updater = textUpdater, scale = scale,
                    backgroundColor = textBackgroundColor,
                )
                text(
                    "f3.cursor.position", "init cursor", font, updater = textUpdater, scale = scale,
                    backgroundColor = textBackgroundColor,
                )
                text(
                    "f3.cursor.block", "init cursor block", font, updater = textUpdater, scale = scale,
                    backgroundColor = textBackgroundColor,
                )
                text(
                    "f3.player.position", "init player position", font, updater = textUpdater, scale = scale,
                    backgroundColor = textBackgroundColor,
                )
                text(
                    "f3.current.layer", "init current layer index", font, updater = textUpdater, scale = scale,
                    backgroundColor = textBackgroundColor,
                )
            }
        }
    }

    fun updateText() {
        textUpdater.update("f3.fps", "fps: ${Application.rendererSystem.fps()}")
        textUpdater.update("f3.v-sync", "v-sync: ${window.vsync}")
        textUpdater.update(
            "f3.cursor.position",
            "position: {cursor: [x:${windowSystem.cursorX}, y:${windowSystem.cursorY}], block: [x:${worldEditor.blockPositionX}, y:${worldEditor.blockPositionY}]}"
        )
        textUpdater.update("f3.cursor.block", "block: ${worldEditor.currentCursorBlock}")
        textUpdater.update("f3.player.position", "player: [x:${Player.self.x}, y:${Player.self.y}]")
        textUpdater.update("f3.current.layer", "layer index: ${worldEditor.currentLayerIndex}")
    }
}