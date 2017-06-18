package day2

import advent.Puzzle
import java.io.File
import day2.Day2.Dir.*

/**
 * # Day 2: Bathroom Security
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
object Day2 : Puzzle() {

    enum class Dir(val dx: Int,
                   val dy: Int) {
        U(0, -1), D(0, +1), L(-1, 0), R(+1, 0)
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

    fun testKeypad() {
        val keypad = Keypad(KEYS, 1, 1)
        keypad.move(U, L, L) produces '1'
        keypad.move(R, R, D, D, D) produces '9'
        keypad.move(L, U, R, D, L) produces '8'
        keypad.move(U, U, U, U, D) produces '5'
    }
    fun testKeypad2() {
        val keypad = Keypad(KEYS2, 0, 2)
        keypad.move(U, L, L) produces '5'
        keypad.move(R, R, D, D, D) produces 'D'
        keypad.move(L, U, R, D, L) produces 'B'
        keypad.move(U, U, U, U, D) produces '3'
    }

    fun String.toDirs() = toCharArray().map { Dir.valueOf("$it") }.toTypedArray()

    val dirs = File("src/day2/input.txt").readLines().map { it.toDirs() }

    override fun part1() = dirs.map { Keypad(KEYS, 1, 1).move(*it) }.joinToString("")
    override fun part2() = dirs.map { Keypad(KEYS2, 0, 2).move(*it) }.joinToString("")
}
fun main(args: Array<String>) = Day2()