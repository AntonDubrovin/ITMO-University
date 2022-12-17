package graph

import api.DrawingApi
import api.Point
import kotlin.math.cos
import kotlin.math.sin

abstract class Graph(private val drawingApi: DrawingApi) {
    private val centerHorizontal = drawingApi.drawingWidth / 2.0
    private val centerVertical = drawingApi.drawingHeight / 2.0

    private val centerPoint = Point(centerHorizontal, centerVertical)

    private val vertexRadius = 20.0
    private val vertexDistance = 200.0

    abstract val size: Int
    abstract fun drawGraph()

    fun draw() {
        drawGraph()
        drawingApi.draw()
    }

    fun drawVertex(vertex: Int) {
        val vertexPoint = getPointByIndex(vertex)
        drawingApi.drawCircle(vertexPoint, vertexRadius)
    }

    fun drawEdge(from: Int, to: Int) {
        val fromPoint = getPointByIndex(from)
        val toPoint = getPointByIndex(to)
        drawingApi.drawLine(fromPoint, toPoint)
    }

    private fun getPointByIndex(index: Int): Point {
        val angle = calculateAngle(index)
        val preRotated = Point(vertexDistance, 0.0)
        val rotated = preRotated.rotate(angle)
        return centerPoint + rotated
    }

    private operator fun Point.plus(other: Point) = Point(this.x + other.x, this.y + other.y)

    private fun Point.rotate(angle: Double): Point {
        val toRadians = angle * 2.0 * Math.PI / 360.0
        return Point(
            this.x * cos(toRadians) + this.y * sin(toRadians), y = -this.x * sin(toRadians) + this.y * cos(toRadians)
        )
    }

    private fun calculateAngle(index: Int) = 360.0 * (index.toDouble() / size.toDouble())
}