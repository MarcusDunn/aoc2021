import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val matrix = Matrix.fromString(Path("src/main/resources/input.txt").readText())
    val basinOrigins = matrix.iterIndexed { height, x, y ->
        matrix.getAdjacent(x, y).all { p -> height < p.height }
    }
    val x = matrix.iterIndexed { height, x, y ->
        if (basinOrigins[x][y]) {
            var prev = Basin(listOf(Point(height, x, y)))
            var curr = prev.expand(matrix)
            while (prev.size != curr.size) {
                prev = curr
                curr = curr.expand(matrix)
            }
            curr.sortedBy { it.x * curr.size + it.y }
        } else {
            null
        }
    }
        .asSequence()
        .flatten()
        .filterNotNull()
        .sortedByDescending { it.size }
        .distinctBy { setOf(it) }
        .take(3)
        .toList()
    println(x.map { it.size }.reduce(Int::times))
}

data class Basin(private val inner: List<Point>) : List<Point> by inner {
    fun expand(mat: Matrix): Basin {
        val basin = inner
            .flatMap { (_, x, y) ->
                mat
                    .getAdjacent(x, y)
                    .filterNot { it.height == 9 }
            }
            .plus(inner)
            .filterNot { it.height == 9 }
            .distinctBy { it.x to it.y }
        return Basin(basin)
    }
}

data class Point(val height: Int, val x: Int, val y: Int)

interface Parser<T> {
    fun fromString(string: String): T
}

data class Matrix(
    private val inner: List<List<Int>>
) : List<List<Int>> by inner {
    companion object : Parser<Matrix> {
        override fun fromString(string: String) =
            Matrix(string.lineSequence().map { it.toCharArray().map { char -> char.digitToInt() } }.toList())
    }

    fun getAdjacent(x: Int, y: Int): List<Point> = listOfNotNull(
        inner.getOrNull(x)?.getOrNull(y - 1)?.let { Point(it, x, y - 1) },
        inner.getOrNull(x)?.getOrNull(y + 1)?.let { Point(it, x, y + 1) },
        inner.getOrNull(x + 1)?.getOrNull(y)?.let { Point(it, x + 1, y) },
        inner.getOrNull(x - 1)?.getOrNull(y)?.let { Point(it, x - 1, y) },
    )

    fun <T> iterIndexed(f: (Int, Int, Int) -> T): List<List<T>> =
        inner.indices.map { x -> inner[x].indices.map { y -> f(inner[x][y], x, y) } }
}

fun <T> List<List<T>>.pp(f: (T) -> String = { it.toString() }) =
    println(this.joinToString("\n") { it.joinToString(separator = "", transform = f) })
