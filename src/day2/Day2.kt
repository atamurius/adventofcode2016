package day2

import java.io.File
import day2.Dir.*

/* # Day 2: Bathroom Security
 *
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written down.
 * Instead, please memorize and follow the procedure below to access the bathrooms."
 *
 * The document goes on to explain that each button to be pressed can be found by starting
 * on the previous button and moving to adjacent buttons on the keypad:
 * U moves up, D moves down, L moves left, and R moves right.
 * Each line of instructions corresponds to one button, starting at the previous button
 * (or, for the first line, the "5" button);
 * press whatever button you're on at the end of each line.
 * If a move doesn't lead to a button, ignore it.
 *
 * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?
 */

enum class Dir(val dx: Int,
               val dy: Int) {
    U(0,-1), D(0,+1), L(-1,0), R(+1,0)
}

class Keypad(val keys: List<String>,
             var x: Int,
             var y: Int) {

    val width = keys.first().length - 1
    val height = keys.size - 1

    fun move(vararg dirs: Dir): Char {
        dirs.forEach {
            x = minOf(width, maxOf(0, x + it.dx))
            y = minOf(height, maxOf(0, y + it.dy))
            if (keys[y][x] == ' ') {
                when (it) {
                    U, D -> y -= it.dy
                    L, R -> x -= it.dx
                }
            }
        }
        return keys[y][x]
    }
}

val KEYS = listOf(
        "123",
        "456",
        "789")

val KEYS2 = listOf(
        "  1  ",
        " 234 ",
        "56789",
        " ABC ",
        "  D  ")

fun String.toDirs() = toCharArray().map { Dir.valueOf("$it") }.toTypedArray()

fun main(args: Array<String>) {
    val dirs = File("src/day2/input.txt").readLines().map { it.toDirs() }
    val keypad = Keypad(KEYS, 1, 1)
    println("Part 1: ${dirs.map { keypad.move(*it) }.joinToString("")}")
    val keypad2 = Keypad(KEYS2, 0, 2)
    println("Part 2: ${dirs.map { keypad2.move(*it) }.joinToString("")}")
}