package cn.afeibaili.jump.desktop.world.model

import cn.afeibaili.gl.render.WorldRenderer
import cn.afeibaili.jump.common.block.Blocks
import cn.afeibaili.jump.common.world.Chunk
import cn.afeibaili.jump.desktop.render.texture.TextureManager
import cn.afeibaili.jump.desktop.world.block.BlockModel
import cn.afeibaili.jump.desktop.world.block.BlockModelType
import cn.afeibaili.jump.desktop.world.block.BlockUv
import org.lwjgl.BufferUtils


/**
 * # 区块模型
 *
 * @author AfeiBaili
 * @version 2026/8/31 13:20
 */

class ChunkModel(val chunk: Chunk, val blocks: Array<BlockModel>) {
    var changed = true
    val positionBuffer = BufferUtils.createByteBuffer(WorldRenderer.INSTANCE_SIZE_BYTE.toInt())
    val uvBuffer = BufferUtils.createByteBuffer(WorldRenderer.UV_SIZE_BYTE.toInt())

    fun updatePositionBuffer() {
        positionBuffer.clear()
        blocks.forEach { blockModel ->
            positionBuffer.putInt(blockModel.x)
            positionBuffer.putInt(blockModel.y)
        }
        positionBuffer.flip()
    }

    fun update() {
        if (chunk.changed || changed) {
            updatePositionBuffer()
            chunk.update()
            changed = false
        }
    }

    companion object {
        val blockImageList get() = TextureManager.blockImageList
        val blockBigImageAtlas get() = TextureManager.blockBigImageAtlas

        fun of(chunk: Chunk): ChunkModel {
            return ChunkModel(chunk, buildBlockModel(chunk))
        }

        fun buildBlockModel(chunk: Chunk): Array<BlockModel> {
            val blockTypeModelMap = mutableMapOf<String, BlockModelType>()
            val blockModelList = mutableListOf<BlockModel>()

            chunk.blocks.forEach { block ->
                val uvMap: Map<String, BlockUv> = blockImageList.toBlockUvMap(blockBigImageAtlas)
                val blockUv: BlockUv = uvMap[block.id]
                    ?: uvMap[Blocks.ERROR.blockType.id]
                    ?: error("找不到错误纹理, 在寻找 ${block.type.identifier} 中")

                var blockModelType: BlockModelType? = blockTypeModelMap[block.id]
                if (blockModelType == null) {
                    blockTypeModelMap[block.id] = BlockModelType.register(
                        block.type.identifier, blockUv
                    )
                }
                blockModelType = blockTypeModelMap[block.id]!!

                val model = BlockModel(block.x, block.y, blockModelType)
                blockModelList.add(model)
            }

            return blockModelList.toTypedArray()
        }
    }
}