package day5

import org.junit.Test
import produces

class Day5Test {

    @Test fun testMd5() {
        md5("abc3231929") produces "00000155f8105dff7f56ee10fa9b9abd"
    }

    @Test fun testLetters() {
        letters1("abc") produces "18f47a30"
    }

    @Test fun testLetters2() {
        letters2("abc") produces "05ace8e3"
    }
}