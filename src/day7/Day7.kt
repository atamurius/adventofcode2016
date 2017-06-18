package day7

import advent.Puzzle
import java.io.File

/**
 * # Day 7: Internet Protocol Version 7
 *
 * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA.
 * An ABBA is any four-character sequence which consists of a pair of two different
 * characters followed by the reverse of that pair, such as xyyx or abba. However,
 * the IP also must not have an ABBA within any hypernet sequences,
 * which are contained by square brackets.
 *
 * ## Part 2
 *
 * n IP supports SSL if it has an Area-Broadcast Accessor, or ABA,
 * anywhere in the supernet sequences (outside any square bracketed sections),
 * and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet sequences.
 * An ABA is any three-character sequence which consists of the same character twice
 * with a different character between them, such as xyx or aba.
 * A corresponding BAB is the same characters but in reversed positions: yxy and bab, respectively.
 */
object Day7 : Puzzle() {

    val ABBA = Regex("""([a-z])([a-z])\2\1""")
    val ABA = Regex("""([a-z])[a-z]\1""")

    fun Regex.each(str: String) = generateSequence({ find(str) }, { find(str, it.range.start + 1) })

    fun testPattern() {
        ABBA.find("someabbat")?.value produces "abba"
    }

    fun validAbba(abba: String) = abba[0] != abba[1]
    fun validAba(aba: String) = aba[0] != aba[1]

    fun inHypernet(str: String, index: Int): Boolean {
        val open = str.lastIndexOf('[', index)
        val close = str.lastIndexOf(']', index)
        return open != -1 && open > close
    }

    fun testInHypernet() {
        inHypernet("abba[mnop]qrst",  0) produces false
        inHypernet("abba[mnop]qrst",  5) produces true
        inHypernet("abba[mnop]qrst", 10) produces false
    }

    fun supportsTLS(addr: String) = ABBA.each(addr).let {
        it.any { validAbba(it.value) } && it.none { validAbba(it.value) && inHypernet(addr, it.range.first) }
    }

    fun supportsSSL(addr: String) = ABA.each(addr)
            .filterNot { inHypernet(addr, it.range.start) }
            .filter { validAba(it.value) }
            .any {
                val (a, b) = it.value.toCharArray()
                Regex("$b$a$b").each(addr).any { inHypernet(addr, it.range.start) }
            }

    fun testAbba1() { supportsTLS("abba[mnop]qrst") produces true }
    fun testAbba2() { supportsTLS("abcd[bddb]xyyx") produces false }
    fun testAbba3() { supportsTLS("aaaa[qwer]tyui") produces false }
    fun testAbba4() { supportsTLS("ioxxoj[asdfgh]zxcvbn") produces true }
    fun testAba1() { supportsSSL("aba[bab]xyz") produces true }
    fun testAba2() { supportsSSL("xyx[xyx]xyx") produces false }
    fun testAba3() { supportsSSL("aaa[kek]eke") produces true }
    fun testAba4() { supportsSSL("zazbz[bzb]cdb") produces true }

    val input = File("src/day7/input.txt").readLines()

    override fun part1() = input.count { supportsTLS(it) }
    override fun part2() = input.count { supportsSSL(it) }
}

fun main(vararg args: String) = Day7()