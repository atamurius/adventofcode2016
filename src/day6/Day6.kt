package day6

import java.io.File

/* # Day 6: Signals and Noise
 *
 * In this model, the same message is sent repeatedly.
 * You've recorded the repeating message signal (your puzzle input),
 * but the data seems quite corrupted - almost too badly to recover. Almost.
 *
 * All you need to do is figure out which character is most frequent for each position.
 */

fun freq(line: List<Char>): Map<Char, Int> =
        line.fold(mapOf<Char, Int>()) { acc, c ->
            acc + (c to ((acc[c] ?: 0) + 1))
        }

fun mostFreqLetters(lines: List<String>): String = lines.transposed()
        .map {
            freq(it)
                    .maxBy { it.value }!!
                    .key
        }.joinToString("")


fun leastFreqLetters(lines: List<String>): String = lines.transposed()
        .map {
            freq(it)
                    .minBy { it.value }!!
                    .key
        }.joinToString("")


fun List<String>.transposed(): List<List<Char>> =
        fold(List(this[0].length) { listOf<Char>() }) { cols, row ->
            cols.mapIndexed { i, col -> col + row[i] }
        }

fun main(args: Array<String>) {
    val lines = File("src/day6/input.txt").readLines()
    println("Part 1: ${lines.let(::mostFreqLetters)}")
    println("Part 2: ${lines.let(::leastFreqLetters)}")
}