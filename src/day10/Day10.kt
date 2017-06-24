package day10

import advent.Puzzle
import java.io.File
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * # Day 10: Balance Factory
 *
 * Upon closer examination, you notice that each bot only proceeds when it has two microchips,
 * and once it does, it gives each one to a different bot or puts it in a marked "output" bin.
 * Sometimes, bots take microchips from "input" bins, too.
 *
 * Inspecting one of the microchips, it seems like they each contain a single number;
 * the bots must use some logic to decide what to do with each chip.
 * You access the local control computer and download the bots' instructions (your puzzle input).
 *
 * Some of the instructions specify that a specific-valued microchip should be given to a specific bot;
 * the rest of the instructions indicate what a given bot should do with its lower-value or higher-value chip.
 */
object Day10 : Puzzle() {
    @JvmStatic fun main(vararg args: String) = Day10()

    sealed class Dest {
        data class Bot(val id: Int) : Dest()
        data class Out(val id: Int) : Dest()
    }

    sealed class Instr {
        abstract val bot: Int

        data class Set(val value: Int, override val bot: Int) : Instr()
        data class Pass(override val bot: Int, val low: Dest, val high: Dest) : Instr()

        companion object {
            fun from(s: String) =
                    Regex("""value (\d+) goes to bot (\d+)""").matchEntire(s)?.destructured
                            ?.let { (v, b) -> Set(v.toInt(), b.toInt())  } ?:
                    Regex("""bot (\d+) gives low to (output|bot) (\d+) and high to (output|bot) (\d+)""")
                            .matchEntire(s)?.destructured?.let { (b, lt, ln, ht, hn) ->
                                Pass(b.toInt(),
                                        low = if (lt == "bot") Dest.Bot(ln.toInt()) else Dest.Out(ln.toInt()),
                                        high = if (ht == "bot") Dest.Bot(hn.toInt()) else Dest.Out(hn.toInt()))
                            } ?:
                    error("Unknown instruction $s")
        }
    }

    class Factory {
        val bots = mutableMapOf<Int, Bot>()
        val todo = mutableMapOf<Int, Instr.Pass>()
        val outs = mutableMapOf<Int, Int>()
        val comparing = mutableMapOf<Pair<Int, Int>, Int>()

        operator fun get(id: Int) = bots[id] ?: Bot().apply {
            bots.put(id, this)
        }
        operator fun set(dest: Dest, value: Int) {
            when (dest) {
                is Dest.Out -> outs[dest.id] = value
                is Dest.Bot -> invoke(Instr.Set(value, dest.id))
            }
        }
        operator fun invoke(instr: Instr) {
            val bot = get(instr.bot)
            when (instr) {
                is Instr.Set -> bot.add(instr.value)
                is Instr.Pass -> todo.merge(instr.bot, instr) { _, _ ->
                    error("bot ${instr.bot} got things to do")
                }
            }
            if (bot.isFull && instr.bot in todo) {
                val (low, high) = bot.give()
                comparing.put(low to high, instr.bot)
                todo[instr.bot]?.let {
                    set(it.low, low)
                    set(it.high, high)
                }
            }
        }
    }

    data class Bot(var low: Int? = null,
                   var high: Int? = null) {

        val isFull get() = low != null && high != null
        fun give() = (low!! to high!!).apply {
            low = null
            high = null
        }
        fun add(value: Int) {
            if (low == null) low = value
            else if (isFull) error("Can't carry any more")
            else {
                val a = low!!
                low = minOf(a, value)
                high = maxOf(a, value)
            }
        }
    }

    val example =
            """|value 5 goes to bot 2
               |bot 2 gives low to bot 1 and high to bot 0
               |value 3 goes to bot 1
               |bot 1 gives low to output 1 and high to bot 0
               |bot 0 gives low to output 2 and high to output 0
               |value 2 goes to bot 2"""
                    .trimMargin()
                    .lines()
                    .map { Instr.from(it) }

    fun testFactory() {
        val f = Factory()
        example.forEach { f(it) }
        f.outs produces mapOf(0 to 5, 1 to 2, 2 to 3)
        f.comparing[2 to 5] produces 2
    }

    val input = File("src/day10/input.txt").readLines().map { Instr.from(it) }

    override fun part1() = Factory().apply { input.forEach { invoke(it) } }.comparing[17 to 61]!!
    override fun part2() = Factory().apply { input.forEach { invoke(it) } }.run {
        outs[0]!! * outs[1]!! * outs[2]!!
    }
}