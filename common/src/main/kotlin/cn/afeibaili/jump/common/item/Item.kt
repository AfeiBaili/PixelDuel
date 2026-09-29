package cn.afeibaili.jump.common.item


/**
 * # 物品
 *
 * @author AfeiBaili
 * @version 2026/9/29 14:28
 */

open class Item(val quantity: Int, type: ItemType) {
    val id = type.identifier.id
}