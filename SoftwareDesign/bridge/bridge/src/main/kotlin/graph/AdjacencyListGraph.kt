package graph

import api.DrawingApi

class AdjacencyListGraph(drawingApi: DrawingApi, private val adjacencyListGraph: List<List<Int>>) : Graph(drawingApi) {
    override val size: Int
        get() = adjacencyListGraph.size

    override fun drawGraph() {
        adjacencyListGraph.forEachIndexed { vertex, ends ->
            drawVertex(vertex)
            ends.forEach { end ->
                drawEdge(vertex, end)
            }
        }
    }
}