package cn.afeibaili.jump.desktop.world.model

import cn.afeibaili.gl.exception.ImageException
import cn.afeibaili.gl.image.Atlas
import cn.afeibaili.gl.image.Texture
import cn.afeibaili.gl.util.Index
import cn.afeibaili.jump.common.block.Blocks
import cn.afeibaili.jump.common.json.BlockInfo
import cn.afeibaili.jump.common.resource.BlockInfoLoader
import cn.afeibaili.jump.common.world.Chunk
import cn.afeibaili.jump.desktop.render.texture.TextureManager
import cn.afeibaili.jump.desktop.world.block.BlockModel
import cn.afeibaili.jump.desktop.world.block.BlockModelType
import cn.afeibaili.jump.desktop.world.block.BlockUv


/**
 * # 区块模型
 *
 * @author AfeiBaili
 * @version 2026/8/31 13:20
 */

class ChunkModel(val chunk: Chunk, var blockAtlas: MutableList<BlockAtlas>) {
    var changed = true

    fun update() {
        if (chunk.changed || changed) {
            blockAtlas = buildAtlases(chunk)

            blockAtlas.forEach { blockAtlas ->
                blockAtlas.updateInstanceBuffer()
                blockAtlas.updateUvBuffer()
            }
            chunk.update()
            changed = false
        }
        blockAtlas.forEach { blockAtlas -> blockAtlas.update() }
    }

    companion object {
        val blockTextureAtlas get() = TextureManager.blockTextureAtlas
        val textureSide get() = TextureManager.textureSizeMap
        val blockInfo get() = BlockInfoLoader.load()

        fun of(chunk: Chunk): ChunkModel {
            return ChunkModel(chunk, buildAtlases(chunk))
        }

        fun buildAtlases(chunk: Chunk): MutableList<BlockAtlas> {
            val blockTypeModelMap = mutableMapOf<String, BlockModelType>()
            val blockModelData = mutableMapOf<Index, BlockTextureModelList>()

            chunk.blocks.forEach { block ->
                var atlas: Atlas? = blockTextureAtlas.getAtlas(block.id)
                if (atlas == null) {
                    atlas = blockTextureAtlas.getAtlas(Blocks.ERROR.blockType.id)
                }
                atlas ?: throw ImageException("找不到错误纹理，其中纹理缺失: ${block.type.id}")

                val uvs: List<FloatArray> = runCatching {
                    blockTextureAtlas.getUvs(block.id)
                }.getOrElse {
                    runCatching {
                        blockTextureAtlas.getUvs(Blocks.ERROR.blockType.id)
                    }.getOrElse { throw ImageException("找不到错误纹理uv") }
                }

                var blockModelType: BlockModelType? = blockTypeModelMap[block.id]
                if (blockModelType == null) {
                    val info: BlockInfo? = blockInfo[block.id]
                    val switchIntervalMilli: Int = info?.switchIntervalMilli ?: 500
                    blockTypeModelMap[block.id] = BlockModelType.register(
                        block.type.identifier, BlockUv(uvs, switchIntervalMilli)
                    )
                }
                blockModelType = blockTypeModelMap[block.id]!!

                val texture: Texture = textureSide[atlas.atlasId]!!
                val modelArray: BlockTextureModelList? = blockModelData[atlas.atlasId]
                val model = BlockModel(block.x, block.y, blockModelType)
                if (modelArray == null) blockModelData[atlas.atlasId] =
                    BlockTextureModelList(texture, mutableListOf(model))
                else modelArray.blocks.add(model)
            }

            val atlases: List<BlockAtlas> =
                blockModelData.map { (_, bl) -> BlockAtlas(bl.texture, bl.blocks, bl.blocks.size) }

            return atlases as MutableList<BlockAtlas>
        }
    }

    class BlockTextureModelList(val texture: Texture, val blocks: MutableList<BlockModel>)
}