package day8

import advent.Puzzle
import java.io.File

/**
 * # Day 8: Two-Factor Authentication
 *
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk).
 * Then, it displays a code on a little screen, and you type that code on a keypad.
 * Then, presumably, the door unlocks.
 *
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything
 * apart and figured out how it works. Now you just have to work out what the screen would have displayed.
 *
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen;
 * these instructions are your puzzle input.
 * The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is capable of
 * three somewhat peculiar operations:
 *
 *  - `rect AxB` turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
 *  - `rotate row y=A by B` shifts all of the pixels in row A (0 is the top row) right by B pixels.
 *    Pixels that would fall off the right end appear at the left end of the row.
 *  - `rotate column x=A by B` shifts all of the pixels in column A (0 is the left column) down by B pixels.
 *    Pixels that would fall off the bottom appear at the top of the column.
 *
 * There seems to be an intermediate check of the voltage used by the display:
 * after you swipe your card, if the screen did work, how many pixels should be lit?
 */
object Day8 : Puzzle() {

    sealed class Cmd {
        data class Rect(val width: Int, val height: Int) : Cmd()
        data class RotateRow(val row: Int, val shift: Int) : Cmd()
        data class RotateCol(val col: Int, val shift: Int) : Cmd()

        companion object {
            fun from(s: String) =
                    Regex("""rect (\d+)x(\d+)""").matchEntire(s)?.destructured
                            ?.let { (w, h) -> Rect(w.toInt(), h.toInt()) } ?:
                    Regex("""rotate row y=(\d+) by (\d+)""").matchEntire(s)?.destructured
                            ?.let { (row, shift) -> RotateRow(row.toInt(), shift.toInt()) } ?:
                    Regex("""rotate column x=(\d+) by (\d+)""").matchEntire(s)?.destructured
                            ?.let { (col, shift) -> RotateCol(col.toInt(), shift.toInt()) } ?:
                    error("Can't parse $s")
        }
    }

    class Screen(val width: Int, val height: Int) {

        val pixels = Array(height) { Array(width) { false } }
        val row = Array(width) { false }
        val column = Array(height) { false }

        fun format(space: String = ".") = pixels.joinToString("\n", prefix = "\n") {
            it.joinToString("") { if (it) "#" else space }
        }

        tailrec fun x(x: Int): Int = if (x >= 0) x % width else x(x + width)
        tailrec fun y(y: Int): Int = if (y >= 0) y % height else y(y + height)

        operator fun get(x: Int, y: Int) = pixels[y(y)][x(x)]
        operator fun set(x: Int, y: Int, v: Boolean) {
            pixels[y(y)][x(x)] = v
        }

        fun updateRow(y: Int, f: Screen.(Int) -> Boolean) {
            row.indices.forEach {
                row[it] = f(it)
            }
            row.forEachIndexed { i, v ->
                pixels[y % height][i] = v
            }
        }
        fun updateCol(x: Int, f: Screen.(Int) -> Boolean) {
            column.indices.forEach {
                column[it] = f(it)
            }
            column.forEachIndexed { i, v ->
                pixels[i][x % width] = v
            }
        }

        fun apply(cmd: Cmd) = when (cmd) {
            is Cmd.Rect -> (0..cmd.width-1).forEach { x ->
                (0..cmd.height-1).forEach { y ->
                    this[x, y] = true
                }
            }
            is Cmd.RotateRow -> updateRow(cmd.row) { get(it - cmd.shift, cmd.row) }
            is Cmd.RotateCol -> updateCol(cmd.col) { get(cmd.col, it - cmd.shift) }
        }.let {
            this
        }

        val litCount get() = pixels.sumBy { it.count { it } }
    }

    fun testScreen() {
        val screen = Screen(7, 3)
        screen.apply(Cmd.from("rect 3x2"))
        screen.format() produces """|
                   |###....
                   |###....
                   |.......""".trimMargin()
        screen.apply(Cmd.from("rotate column x=1 by 1"))
        screen.format() produces """|
                   |#.#....
                   |###....
                   |.#.....""".trimMargin()
        screen.apply(Cmd.from("rotate row y=0 by 4"))
        screen.format() produces """|
                   |....#.#
                   |###....
                   |.#.....""".trimMargin()
        screen.apply(Cmd.from("rotate column x=1 by 1"))
        screen.format() produces """|
                   |.#..#.#
                   |#.#....
                   |.#.....""".trimMargin()
    }

    val input = File("src/day8/input.txt").readLines().map { Cmd.from(it) }

    /**
     * how many pixels should be lit?
     */
    override fun part1() = input.fold(Screen(50, 6)) { s, c -> s.apply(c) }.litCount

    /**
     * You notice that the screen is only capable of displaying capital letters;
     * in the font it uses, each letter is 5 pixels wide and 6 tall.
     * After you swipe your card, what code is the screen trying to display?
     */
    override fun part2() = input.fold(Screen(50, 6)) { s, c -> s.apply(c) }.format(" ")

    @JvmStatic fun main(vararg args: String) = Day8()
}













