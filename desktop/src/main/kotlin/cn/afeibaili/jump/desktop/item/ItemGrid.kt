package cn.afeibaili.jump.desktop.item

import cn.afeibaili.jump.common.item.Item


/**
 * # 物品格
 *
 * @author AfeiBaili
 * @version 2026/9/29 13:51
 */

class ItemGrid() {
    var item: Item? = null

    constructor(item: Item) : this() {
        this.item = item
    }
}