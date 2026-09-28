import cn.afeibaili.gl.image.ImageUtil
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.test.Test

/**
 *
 *
 * @author AfeiBaili
 * @version 2026/9/28 11:09
 */

class TestImage {
    @Test
    fun test01(){
        val image: BufferedImage = ImageUtil.loadImage("B:\\Java\\Kotlin\\PixelDuel\\resource\\block\\dirt.png")
        val extendSide: BufferedImage = ImageUtil.extendSide(image, 1)
        ImageIO.write(extendSide, "png", File("B:\\Java\\Kotlin\\Engine2D\\temp\\test.png"))
    }
}