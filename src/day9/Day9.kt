package day9

import advent.Puzzle
import java.io.File

/**
 * # Day 9: Explosives in Cyberspace
 *
 * The format compresses a sequence of characters. Whitespace is ignored.
 * To indicate that some sequence should be repeated, a marker is added to the file, like (10x2).
 * To decompress this marker, take the subsequent 10 characters and repeat them 2 times.
 * Then, continue reading the file after the repeated data.
 * The marker itself is not included in the decompressed output.
 *
 * If parentheses or other characters appear within the data referenced by a marker,
 * that's okay - treat it like normal data, not a marker, and then resume looking for
 * markers after the decompressed section.
 */
object Day9 : Puzzle() {

    val REP = Regex("""\((\d+)x(\d+)\)""")

    tailrec fun <T> decompress(text: String,
                               from: Int,
                               init: T,
                               simple: (T, from: Int, len: Int) -> T,
                               complex: (T, from:Int, len:Int, times:Int) -> T): T {
        val rep = REP.find(text, from)
        return if (rep == null) (
                if (from == text.length) init
                else simple(init, from, text.length - from)
        )
        else {
            val (len, times) = rep.destructured
            val seqStart = rep.range.endInclusive + 1
            val seqEnd = seqStart + len.toInt()
            val first = if (from == rep.range.start) init
                        else simple(init, from, rep.range.start - from)
            val next = complex(first, seqStart, len.toInt(), times.toInt())
            decompress(text, seqEnd, next, simple, complex)
        }
    }

    fun decompressToStr(text: String) = decompress(text, 0, "",
            simple = { prev, from, len -> prev + text.substring(from, from + len) },
            complex = { prev, from, len, times ->
                (1..times).fold(prev) { p, _ -> p + text.substring(from, from + len) }
            })

    fun testDec1() = decompressToStr("ADVENT") produces "ADVENT"
    fun testDec2() = decompressToStr("A(1x5)BC") produces "ABBBBBC"
    fun testDec3() = decompressToStr("(3x3)XYZ") produces "XYZXYZXYZ"
    fun testDec4() = decompressToStr("A(2x2)BCD(2x2)EFG") produces "ABCBCDEFEFG"
    fun testDec5() = decompressToStr("(6x1)(1x3)A") produces "(1x3)A"
    fun testDec6() = decompressToStr("X(8x2)(3x3)ABCY") produces "X(3x3)ABC(3x3)ABCY"

    fun decompressToLen(text: String) = decompress(text, 0, 0,
            simple = { prev, _, len -> prev + len },
            complex = { prev, _, len, times -> prev + len * times })

    fun testLen1() = decompressToLen("ADVENT") produces 6
    fun testLen2() = decompressToLen("A(1x5)BC") produces 7
    fun testLen3() = decompressToLen("(3x3)XYZ") produces 9
    fun testLen4() = decompressToLen("A(2x2)BCD(2x2)EFG") produces 11
    fun testLen5() = decompressToLen("(6x1)(1x3)A") produces 6
    fun testLen6() = decompressToLen("X(8x2)(3x3)ABCY") produces 18

    val input = File("src/day9/input.txt").readText()

    override fun part1() = decompressToLen(input)

    fun decompress2ToStr(text: String): String = decompress(text, 0, "",
            simple = { prev, from, len -> prev + text.substring(from, from + len) },
            complex = { prev, from, len, times ->
                (1..times).fold(prev) { p, _ -> p + decompress2ToStr(text.substring(from, from + len)) }
            })

    fun testDec2_1() = decompress2ToStr("(3x3)XYZ") produces "XYZXYZXYZ"
    fun testDec2_2() = decompress2ToStr("X(8x2)(3x3)ABCY") produces "XABCABCABCABCABCABCY"


    fun decompress2ToLen(text: String): Long = decompress(text, 0, 0L,
            simple = { prev, _, len -> prev + len },
            complex = { prev, from, len, times ->
                prev + times * decompress2ToLen(text.substring(from, from + len))
            })

    fun testLen2_1() = decompress2ToLen("(27x12)(20x12)(13x14)(7x10)(1x12)A") produces 241920
    fun testLen2_2() = decompress2ToLen("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN") produces 445

    override fun part2() = decompress2ToLen(input)

    @JvmStatic fun main(vararg args: String) = Day9()
}