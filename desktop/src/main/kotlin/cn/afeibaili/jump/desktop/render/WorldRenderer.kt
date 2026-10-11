package cn.afeibaili.jump.desktop.render

import cn.afeibaili.gl.image.atlas.BigImageAtlas
import cn.afeibaili.gl.input.Scroll
import cn.afeibaili.gl.render.WorldRenderer
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.shader.Program
import cn.afeibaili.gl.render.shader.Shader
import cn.afeibaili.gl.util.Time
import cn.afeibaili.jump.common.resource.ResourceFileGetter
import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.desktop.Application
import cn.afeibaili.jump.desktop.input.ScrollSet
import cn.afeibaili.jump.desktop.render.texture.TextureManager
import cn.afeibaili.jump.desktop.world.model.LayerModel
import cn.afeibaili.jump.desktop.world.model.WorldModel


/**
 * # 世界渲染器
 *
 * @author AfeiBaili
 * @version 2026/6/6 18:59
 */

class WorldRenderer {
    private val logger = logger { "WorldRenderer" }

    val camera get() = _camera
    val world get() = Application.world

    private lateinit var _camera: Camera
    private lateinit var _program: Program
    private lateinit var renderer: WorldRender
    val scrollSet = ScrollSet("world.scale")

    var scale = 1f
    var accumulator = 1f
    var damping = 6f
    var lastMillis = Time.millis()

    fun init() {
        loadInput()
        logger.info("create program")
        _program = Program.create(
            Shader.create(
                Shader.ShaderType.VERTEX,
                ResourceFileGetter.getResourceFile("shader/world.vert").readText()
            ),
            Shader.create(
                Shader.ShaderType.FRAGMENT,
                ResourceFileGetter.getResourceFile("shader/world.frag").readText()
            )
        )
        _camera = Camera(_program, "projection", "view")
        _program.link()
        renderer = WorldRender(_program, _camera, TextureManager.blockBigImageAtlas, world)
    }

    fun render() {
        computeScale()
        renderer.render()
    }

    fun computeScale() {
        val now = Time.millis()
        val dt = (now - lastMillis) / 1000f
        lastMillis = now
        scale += (accumulator - scale) * (damping * dt / (1f - damping * dt))
        camera.scale(scale)
    }

    fun loadInput() {
        val scaleValue = 0.05f

        scrollSet.bind(Scroll("augment.scale", 1.0, alt = true)) {
            up { if (accumulator + scaleValue < 5) accumulator += scaleValue }
        }
        scrollSet.bind(Scroll("reduce.scale", 1.0, alt = true)) {
            down { if (accumulator - scaleValue > 0.1) accumulator -= scaleValue }
        }

        scrollSet.on()
    }

    companion object {
        class WorldRender(
            override val program: Program,
            override val camera: Camera,
            bigImageAtlas: BigImageAtlas,
            val world: WorldModel,
        ) : WorldRenderer(program, camera) {
            val texture = bigImageAtlas.toTexture().apply { this.upload() }

            fun render() {
                texture.bind()

                val layerSize = world.layers.size
                for (index in layerSize - 1 downTo 0) {
                    val layer: LayerModel = world.layers[index]
                    layer.chunks.forEach { chunkModel ->
                        chunkModel.update()
                        uploadInstanceBuffer(chunkModel.positionBuffer)
                        uploadUvBuffer(chunkModel.uvBuffer)
                        program.setUniform("light", f1 = computeLayerLight(index, layerSize))
                        renderInstance(chunkModel.blocks.size)
                    }
                }
            }

            fun computeLayerLight(layerIndex: Int, size: Int): Float {
                return 1f - layerIndex / size.toFloat() / 1.55f
            }
        }
    }
}