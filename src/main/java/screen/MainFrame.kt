import logic.entity.Goal
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.util.*
import kotlin.concurrent.schedule
import javax.swing.JFrame
import javax.swing.JPanel
import kotlin.math.roundToInt

const val TIMER = 5L

fun main(){
    MainFrame()
}

class MainFrame: JFrame() {
    val panel = GamePanel()

    init {
        background = Color.black
        setLocation(450,50)
        isVisible = true
        add(panel)
        pack()
    }
}

class GamePanel: JPanel() {
    val game = GameWorld()

    init {
        preferredSize = Dimension(MAP_SIZE.toInt(),MAP_SIZE.toInt())

        Timer().schedule(TIMER, TIMER) {
            game.tick()
            repaint()
        }
    }

    override fun paint(g: Graphics) {
        g.color = Color.black
        g.fillRect(0, 0, width, height)

        g.color = Color.GREEN
        for (base in game.bases)
            g.fillCircle(base.x.roundToInt(), base.y.roundToInt(), BASE_R)

        g.color = Color.RED
        for (source in game.sources)
            g.fillCircle(source.x.roundToInt(), source.y.roundToInt(), SOURCE_R)

        for (worker in game.workers) {
            if (worker.goal == Goal.SOURCE)
                g.color = Color.WHITE
            else
                g.color = Color.BLUE
            g.fillRect(worker.x.roundToInt() - 1, worker.y.roundToInt() - 1, 3, 3)
        }
    }
}

private fun Graphics.fillCircle(x: Int, y: Int, r: Int) {
    fillOval(x-r/2, y-r/2, r, r)
}
