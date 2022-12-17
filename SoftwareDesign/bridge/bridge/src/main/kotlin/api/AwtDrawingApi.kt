package api

import java.awt.Dimension
import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.system.exitProcess

object AwtDrawingApi : DrawingApi, Frame() {
    private val shapes = mutableListOf<Shape>()

    override fun drawCircle(center: Point, radius: Double) {
        shapes.add(
            Ellipse2D.Double(
                center.x - radius / 2, center.y - radius / 2, radius, radius
            )
        )
    }

    override fun drawLine(from: Point, to: Point) {
        shapes.add(
            Line2D.Double(
                from.x, from.y, to.x, to.y
            )
        )
    }

    override fun draw() {
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) = exitProcess(0)
        })

        title = "DRAWING API AWT"
        size = Dimension(drawingWidth, drawingHeight)
        isVisible = true
    }

    override fun paint(g: Graphics?) {
        val ga = g as Graphics2D
        shapes.forEach(ga::draw)
    }
}