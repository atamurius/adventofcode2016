package day3

import java.io.File

/* # Day 3: Squares With Three Sides
 *
 * The design document gives the side lengths of each triangle it describes,
 * but... 5 10 25? Some of these aren't triangles.
 * You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side.
 * For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, how many of the listed triangles are possible?
 */

data class Triangle(val a: Int, val b: Int, val c: Int) {
    val isPossible = (a + b) > c && (a + c) > b && (b + c) > a
}

fun String.toTrianle() = trim().split(Regex("""\s+""")).map { it.toInt() }.let {
    Triangle(it[0], it[1], it[2])
}

fun main(args: Array<String>) {
    val ts = File("src/day3/input.txt").readLines().map { it.toTrianle() }
    println("Part 1: ${ts.count { it.isPossible }}")

    val rows = File("src/day3/input.txt").readLines().map { it.trim().split(Regex("""\s+""")).map { it.toInt() } }
    val oneLine = rows.map { it[0] } + rows.map { it[1] } + rows.map { it[2] }
    val ts2 = (0 until oneLine.size step 3).map { i ->
        Triangle(oneLine[i], oneLine[i+1], oneLine[i+2])
    }
    println("Part 2: ${ts2.count { it.isPossible }}")
}