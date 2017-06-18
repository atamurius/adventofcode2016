package day4

import advent.Puzzle
import org.junit.Test
import java.io.File
import java.util.Comparator.comparingInt

/**
 * # Day 4: Security Through Obscurity
 *
 * Each room consists of an encrypted name (lowercase letters separated by dashes)
 * followed by a dash, a sector ID, and a checksum in square brackets.
 *
 * A room is real (not a decoy) if the checksum is the five most common letters
 * in the encrypted name, in order, with ties broken by alphabetization.
 *
 * What is the sum of the sector IDs of the real rooms?
 */
object Day4 : Puzzle() {

    data class Room(val name: String,
                    val sum: String,
                    val id: Int)

    fun parseName(name: String): Room =
            Regex("""([a-z-]+)-(\d+)\[([a-z]{5})]""").matchEntire(name)?.let {
                Room(it.groupValues[1], it.groupValues[3], it.groupValues[2].toInt())
            }!!

    fun checksum(name: String) = name
            .replace("-", "")
            .toCharArray()
            .fold(mutableMapOf<Char, Int>()) { map, c ->
                map.merge(c, 1, Int::plus)
                map
            }
            .entries
            .sortedWith(
                    comparingInt<Map.Entry<Char, Int>> { -it.value }
                            .thenComparing<Char> { it.key }
            )
            .take(5)
            .joinToString("") { "${it.key}" }

    fun testChecksum() {
        checksum("aaaaa-bbb-z-y-x") produces "abxyz"
        checksum("a-b-c-d-e-f-g-h") produces "abcde"
        checksum("not-a-real-room") produces "oarel"
    }

    fun decrypt(room: Room) = room.name.toCharArray().map {
        when (it) {
            '-' -> ' '
            else -> {
                val l = it - 'a'
                val rot = (l + room.id) % ('z' - 'a' + 1)
                (rot + 'a'.toInt()).toChar()
            }
        }
    }.joinToString("")

    fun testDescrypt() {
        decrypt(parseName("abcxyz-2[zzzzz]")) produces "cdezab"
        decrypt(parseName("qzmt-zixmtkozy-ivhz-343[zzzzz]")) produces "very encrypted name"
    }

    val valids = File("src/day4/input.txt").readLines().map(Day4::parseName).filter { it.sum == checksum(it.name) }

    override fun part1() = valids.sumBy { it.id }
    override fun part2() = valids.find { decrypt(it) == "northpole object storage" }!!.id
}
fun main(args: Array<String>) = Day4()