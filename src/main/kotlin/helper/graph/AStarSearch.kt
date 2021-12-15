package helper.graph

import java.util.*

/**
 * Implements A* search to find the shortest path between two vertices
 */
inline fun <K> findShortestPath(start: K, end: K, neighbours: (K) -> Iterable<K>, cost: (K, K) -> Int, heuristic: (K) -> Int = { 0 }): GraphSearchResult<K> {
    val toVisit = PriorityQueue(listOf(ScoredVertex(start, 0, heuristic(start))))
    val seenPoints: MutableMap<K, SeenVertex<K>> = mutableMapOf(start to SeenVertex(0, null))

    while (!seenPoints.containsKey(end)) {
        val (currentVertex, currentScore) = toVisit.remove()
        val nextPoints = neighbours(currentVertex)
            .filter { it !in seenPoints }
            .map { next -> ScoredVertex(next, currentScore + cost(currentVertex, next), heuristic(next)) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.vertex to SeenVertex(it.score, currentVertex) })
    }

    return GraphSearchResult(seenPoints)
}

class GraphSearchResult<K>(private val result: Map<K, SeenVertex<K>>) {
    fun getScore(vertex: K) = result[vertex]?.score ?: throw IllegalStateException("Result for $vertex not available")
}

data class SeenVertex<K>(val score: Int, val prev: K?)

data class ScoredVertex<K>(val vertex: K, val score: Int, val heuristic: Int) : Comparable<ScoredVertex<K>> {
    override fun compareTo(other: ScoredVertex<K>): Int = (score + heuristic).compareTo(other.score + heuristic)
}