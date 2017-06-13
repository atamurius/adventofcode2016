package day4

import java.io.File
import java.util.Comparator.comparingInt

/* # Day 4: Security Through Obscurity
 *
 * Each room consists of an encrypted name (lowercase letters separated by dashes)
 * followed by a dash, a sector ID, and a checksum in square brackets.
 *
 * A room is real (not a decoy) if the checksum is the five most common letters
 * in the encrypted name, in order, with ties broken by alphabetization.
 *
 * What is the sum of the sector IDs of the real rooms?
 */

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

fun main(args: Array<String>) {
    val rooms = File("src/day4/input.txt").readLines().map(::parseName)
    val valids = rooms.filter { it.sum == checksum(it.name) }
    println("Part 1: ${valids.sumBy { it.id }}")
    println("Part 2: ${valids.find { decrypt(it) == "northpole object storage" }?.id}")
}