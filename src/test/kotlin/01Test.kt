import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class `01Test` {
    @Test
    fun `1`() {
        assertEquals(1752, solve(File("src/test/resources/01-1.txt").readText()))
    }

    @Test
    fun `2`() {
        assertEquals(1781, solve2(File("src/test/resources/01-1.txt").readText()))
    }
}