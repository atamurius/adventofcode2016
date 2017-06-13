package day4

import org.junit.Test
import produces

class Day4Test {

    @Test fun testChecksum() {
        checksum("aaaaa-bbb-z-y-x") produces "abxyz"
        checksum("a-b-c-d-e-f-g-h") produces "abcde"
        checksum("not-a-real-room") produces "oarel"
    }
    @Test fun testDescrypt() {
        println()
        decrypt(parseName("abcxyz-2[zzzzz]")) produces "cdezab"
        decrypt(parseName("qzmt-zixmtkozy-ivhz-343[zzzzz]")) produces "very encrypted name"
    }
}