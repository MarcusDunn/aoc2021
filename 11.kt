import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val cavern = Path("src/main/resources/input.txt")
        .readLines()
        .mapIndexed { y, lines ->
            lines.mapIndexed { x, c -> Octopus.fromChar(c, x, y) }
        }
    var flashed = 0
    for (step in 0 until Int.MAX_VALUE) {
        println(cavern.joinToString("\n") { it.joinToString(separator = "") { it.energyLevel.toString() } } + "\n\n")
        cavern.forEach { it.forEach { it.tick() } } // increase energy level by 1
        val hasFlashed = mutableListOf<Octopus>()
        var willFlash = cavern.flatMap { it.filter { it.isGoingToFlash() } }
        while (willFlash.isNotEmpty()) {
            willFlash.forEach { it.flash(cavern) }
            hasFlashed += willFlash
            willFlash = cavern.flatMap { it.filter { it.isGoingToFlash() } }
            hasFlashed.forEach { it.energyLevel = 0 }
        }
        if (hasFlashed.size == cavern.sumOf { it.size }) {
            println(step + 1)
            break
        }
        flashed += hasFlashed.size
    }
    println(flashed)
}


data class Octopus(var energyLevel: Int, val x: Int, val y: Int) {
    companion object {
        fun fromChar(char: Char, x: Int, y: Int) = Octopus(char.digitToInt(), x, y)
    }

    fun tick() {
        energyLevel++
    }

    fun isGoingToFlash(): Boolean = energyLevel > 9

    fun flash(cavern: List<List<Octopus>>) {
        val adjacent = listOfNotNull(
            cavern.getOrNull(y),
            cavern.getOrNull(y + 1),
            cavern.getOrNull(y - 1)
        )
            .flatMap {
                listOfNotNull(
                    it.getOrNull(x),
                    it.getOrNull(x + 1),
                    it.getOrNull(x - 1)
                )
            }
        adjacent.forEach { it.energyLevel +=1 }
        energyLevel = 0
    }
}