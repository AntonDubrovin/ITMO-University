package graph

import api.DrawingApi

class MatrixGraph(drawingApi: DrawingApi, private val graph: List<List<Int>>) : Graph(drawingApi) {
    override val size: Int
        get() = graph.size

    override fun drawGraph() = graph.forEachIndexed { vertex, ends ->
        drawVertex(vertex)
        ends.forEachIndexed { endIndex, isExist ->
            if (isExist == 1) {
                drawEdge(vertex, endIndex)
            }
        }
    }
}