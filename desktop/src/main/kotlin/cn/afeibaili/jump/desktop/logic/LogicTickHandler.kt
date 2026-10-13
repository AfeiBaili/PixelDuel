package cn.afeibaili.jump.desktop.logic


/**
 * # Tick处理器
 *
 * 在实现类中需要使用addHandler方法进行添加至TickHandler.all中，否则不生效
 *
 * ```
 * // children class
 * init {
 *     TickHandler.addHandler(this)
 * }
 * ```
 *
 * @author AfeiBaili
 * @version 2026/6/28 00:30
 */

interface LogicTickHandler {
    companion object {
        val all = mutableListOf<LogicTickHandler>()

        fun addHandler(handler: LogicTickHandler) {
            all.add(handler)
        }
    }

    fun tick()
}