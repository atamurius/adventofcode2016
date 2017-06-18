package day6

import advent.Puzzle
import java.io.File

/**
 * # Day 6: Signals and Noise
 *
 * In this model, the same message is sent repeatedly.
 * You've recorded the repeating message signal (your puzzle input),
 * but the data seems quite corrupted - almost too badly to recover. Almost.
 *
 * All you need to do is figure out which character is most frequent for each position.
 */
object Day6 : Puzzle() {

    fun freq(line: List<Char>): Map<Char, Int> =
            line.fold(mapOf<Char, Int>()) { acc, c ->
                acc + (c to ((acc[c] ?: 0) + 1))
            }

    fun testTranspose() {
        listOf("abc", "def", "ghi").transposed() produces listOf(
                listOf('a', 'd', 'g'),
                listOf('b', 'e', 'h'),
                listOf('c', 'f', 'i'))
    }

    fun mostFreqLetters(lines: List<String>): String = lines.transposed()
            .map {
                freq(it)
                        .maxBy { it.value }!!
                        .key
            }.joinToString("")


    val lines = """
            |eedadn
            |drvtee
            |eandsr
            |raavrd
            |atevrs
            |tsrnev
            |sdttsa
            |rasrtv
            |nssdts
            |ntnada
            |svetve
            |tesnvt
            |vntsnd
            |vrdear
            |dvrsen
            |enarar""".trimMargin().lines()

    fun testMostFreqLetters() {
        mostFreqLetters(lines) produces "easter"
    }

    fun leastFreqLetters(lines: List<String>): String = lines.transposed()
            .map {
                freq(it)
                        .minBy { it.value }!!
                        .key
            }.joinToString("")

    fun testLeastFreqLetters() {
        leastFreqLetters(lines) produces "advent"
    }

    fun List<String>.transposed(): List<List<Char>> =
            fold(List(this[0].length) { listOf<Char>() }) { cols, row ->
                cols.mapIndexed { i, col -> col + row[i] }
            }

    val input = File("src/day6/input.txt").readLines()

    override fun part1() = mostFreqLetters(input)
    override fun part2() = leastFreqLetters(input)
}
fun main(vararg args: String) = Day6()
