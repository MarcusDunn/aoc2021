fun solve(input: CharSequence) = input
    .lines()
    .asSequence()
    .map { it.trim() }
    .mapNotNull { it.toIntOrNull() }
    .windowed(2, partialWindows = false)
    .fold(0) { acc, list ->
        if (list[1] > list[0]) {
            acc + 1
        } else {
            acc
        }
    }

fun solve2(input: CharSequence) = input
    .lines()
    .asSequence()
    .map { it.trim() }
    .mapNotNull { it.toIntOrNull() }
    .windowed(3)
    .map { list -> list.fold(0) { acc, i -> acc + i } }
    .windowed(2)
    .fold(0) { acc, list ->
        if (list[0] < list[1]) {
            acc + 1
        } else {
            acc
        }
    }
