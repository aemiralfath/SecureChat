package com.aemiralfath.securechat.security

import android.annotation.SuppressLint
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64
import java.security.Security
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

class AES {
    private val keyBytes = "sTADoneSemester8".toByteArray(charset("UTF8"))

    @SuppressLint("GetInstance")
    fun encrypt(strToEncrypt: String): String {

        Security.addProvider(BouncyCastleProvider())

        try {
            val secretKey = SecretKeySpec(keyBytes, "AES")
            val input = strToEncrypt.trim().toByteArray(charset("UTF8"))

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)

                val cipherText = ByteArray(cipher.getOutputSize(input.size))
                var ctLength = cipher.update(input, 0, input.size, cipherText, 0)

                ctLength += cipher.doFinal(cipherText, ctLength)
                return String(Base64.encode(cipherText))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "not encrypted"
    }

    @SuppressLint("GetInstance")
    fun decrypt(cipherToDecrypt: String): String {

        Security.addProvider(BouncyCastleProvider())

        try {
            val secretKey = SecretKeySpec(keyBytes, "AES")
            val input = Base64.decode(cipherToDecrypt.trim().toByteArray(charset("UTF8")))

            synchronized(Cipher::class.java) {
                val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
                cipher.init(Cipher.DECRYPT_MODE, secretKey)

                val plainText = ByteArray(cipher.getOutputSize(input.size))
                var ptLength = cipher.update(input, 0, input.size, plainText, 0)

                ptLength += cipher.doFinal(plainText, ptLength)
                val decryptedString = String(plainText)
                return decryptedString.trim()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "not decrypted"
    }
}