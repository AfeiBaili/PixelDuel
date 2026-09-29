package cn.afeibaili.jump.common.resource

import cn.afeibaili.jump.common.Identifier
import cn.afeibaili.jump.common.block.Block
import cn.afeibaili.jump.common.block.BlockType
import cn.afeibaili.jump.common.block.Blocks
import cn.afeibaili.jump.common.exception.IdentifierException
import cn.afeibaili.jump.common.exception.KeyException
import cn.afeibaili.jump.common.util.logger
import cn.afeibaili.jump.common.world.World

/**
 * # 地图解析器，解析字符串为地图信息
 *
 * 并根据键值对先获取字符和id的映射关系。通过使用字符绘制地图，使用 **WorldLoader** 映射为地图对象
 * 用{关键字}来配置地图相关信息
 *
 * ```xxx.world
 * # 定义地图名
 * {name}=world name
 * # 注释，忽略行
 * # 键值对用来设置字符与方块ID映射关系
 * # 放到地图前
 * D=dirt
 * G=grass_dirt
 * # 拼接地图
 * GGG     GGGG
 * DD      DDDD
 *      GGGDDDD
 *      DDDDDDD
 * GG     DDDDD
 * DD     DDDDD
 * DDGGGGGDDDDD
 * ```
 *
 * @author AfeiBaili
 * @version 2026/6/3 12:48
 */

@Deprecated("弃用使用文本解析地图数据")
class WorldParser {
    private val logger = logger { "WorldParser" }

    /**
     * 解析地图内容，并转换为对象
     *
     * @param text 地图文件内容
     *
     * @return 地图对象
     *
     * @see World
     */
    fun parse(text: String): World {
        var worldName = "unnamed"
        val charBlockMap = HashMap<Char, Identifier>()
        val blocks = ArrayList<ArrayList<Block>>()
        var rowLine = 0

        /**
         * 解析键值对行
         *
         * @param key 键
         * @param value 值
         */
        fun parseKeyAndValue(key: String, value: String) {
            val key = key.trim()
            val value = value.trim().lowercase()

            if ("\\{.*}".toRegex().matches(key)) {
                val v: String? = "\\{(\\w+)}".toRegex().find(key)?.groups?.get(1)?.value
                if (v == null) throw KeyException("keyword is null: $key")

                when (v.trim()) {
                    "name" -> worldName = value
                }
            } else {
                val key = key
                if (key.length != 1) throw KeyException("key not's char: $key")
                charBlockMap[key[0]] = Identifier("block", value)
            }
        }

        //// MAIN PARSE ////
        text.lines().forEach { line ->
            if (line.trimStart().startsWith("#")) return@forEach
            val line = if (line.indexOf('#') != -1) line.split("#")[0] else line

            if (line.indexOf('=') != -1) {
                val split: List<String> = line.split("=")
                if (split.size == 2) {
                    val (key, value) = split[0] to split[1]
                    return@forEach parseKeyAndValue(key, value)
                }
            }


            val blockRow = ArrayList<Block>()
            line.forEachIndexed { indexX, char ->
                val identifier: Identifier? = charBlockMap[char]
                if (char == ' ') {
                    blockRow.add(Block(indexX, rowLine, Blocks.AIR.blockType))
                    return@forEachIndexed
                }
                if (identifier == null) throw IdentifierException("标识符为空，未知的char: $char")
                blockRow.add(Block(indexX, rowLine, BlockType.getBlockTypeById(identifier)))
            }
            blocks.add(blockRow)
            rowLine--
        }

        logger.info("[$worldName] world is load")
        return World(worldName).also { logger.debug(it) }
    }
}