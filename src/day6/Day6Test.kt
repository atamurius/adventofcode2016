package day6

import org.junit.Test
import produces

class Day6Test {

    @Test fun testTranspose() {
        listOf("abc", "def", "ghi").transposed() produces listOf(
                listOf('a', 'd', 'g'),
                listOf('b', 'e', 'h'),
                listOf('c', 'f', 'i'))

    }

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

    @Test fun testMostFreqLetters() {
        mostFreqLetters(lines) produces "easter"
    }
    @Test fun testLeastFreqLetters() {
        leastFreqLetters(lines) produces "advent"
    }
}