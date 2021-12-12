import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("src/main/resources/input.txt").readText()
    val middle = input.lines()
        .map { it.split("-") }
        .map { list -> list.first() to list.last() }
        .map { (fst, lst) -> Cave.fromString(fst) to Cave.fromString(lst) }
    val graph = Graph(middle + middle.map { it.second to it.first }
        .filterNot { it.first is Cave.End || it.second is Cave.Start })
    println(graph.paths().also { println(it.joinToString(separator = "\n")) }.size)
}

sealed class Cave {
    abstract val name: String
    abstract val maxVisits: Int

    companion object {
        fun fromString(name: String): Cave = when {
            name == "start" -> Start
            name == "end" -> End
            name.all { it.isUpperCase() } -> Big(name)
            name.all { it.isLowerCase() } -> Small(name)
            else -> throw Exception(name)
        }
    }

    object Start : Cave() {
        override val name = "start"
        override val maxVisits = 1
    }

    object End : Cave() {
        override val name = "end"
        override val maxVisits = 1
    }

    open class Big(override val name: String) : Cave() {
        override val maxVisits = Int.MAX_VALUE
    }

    open class Small(override val name: String) : Cave() {
        override val maxVisits = 2
    }

    override fun toString() = name
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cave

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

data class Graph(val edges: List<Pair<Cave, Cave>>) {
    private val nodes = edges.flatMap { listOf(it.first, it.second) }.distinct()

    fun paths(): List<List<Cave>> {
        var prev = emptyList<List<Cave>>()
        var curr: List<List<Cave>> = nodes.filterIsInstance<Cave.Start>().map { listOf(it) }
        while (prev != curr) {
            prev = curr
            curr = (curr.flatMap { next(it) } + curr)
                .filter { path ->
                    val caveGroups = path.groupBy { it }
                    caveGroups.all { if (it.key is Cave.Start || it.key is Cave.End) it.value.size == 1 else true }
                            &&
                            caveGroups.count { it.key is Cave.Small && it.value.size >= 2 } <= 1
                            &&
                            caveGroups.filter { it.key is Cave.Small && it.value.size > 2 }.isEmpty()
                }
                .distinct()
        }
        return curr.filter { it.last() is Cave.End }
    }

    private fun next(path: List<Cave>): List<List<Cave>> {
        val whereWeAtTho = path.last()
        val validNextEdges = edges
            .filter { (fst, _) -> fst == whereWeAtTho }
        val map: List<List<Cave>> = validNextEdges.map { path + it.second }.distinct()
        return map
    }
}