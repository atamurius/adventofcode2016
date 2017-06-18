package day5

import java.security.MessageDigest
import java.util.stream.Stream

/* # Day 5: How About a Nice Game of Chess?
 *
 * The eight-character password for the door is generated one character at a time by
 * finding the MD5 hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).
 *
 * A hash indicates the next character in the password if its hexadecimal representation
 * starts with five zeroes.
 * If it does, the sixth character in the hash is the next character of the password.
 *
 * ## Part 2
 *
 * Instead of simply filling in the password from left to right,
 * the hash now also indicates the position within the password to fill.
 * You still look for hashes that begin with five zeroes; however, now,
 * the sixth character represents the position (0-7),
 * and the seventh character is the character to put in that position.
 */

fun md5(text: String) = MessageDigest
        .getInstance("MD5")
        .digest(text.toByteArray())
        .joinToString("") { (it.toInt() and 0xff).toString(16).padStart(2, '0') }

fun letters(text: String) = Sequence({ (0..Int.MAX_VALUE).iterator() })
        .map { md5("$text$it") }
        .filter { it.startsWith("00000") }
        .map {
            println("Hash found: $it")
            it
        }

fun letters1(text: String) = letters(text).take(8).joinToString("") { "${it[5]}" }

fun letters2(text: String): String = letters(text)
        .filter { it[5] in '0'..'7' }
        .map { (it[5] - '0') to it[6] }
        .fold(Array<Char?>(8) { null }) { pass, (i, c) ->
            pass[i] = pass[i] ?: c
            if (null !in pass)
                return pass.joinToString("")
            pass
        }.let { "Not possible" }

fun main(args: Array<String>) {
    println("Part 1: ${letters1("abbhdwsy")}")
    println("Part 2: ${letters2("abbhdwsy")}")
}