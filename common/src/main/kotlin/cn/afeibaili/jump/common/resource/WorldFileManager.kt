package cn.afeibaili.jump.common.resource

import cn.afeibaili.jump.common.world.Chunk
import cn.afeibaili.jump.common.world.World
import java.io.File


/**
 * # 地图文件管理器
 *
 * @author AfeiBaili
 * @version 2026/10/14 02:06
 */

object WorldFileManager {
    val worldDirPath = "${System.getProperty("user.dir")}/resource/world"

    fun getWorldResource(childName: String): File {
        return File(worldDirPath, childName)
    }

    private object ChunkManager {
        fun saveChunk(world: World, chunkX: Int, chunkY: Int) {
            val chunkFile = getWorldResource("${world.name}/${chunkX}-${chunkY}.ck")
            chunkFile.parentFile.mkdirs()
            chunkFile.createNewFile()
            //todo

        }

        fun loadChunk(worldName: String, chunk: Chunk) {

        }

        fun toChunkBytes(chunk: Chunk): ByteArray {
            TODO()
        }
    }
}