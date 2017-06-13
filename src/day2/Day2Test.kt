package day2

import org.junit.Test
import day2.Dir.*
import produces

class Day2Test {

    @Test fun testKeypad() {
        val keypad = Keypad(KEYS, 1, 1)
        keypad.move(U, L, L) produces '1'
        keypad.move(R, R, D, D, D) produces '9'
        keypad.move(L, U, R, D, L) produces '8'
        keypad.move(U, U, U, U, D) produces '5'
    }
    @Test fun testKeypad2() {
        val keypad = Keypad(KEYS2, 0, 2)
        keypad.move(U, L, L) produces '5'
        keypad.move(R, R, D, D, D) produces 'D'
        keypad.move(L, U, R, D, L) produces 'B'
        keypad.move(U, U, U, U, D) produces '3'
    }
}