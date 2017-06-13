package day1

import day1.Dir.L
import day1.Dir.R
import org.junit.Test
import produces

class Day1Test {
    infix operator fun Dir.invoke(distance: Int) = Edge(this, distance)

    @Test fun parseTest() {
        parse("R2, L3") produces listOf(R(2), L(3))
    }
    @Test fun distanceTest() {
        distance(R(2), L(3)) produces 5
        distance(R(2), R(2), R(2)) produces 2
        distance(R(5), L(5), R(5), R(3)) produces 12
    }
    @Test fun distance2test() {
        distanceToTwice(R(8), R(4), R(4), R(8)) produces 4
    }
}