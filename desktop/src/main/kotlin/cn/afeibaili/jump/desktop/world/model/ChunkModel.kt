package cn.afeibaili.jump.desktop.world.model

import cn.afeibaili.gl.render.WorldRenderer
import cn.afeibaili.jump.common.block.Block
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

class ChunkModel(val chunk: Chunk, var blocks: Array<BlockModel>) {
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

    fun updateUv() {
        var isUpdateUv = false
        for (model in blocks) {
            if (model.type.uv.changed) {
                isUpdateUv = true
                break
            }
        }
        if (isUpdateUv) updateUvBuffer()
    }

    fun updateUvBuffer() {
        uvBuffer.clear()
        blocks.forEach { blockModel ->
            val uvs: FloatArray = blockModel.type.uv.get()
            uvBuffer.putFloat(uvs[0])
            uvBuffer.putFloat(uvs[1])
            uvBuffer.putFloat(uvs[2])
            uvBuffer.putFloat(uvs[3])
        }
        uvBuffer.flip()
    }

    fun update() {
        if (chunk.changed || changed) {
            updateBlockModel(chunk.blocks, blocks)
            updatePositionBuffer()
            updateUvBuffer()
            chunk.update()
            changed = false
        }
        updateUv()
    }

    companion object {
        val blockImageList get() = TextureManager.blockImageList
        val blockBigImageAtlas get() = TextureManager.blockBigImageAtlas
        val uvMap: Map<String, BlockUv> = blockImageList.toBlockUvMap(blockBigImageAtlas)
        val blockTypeModelMap = mutableMapOf<String, BlockModelType>()

        fun of(chunk: Chunk): ChunkModel {
            return ChunkModel(chunk, buildBlockModel(chunk))
        }

        fun updateBlockModel(blocks: Array<Block>, outBlockModel: Array<BlockModel>) {
            for ((index, block) in blocks.withIndex()) {
                val blockModel = outBlockModel[index]

                val blockUv: BlockUv = uvMap[block.id] ?: uvMap[Blocks.ERROR.blockType.id]
                ?: error("找不到错误纹理, 在寻找 ${block.type.identifier} 中")

                val blockModelType: BlockModelType = blockTypeModelMap[block.id] ?: let {
                    BlockModelType.register(block.type.identifier, blockUv).apply {
                        blockTypeModelMap[block.id] = this
                    }
                }

                blockModel.type = blockModelType
            }
        }

        fun buildBlockModel(chunk: Chunk): Array<BlockModel> {
            val blockModelList = mutableListOf<BlockModel>()

            chunk.blocks.forEach { block ->
                val blockUv: BlockUv = uvMap[block.id] ?: uvMap[Blocks.ERROR.blockType.id]
                ?: error("找不到错误纹理, 在寻找 ${block.type.identifier} 中")

                val blockModelType: BlockModelType = blockTypeModelMap[block.id] ?: let {
                    BlockModelType.register(block.type.identifier, blockUv).apply {
                        blockTypeModelMap[block.id] = this
                    }
                }
                val model = BlockModel(block.x, block.y, blockModelType)
                blockModelList.add(model)
            }

            return blockModelList.toTypedArray()
        }
    }
}