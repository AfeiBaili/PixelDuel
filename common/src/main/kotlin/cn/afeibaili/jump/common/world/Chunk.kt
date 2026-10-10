package cn.afeibaili.jump.common.world

import cn.afeibaili.jump.common.block.Block
import cn.afeibaili.jump.common.block.BlockType
import cn.afeibaili.jump.common.block.Blocks
import java.util.*


/**
 * # 区块，地图区块
 *
 * 区块默认边长为32，可使用`createEmpty()`创建空区块，进行占位
 *
 * @param chunkX 区块X坐标
 * @param chunkY 区块Y坐标
 * @param blocks 存储的方块
 *
 * @author AfeiBaili
 * @version 2026/8/28 12:41
 */

class Chunk(
    val chunkX: Int,
    val chunkY: Int,
    val blocks: Array<Block>,
) {
    var changed = false

    fun update() {
        changed = false
    }

    fun getBlockAt(x: Int, y: Int): Block? {
        if (x < 0 || x >= CHUNK_SIDE || y < 0 || y >= CHUNK_SIDE) return null
        return blocks[y * CHUNK_SIDE + x]
    }

    fun setBlockAt(x: Int, y: Int, blockType: BlockType) {
        if (x < 0 || x >= CHUNK_SIDE || y < 0 || y >= CHUNK_SIDE) return
        val blockIndex: Int = y * CHUNK_SIDE + x
        val block: Block = blocks[blockIndex]
        if (block.type == blockType) return
        blocks[blockIndex] = Block((CHUNK_SIDE * chunkX) + x, (CHUNK_SIDE * chunkY) + y, blockType)
        changed = true
    }

    override fun hashCode(): Int {
        return Objects.hash(chunkX, chunkY)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Chunk) return false
        if (chunkX != other.chunkX) return false
        if (chunkY != other.chunkY) return false
        return true
    }

    companion object {
        fun createEmpty(chunkX: Int, chunkY: Int): Chunk {
            return createByBlock(chunkX, chunkY, Blocks.AIR.blockType)
        }

        fun createByBlock(chunkX: Int, chunkY: Int, blockType: BlockType): Chunk {
            val blocks = Array(CHUNK_SIDE * CHUNK_SIDE) { index ->
                Block(
                    (CHUNK_SIDE * chunkX) + index % CHUNK_SIDE,
                    (CHUNK_SIDE * chunkY) + index / CHUNK_SIDE,
                    blockType
                )
            }
            val chunk = Chunk(chunkX, chunkY, blocks)
            return chunk
        }

        const val CHUNK_SIDE = 16
    }
}