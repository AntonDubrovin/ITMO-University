package api

import javafx.application.Application
import javafx.application.Application.launch
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import javafx.stage.Stage


object FxDrawingApi : DrawingApi {
    val shapes = mutableListOf<Shape>()

    override fun drawCircle(center: Point, radius: Double) {
        shapes.add(Circle(center.x, center.y, radius).apply {
            fill = Color.BLACK
            stroke = Color.BLUE
        })
    }

    override fun drawLine(from: Point, to: Point) {
        shapes.add(Line(from.x, from.y, to.x, to.y).apply {
            stroke = Color.BLUE
        })
    }

    override fun draw() = launch(FXApplication::class.java)
}

internal class FXApplication : Application() {
    override fun start(primaryStage: Stage?) {
        val rootView = Group()
        primaryStage?.apply {
            title = "DRAWING API FX"
            scene = Scene(
                rootView,
                DrawingApi.SIZE.toDouble(),
                DrawingApi.SIZE.toDouble(),
            )
            rootView.children.addAll(FxDrawingApi.shapes)
            show()
        }
    }
}