import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val sorted = buildList {
        Path("src/main/resources/input.txt").useLines { lines ->
            outer@ for (line in lines) {
                val stack = BracketStack()
                val brackets = line.map { Bracket.fromChar(it) }
                for (bracket in brackets) {
                    if (!stack.add(bracket)) {
                        continue@outer
                    }
                }
                add(stack)
            }
        }
    }
        .map { it.score() }
        .sorted()
    println(sorted[sorted.size / 2])
}

class BracketStack {
    private val inner: MutableList<OpenBracket> = mutableListOf()
    private val neededStack
        get() = inner.map { it.closedCounterpart() }

    fun add(bracket: Bracket): Boolean = when (bracket) {
        is OpenBracket -> inner.add(bracket)
        is ClosedBracket -> if (inner.last().closedCounterpart() == bracket) {
            inner.removeLast()
            true
        } else {
            false
        }
    }

    fun score(): Long = neededStack.reversed().fold(0) { acc, closedBracket ->
        acc * 5 + when (closedBracket) {
            AngledClose -> 4
            RoundClose -> 1
            SquareClose -> 2
            SquigglyClose -> 3
        }
    }

}

sealed class Bracket {
    companion object {
        fun fromChar(char: Char) = when (char) {
            '(' -> RoundOpen
            ')' -> RoundClose
            '[' -> SquareOpen
            ']' -> SquareClose
            '{' -> SquigglyOpen
            '}' -> SquigglyClose
            '<' -> AngledOpen
            '>' -> AngledClose
            else -> throw Exception()
        }
    }

    private fun toChar(): Char = when (this) {
        AngledClose -> '>'
        RoundClose -> ')'
        SquareClose -> ']'
        SquigglyClose -> '}'
        AngledOpen -> '<'
        RoundOpen -> '('
        SquareOpen -> '['
        SquigglyOpen -> '{'
    }

    override fun toString() = this.toChar().toString()
}

sealed class OpenBracket : Bracket() {
    fun closedCounterpart() = when (this) {
        RoundOpen -> RoundClose
        SquigglyOpen -> SquigglyClose
        AngledOpen -> AngledClose
        SquareOpen -> SquareClose
    }
}

sealed class ClosedBracket : Bracket()

object RoundOpen : OpenBracket()
object RoundClose : ClosedBracket()
object SquareOpen : OpenBracket()
object SquareClose : ClosedBracket()
object SquigglyOpen : OpenBracket()
object SquigglyClose : ClosedBracket()
object AngledOpen : OpenBracket()
object AngledClose : ClosedBracket()
