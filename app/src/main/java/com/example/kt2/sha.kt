package com.example.kt2

import android.util.Log
import kotlin.Throws
import java.io.UnsupportedEncodingException
import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

 object AeSimpleSHA1 {
    private fun convertToHex(data: ByteArray): String {
        val buf = StringBuilder()
        for (b in data) {
            var halfByte: Int = b.toInt() ushr  4 and 0x0F
            var twoHalf = 0
            do {
                buf.append(if (halfByte <= 9) ('0'.code + halfByte).toChar() else ('a'.code + halfByte - 10).toChar())
                halfByte = (b and 0x0F).toInt()
            } while (twoHalf++ < 1)
        }
        return buf.toString()
    }

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    fun sHA1(text: String): String {
        val md = MessageDigest.getInstance("SHA-1")
        println("SHA1: $md.")
        val textBytes = text.toByteArray(StandardCharsets.ISO_8859_1)
        md.update(textBytes, 0, textBytes.size)
        val sha1hash = md.digest()
        return convertToHex(sha1hash)
    }
}