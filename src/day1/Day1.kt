package day1

import advent.Puzzle
import day1.Day1.Dir.*
import day1.Day1.Heading.*
import org.junit.Test
import java.io.File
import java.lang.Math.abs

/**
 * # Day 1: No Time for a Taxicab
 *
 * You're airdropped near Easter Bunny Headquarters in a city somewhere.
 * "Near", unfortunately, is as close as you can get -
 * the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here,
 * and nobody had time to work them out further.
 *
 * The Document indicates that you should start at the given coordinates
 * (where you just landed) and face North.
 * Then, follow the provided sequence:
 * either turn left (L) or right (R) 90 degrees, then walk forward the given number of blocks,
 * ending at a new intersection.
 *
 * There's no time to follow such ridiculous instructions on foot, though,
 * so you take a moment and work out the destination.
 * Given that you can only walk on the street grid of the city,
 * how far is the shortest path to the destination?
 *
 * ## Part Two
 * Then, you notice the instructions continue on the back of the Recruiting Document.
 * Easter Bunny HQ is actually at the first location you visit twice.
 *
 * For example, if your instructions are R8, R4, R4, R8,
 * the first location you visit twice is 4 blocks away, due East.
 *
 * How many blocks away is the first location you visit twice?
 */
object Day1 : Puzzle() {

    enum class Dir { L, R }
    data class Edge(val dir: Dir, val distance: Int)

    enum class Heading(val dx: Int, val dy: Int) {
        N(+1, 0), E(0, +1), S(-1, 0), W(0, -1)
    }

    fun turn(h: Heading, dir: Dir) = when (h) {
        N -> when (dir) { L -> W; R -> E
        }
        E -> when (dir) { L -> N; R -> S
        }
        S -> when (dir) { L -> E; R -> W
        }
        W -> when (dir) { L -> S; R -> N
        }
    }

    data class Pos(val heading: Heading,
                   val dx: Int = 0,
                   val dy: Int = 0) {
        operator fun plus(edge: Edge): Pos {
            val heading = turn(this.heading, edge.dir)
            return copy(heading,
                    dx + heading.dx * edge.distance,
                    dy + heading.dy * edge.distance)
        }

        val distance: Int get() = abs(dx) + abs(dy)
        fun place() = dx to dy
        operator fun times(dir: Dir) = copy(heading = turn(heading, dir))
        operator fun plus(dist: Int) = copy(dx = dx + heading.dx * dist, dy = dy + heading.dy * dist)
    }

    fun distance(vararg edges: Edge) = edges.fold(Pos(N)) { pos, edge -> pos + edge }.distance

    fun distanceToTwice(vararg edges: Edge): Int {
        var current = Pos(N)
        val visited = mutableSetOf(current.place())
        var first2: Pos? = null
        for ((dir, dist) in edges) {
            current *= dir
            for (i in 1..dist) {
                current += 1
                if (!visited.add(current.place())) {
                    first2 = first2 ?: current
                    //                return current.distance
                }
            }
        }
        /* print
        val left = visited.map { it.first }.min()!!
        val right = visited.map { it.first }.max()!!
        val top = visited.map { it.second }.min()!!
        val bottom = visited.map { it.second }.max()!!
        for (y in top..bottom) {
            for (x in left..right) {
                if (x == 0 && y == 0)
                    print('*')
                else if (first2 != null && first2.dx == x && first2.dy == y)
                    print('x')
                else if (x to y in visited)
                    print('#')
                else
                    print(' ')
            }
            println()
        }
        */
        return (first2 ?: current).distance
    }

    fun parse(input: String) = input.split(", ").map { part ->
        Edge(
                dir = Dir.valueOf(part.substring(0, 1)),
                distance = part.substring(1).toInt()
        )
    }

    infix operator fun Dir.invoke(distance: Int) = Edge(this, distance)

    fun testParse() {
        parse("R2, L3") produces listOf(R(2), L(3))
    }
    fun testDistance() {
        distance(R(2), L(3)) produces 5
        distance(R(2), R(2), R(2)) produces 2
        distance(R(5), L(5), R(5), R(3)) produces 12
    }
    fun testDistance2() {
        distanceToTwice(R(8), R(4), R(4), R(8)) produces 4
    }

    val text = File("src/day1/input.txt").readText()
    val edges = parse(text).toTypedArray()

    override fun part1() = distance(*edges)
    override fun part2() = distanceToTwice(*edges)
}
fun main(args: Array<String>) = Day1()