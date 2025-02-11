import java.util.*

interface Node

data class Edge(val node1: Node, val node2: Node, val distance: Int)

fun dijkstraWithLoops(graph: Map<Node, List<Pair<Node, Int>>>, start: Node): Map<Node, Int> {
    val distances = mutableMapOf<Node, Int>().withDefault { Int.MAX_VALUE }
    val priorityQueue = PriorityQueue<Pair<Node, Int>>(compareBy { it.second })
    val visited = mutableSetOf<Pair<Node, Int>>()

    priorityQueue.add(start to 0)
    distances[start] = 0

    while (priorityQueue.isNotEmpty()) {
        val (node, currentDist) = priorityQueue.poll()
        if (visited.add(node to currentDist)) {
            graph[node]?.forEach { (adjacent, weight) ->
                val totalDist = currentDist + weight
                if (totalDist < distances.getValue(adjacent)) {
                    distances[adjacent] = totalDist
                    priorityQueue.add(adjacent to totalDist)
                }
            }
        }
    }
    return distances
}

fun bfs(graph: Map<Node, List<Node>>, start: Node, end:Node): Set<Node> {
    val visited = mutableSetOf<Node>()
    val queue = ArrayDeque<Node>()
    queue.add(start)
    while (queue.isNotEmpty() && queue.first() != end) {
        val vertex = queue.removeFirst()
        if (vertex !in visited) {
            visited.add(vertex)
            graph[vertex]?.let { neighbors -> queue.addAll(neighbors.filterNot { it in visited }) }
        }
    }
    return visited
}

/**
 * See https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 */
fun findShortestPath(edges: List<Edge>, source: Node, target: Node): ShortestPathResult {

    // Note: this implementation uses similar variable names as the algorithm given do.
    // We found it more important to align with the algorithm than to use possibly more sensible naming.

    val dist = mutableMapOf<Node, Int>()
    val prev = mutableMapOf<Node, Node?>()
    val q = findDistinctMazeNodes(edges)

    q.forEach { v ->
        dist[v] = Integer.MAX_VALUE
        prev[v] = null
    }
    dist[source] = 0

    while (q.isNotEmpty()) {
        val u = q.minByOrNull { dist[it] ?: 0 }
        q.remove(u)

        if (u == target) {
            break // Found shortest path to target
        }
        edges
            .filter { it.node1 == u }
            .forEach { edge ->
                val v = edge.node2
                val alt = (dist[u] ?: 0) + edge.distance
                if (alt < (dist[v] ?: 0)) {
                    dist[v] = alt
                    prev[v] = u
                }
            }
    }

    return ShortestPathResult(prev, dist, source, target)
}

private fun findDistinctMazeNodes(edges: List<Edge>): MutableSet<Node> {
    val nodes = mutableSetOf<Node>()
    edges.forEach {
        nodes.add(it.node1)
        nodes.add(it.node2)
    }
    return nodes
}

/**
 * Traverse result
 */
class ShortestPathResult(val prev: Map<Node, Node?>, val dist: Map<Node, Int>, val source: Node, val target: Node) {

    fun shortestPath(from: Node = source, to: Node = target, list: List<Node> = emptyList()): List<Node> {
        val last = prev[to] ?: return if (from == to) {
            list + to
        } else {
            emptyList()
        }
        return shortestPath(from, last, list) + to
    }

    fun shortestDistance(): Int? {
        val shortest = dist[target]
        if (shortest == Integer.MAX_VALUE) {
            return null
        }
        return shortest
    }
}