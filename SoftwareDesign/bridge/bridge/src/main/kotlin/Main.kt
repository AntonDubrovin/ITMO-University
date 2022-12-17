import api.AwtDrawingApi
import api.FxDrawingApi
import graph.AdjacencyListGraph
import graph.MatrixGraph

fun main(args: Array<String>) {
    val matrixGraph = listOf(
        listOf(0, 1, 0, 1, 0, 1),
        listOf(1, 0, 1, 1, 0, 1),
        listOf(0, 1, 0, 0, 0, 1),
        listOf(1, 1, 0, 0, 0, 1),
        listOf(0, 0, 0, 0, 0, 0),
        listOf(1, 1, 1, 1, 0, 0)
    )
    val adjacencyListGraph = listOf(
        listOf(1, 2), listOf(2, 3), listOf(3, 4), listOf(4, 5), listOf(5, 6), listOf(6, 7), listOf(1, 7)
    )

    val drawingApiType = args[0]
    val graphType = args[1]

    val drawingApi = when (drawingApiType) {
        "awt" -> AwtDrawingApi
        "fx" -> FxDrawingApi
        else -> error("Incorrect drawing api")
    }

    val graph = when (graphType) {
        "matrix" -> MatrixGraph(
            drawingApi, matrixGraph
        )

        "adjacency" -> AdjacencyListGraph(
            drawingApi, adjacencyListGraph
        )

        else -> error("Incorrect graph type")
    }

    graph.draw()
}