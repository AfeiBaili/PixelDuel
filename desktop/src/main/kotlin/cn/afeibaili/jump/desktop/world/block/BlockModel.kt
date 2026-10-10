package cn.afeibaili.jump.desktop.world.block

/**
 * # 方块实例
 *
 * @author AfeiBaili
 * @version 2026/6/5 22:43
 */

class BlockModel(val x: Int, val y: Int, var type: BlockModelType) {
    val id get() = type.identifier.id
}