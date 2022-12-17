package api

interface DrawingApi {
    val drawingWidth: Int
        get() = SIZE
    val drawingHeight: Int
        get() = SIZE

    fun drawCircle(center: Point, radius: Double)
    fun drawLine(from: Point, to: Point)

    fun draw()

    companion object {
        const val SIZE = 600
    }
}