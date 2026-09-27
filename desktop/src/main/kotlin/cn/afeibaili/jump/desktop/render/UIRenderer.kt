package cn.afeibaili.jump.desktop.render

import cn.afeibaili.gl.render.ImageRenderer
import cn.afeibaili.gl.render.LayoutRenderer
import cn.afeibaili.gl.render.RectangleRenderer
import cn.afeibaili.gl.render.TextLayoutRenderer
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.shader.Program
import cn.afeibaili.gl.render.shader.Shader
import cn.afeibaili.jump.common.resource.ResourceFileGetter
import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.render.layout.F3Layout
import cn.afeibaili.jump.desktop.render.layout.ItemBarLayout

/**
 * # 界面渲染器
 *
 * @author AfeiBaili
 * @version 2026/8/16 13:34
 */

class UIRenderer {
    private val logger = logger { "UIRenderer" }
    private val window get() = Application.window
    lateinit var layoutRenderer: LayoutRenderer
    lateinit var rectCamera: Camera
    lateinit var textCamera: Camera
    lateinit var imageCamera: Camera
    var f3Layout: F3Layout = F3Layout()
    var itemBarLayout: ItemBarLayout = ItemBarLayout()

    fun init() {
        f3Layout.load()
        itemBarLayout.load()
        logger.info("loaded layout")
        // RECT ////
        val rectVertexShader = Shader.create(
            Shader.ShaderType.VERTEX, ResourceFileGetter.getResourceFile("shader/layout/rectangle.vert").readText()
        )
        val rectFragmentShader = Shader.create(
            Shader.ShaderType.FRAGMENT, ResourceFileGetter.getResourceFile("shader/layout/rectangle.frag").readText()
        )
        val rectProgram: Program = Program.create(rectVertexShader, rectFragmentShader).apply { link() }
        rectCamera =
            Camera(rectProgram).apply { ortho(0f, window.width.toFloat(), window.height.toFloat(), 0f, -1f, 1f) }
        val rectangleRenderer = RectangleRenderer(rectProgram, rectCamera)

        // TEXT ////
        val textVertexShader = Shader.create(
            Shader.ShaderType.VERTEX, ResourceFileGetter.getResourceFile("shader/layout/text.vert").readText()
        )
        val textFragmentShader = Shader.create(
            Shader.ShaderType.FRAGMENT, ResourceFileGetter.getResourceFile("shader/layout/text.frag").readText()
        )
        val textProgram: Program = Program.create(textVertexShader, textFragmentShader).apply { link() }
        textCamera =
            Camera(textProgram).apply { ortho(0f, window.width.toFloat(), window.height.toFloat(), 0f, -1f, 1f) }
        val textRenderer = TextLayoutRenderer(textProgram, textCamera)
        // IMAGE ////
        val imageVertexShader = Shader.create(
            Shader.ShaderType.VERTEX,
            ResourceFileGetter.getResourceFile("shader/layout/image.vert").readText()
        )
        val imageFragmentShader = Shader.create(
            Shader.ShaderType.FRAGMENT,
            ResourceFileGetter.getResourceFile("shader/layout/image.frag").readText()
        )
        val imageProgram: Program = Program.create(imageVertexShader, imageFragmentShader).apply { link() }
        imageCamera =
            Camera(imageProgram).apply { ortho(0f, window.width.toFloat(), window.height.toFloat(), 0f, -1f, 1f) }
        val imageRenderer = ImageRenderer(imageProgram, imageCamera)

        layoutRenderer = LayoutRenderer(textRenderer, rectangleRenderer, imageRenderer, Application.screen)
        layoutRenderer.init()
    }

    fun update() {
        layoutRenderer.update()
    }

    fun render() {
        f3Layout.updateText()
        layoutRenderer.render()
    }
}