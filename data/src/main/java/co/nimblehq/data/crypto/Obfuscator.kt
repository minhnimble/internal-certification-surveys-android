package co.nimblehq.data.crypto

import java.nio.charset.StandardCharsets
import kotlin.experimental.xor

object Obfuscator {

    fun reveal(): String {
        val salt = "survey_android_salt_aczzdkjerjowieru10231782349"
        val keys = byteArrayOf(32, 0, 0, 0, 32, 110, 83, 93, 69, 83, 11, 10, 22, 38, 3, 21)
        val cipher = salt.toByteArray(StandardCharsets.UTF_8)
        return String(keys.withIndex().map { it.value xor cipher[it.index % cipher.size] }.toByteArray(), StandardCharsets.UTF_8)
    }
}

class SecretKeyException : Exception("Secret Key has been corrupted.")
