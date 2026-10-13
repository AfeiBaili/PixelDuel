package cn.afeibaili.jump.desktop.world.block

import cn.afeibaili.jump.common.exception.WorldException
import cn.afeibaili.jump.common.json.BlockInfo
import cn.afeibaili.jump.common.json.JSON_MAPPER
import cn.afeibaili.jump.common.resource.Loader
import cn.afeibaili.jump.common.resource.ResourceFileGetter
import java.io.File

/**
 * # 方块信息加载器
 *
 * @author AfeiBaili
 * @version 2026/7/3 01:19
 */

object BlockInfoLoader : Loader<Map<String, BlockInfo>> {

    /**
     * 从资源中加载方块纹理信息
     *
     * @return 返回以id为key，对象为BlockInfo的Map集合
     *
     * @see BlockInfo
     */
    override fun load(): Map<String, BlockInfo> {
        val map = mutableMapOf<String, BlockInfo>()

        val files: List<File> = ResourceFileGetter.getResourceFileList("block/info")
        files.filter { it.name.endsWith(".json") }.forEach { it ->
            val id: String = it.name.split(".").first()
            val info: BlockInfo = runCatching {
                JSON_MAPPER.readValue(it, BlockInfo::class.java)
            }.getOrElse { exception ->
                throw WorldException("无法加载BlockInfo文件[${it.canonicalPath}]: ${exception.message}")
            }

            map[id] = info
        }
        return map
    }
}