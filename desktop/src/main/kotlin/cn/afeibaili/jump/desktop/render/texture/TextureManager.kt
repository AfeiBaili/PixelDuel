package cn.afeibaili.jump.desktop.render.texture

import cn.afeibaili.gl.image.Texture
import cn.afeibaili.gl.image.TextureAtlas
import cn.afeibaili.gl.image.TextureModel
import cn.afeibaili.gl.util.Index
import cn.afeibaili.jump.common.block.Blocks
import cn.afeibaili.jump.common.resource.ResourceFileGetter
import cn.afeibaili.jump.common.util.logger
import java.awt.image.BufferedImage
import kotlin.random.Random

/**
 * 纹理管理器
 *
 * @author AfeiBaili
 * @version 2026/6/29 21:54
 */

object TextureManager {
    const val DEFAULT_MODEL_SIZE = 16

    private val logger = logger { "TextureManager" }

    val air = TextureModel.create(Blocks.AIR.blockType.id) {
        val image = getDefaultImage()
        image
    }

    val error = TextureModel.createDynamic(Blocks.ERROR.blockType.id) {
        val seed: Long = System.currentTimeMillis()
        val random = Random(seed)
        logger.info("error random seed: $seed")

        fun getErrorImage(): BufferedImage = defaultImage {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val rgb: Int = (random.nextInt() ushr 8) or (0xff shl 24)
                    setRGB(y, x, rgb)
                }
            }
        }

        listOf(
            getErrorImage(),
            getErrorImage(),
            getErrorImage(),
            getErrorImage(),
            getErrorImage(),
        )
    }

    /** 方块所有图集 */
    val blockTextureAtlas = TextureAtlas.create(
        "block",
        ResourceFileGetter.getResourceFileList("block"),
        1,
        air, *error.toTypedArray(),
    )

    /** 不同大小的图集 */
    val textureSizeMap: Map<Index, Texture> =
        blockTextureAtlas.atlas.map { it.key to it.value.texture }.toMap()

    fun getDefaultImage() = BufferedImage(DEFAULT_MODEL_SIZE, DEFAULT_MODEL_SIZE, BufferedImage.TYPE_INT_ARGB)
    fun defaultImage(imageAction: BufferedImage.() -> Unit): BufferedImage {
        val image: BufferedImage = getDefaultImage()
        imageAction(image)
        return image
    }
}