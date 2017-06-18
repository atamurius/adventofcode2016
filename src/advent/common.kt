package advent

import org.junit.Assert.assertEquals
import org.junit.ComparisonFailure
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.full.declaredFunctions

abstract class Puzzle {

    infix fun <T> T.produces(t: T) = assertEquals(t, this)

    open fun part1(): Any = TODO("Part 1")
    open fun part2(): Any = TODO("Part 2")

    private val RED = "31"
    private val GRN = "32"
    private val YEL = "33"
    private val BLU = "34"
    private val BLD = "1"
    private fun String?.color(c: String) = "\u001b[${c}m$this\u001b[0m"

    private var time = 0L

    fun profile() { time = System.currentTimeMillis() }
    fun time() = (System.currentTimeMillis() - time).let {
        String.format("%5.3fs", it / 1000.0)
    }

    operator fun invoke() {
        println("${this::class.simpleName.color("$BLU;$BLD")}\n")
        val success = this::class.declaredFunctions
                .filter { it.name.startsWith("test") }
                .all {
                    val title = it.name.replace(Regex("""[A-Z]"""), " \$0") +" "
                    val test = "*".color(BLU) +" ${title.padEnd(50, '.')}"
                    try {
                        profile()
                        it.call(this)
                        println(test + " SUCCESS".color(GRN) + " in ${time()}")
                        true
                    } catch (e: Throwable) {
                        println(test + " FAILED\n".color(RED))
                        if (e is InvocationTargetException && e.targetException is ComparisonFailure)
                            println(e.targetException.message.color(RED))
                        else
                            e.printStackTrace()
                        false
                    }
                }
        if (! success) {
            println("Tests failed".color(RED))
            System.exit(1)
        } else {
            println("")
            profile()
            println("Part 1: ${part1().toString().color("$YEL;$BLD")} in ${time()}".color(YEL))
            profile()
            println("Part 2: ${part2().toString().color("$YEL;$BLD")} in ${time()}".color(YEL))
        }
    }
}