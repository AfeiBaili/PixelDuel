package cn.afeibaili.jump.common.block

import cn.afeibaili.jump.common.Identifier
import cn.afeibaili.jump.common.util.logger


/**
 * # 方块类
 *
 *@author AfeiBaili
 *@version 2026/6/2 22:33
 */

data class BlockType(val identifier: Identifier) {
    val id get() = identifier.id

    /**
     * # 方块类型实例
     *
     * @author AfeiBaili
     * @version 2026/09/02 13:13
     */
    companion object {
        val all = HashMap<Identifier, BlockType>()
        private val logger = logger { "Blocks" }

        fun register(id: String): BlockType {
            val identifier = Identifier("block", id)
            logger.info("registering $identifier")
            return BlockType(identifier).also { all[identifier] = it }
        }

        fun getBlockTypeById(identifier: Identifier): BlockType {
            val blockType: BlockType? = all[identifier]
            if (blockType == null) {
                logger.warn("not found $identifier")
                return Blocks.ERROR.blockType
            }
            return blockType
        }
    }
}