package cn.afeibaili.jump.desktop.render.texture

import cn.afeibaili.gl.image.TextureModel
import cn.afeibaili.gl.image.atlas.BigImageAtlas
import cn.afeibaili.gl.image.atlas.DynamicImage
import cn.afeibaili.gl.image.atlas.Image
import cn.afeibaili.gl.util.putElementOrCreateList
import cn.afeibaili.jump.desktop.world.block.BlockUv
import java.awt.image.BufferedImage

/**
 * # 方块所有图片
 *
 * @author AfeiBaili
 * @version 2026/10/4 22:02
 */

class BlockImageList {
    val allImages = mutableMapOf<String, Image>()
    val allDynamicImages = mutableMapOf<String, DynamicImage>()
    val blockInfo get() = TextureManager.blockInfoMap

    constructor(list: HashMap<String, TextureModel>) {
        val dynamicImage = HashMap<String, MutableList<Pair<Int, BufferedImage>>>()
        val images = mutableMapOf<String, Image>()
        val matchRegex = "(.*)_([0-9]+)".toRegex()

        list.forEach { id, model ->
            val isDynamic: Boolean = matchRegex.matches(id)
            if (isDynamic) {
                val result: MatchResult? = matchRegex.find(id)
                val id: String = result!!.groups[1]!!.value
                val index: Int = result.groups[2]!!.value.toInt()
                val pair: Pair<Int, BufferedImage> = index to model.image
                dynamicImage.putElementOrCreateList(id, pair)
            } else {
                images.put(id, Image(id, model.image))
            }
        }
        val dynamicImageMap: Map<String, DynamicImage> = dynamicImage.map { entry ->
            val key = entry.key
            val list = entry.value
            val switchIntervalMillis: Int = blockInfo[key]?.switchIntervalMillis ?: 500
            val array = list.sortedBy { it.first }.map { Image(key, it.second) }.toTypedArray()
            key to DynamicImage(entry.key, switchIntervalMillis, array)
        }.toMap()
        allDynamicImages.putAll(dynamicImageMap)
        allImages.putAll(images)
    }

    /**
     * ## 使用 BigImageAtlas 接口需要事先填入元素
     */
    fun toBlockUvMap(bigImageAtlas: BigImageAtlas): Map<String, BlockUv> {
        bigImageAtlas.apply()
        bigImageAtlas.generateUv()
        val map = mutableMapOf<String, BlockUv>()
        allDynamicImages.forEach { (_, image) ->
            map[image.key] = BlockUv(image.getUvs(), image.switchMillisInternal)
        }
        allImages.forEach { (_, image) ->
            map[image.key] = BlockUv(listOf(image.uv))
        }
        return map
    }

    fun getImage(key: String): Image {
        allDynamicImages[key]?.let { return it.getImage() }
        allImages[key]?.let { return it }
        error("无法找到图片信息: $key")
    }

    fun getAllImage(): List<Image> {
        val images = mutableListOf<Image>()
        images.addAll(allImages.values)
        val map: List<Image> = allDynamicImages.values.flatMap { it.images.toList() }
        images.addAll(map)
        return images
    }

    fun updateDynamicImage() {
        allDynamicImages.values.forEach { image -> image.update() }
    }
}