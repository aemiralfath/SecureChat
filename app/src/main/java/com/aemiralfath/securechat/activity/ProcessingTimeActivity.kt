package com.aemiralfath.securechat.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aemiralfath.securechat.databinding.ActivityProcessingTimeBinding
import com.aemiralfath.securechat.observer.MyButtonObserver
import com.aemiralfath.securechat.security.AES
import com.aemiralfath.securechat.security.DigitalSignature
import java.security.KeyPair

class ProcessingTimeActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "ProcessingTimeActivity"
        private const val TITLE = "Processing Time"
    }

    private lateinit var aes: AES
    private lateinit var keyPair: KeyPair
    private lateinit var digitalSignature: DigitalSignature
    private lateinit var binding: ActivityProcessingTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcessingTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        aes = AES()
        digitalSignature = DigitalSignature()
        keyPair = digitalSignature.getKeyPair()

        binding.edtText.addTextChangedListener(MyButtonObserver(binding.btnCountTime, null))

        binding.btnCountTime.setOnClickListener {
            Log.d(TAG, "onClick")

            var text = binding.edtText.text.toString()

            val startEncrypt = System.nanoTime()
            val cipherText = aes.encrypt(text)
            val encryptEnd = System.nanoTime() - startEncrypt
            val encryptTime = String.format("$encryptEnd ns (%.2f ms)", encryptEnd / 1000000.0)

            val startSign = System.nanoTime()
            val signText = digitalSignature.sign(cipherText, keyPair.private)
            val signEnd = System.nanoTime() - startSign
            val signTime = String.format("$signEnd ns (%.2f ms)", signEnd / 1000000.0)

            val sendTime = String.format(
                "${encryptEnd + signEnd} ns (%.2f ms)",
                (encryptEnd + signEnd) / 1000000.0
            )

            val startVerify = System.nanoTime()
            val verify = digitalSignature.verifySign(cipherText, signText, keyPair.public)
            val verifyEnd = System.nanoTime() - startVerify
            val verifyTime = String.format("$verifyEnd ns (%.2f ms)", verifyEnd / 1000000.0)

            val startDecrypt = System.nanoTime()
            text = if (verify) {
                aes.decrypt(cipherText)
            } else {
                "Pesan Tidak Dapat Dipercaya!!!"
            }
            val decryptEnd = System.nanoTime() - startDecrypt
            val decryptTime = String.format("$decryptEnd ns (%.2f ms)", decryptEnd / 1000000.0)

            val receiveTime = String.format(
                "${decryptEnd + verifyEnd} ns (%.2f ms)",
                (decryptEnd + verifyEnd) / 1000000.0
            )

            val textLength = "${text.length} characters"

            binding.tvText.text = text
            binding.tvChipertext.text = cipherText
            binding.tvSign.text = signText
            binding.tvTextLength.text = textLength
            binding.tvEncryptTime.text = encryptTime
            binding.tvSignTime.text = signTime
            binding.tvSendTime.text = sendTime
            binding.tvDecryptTime.text = decryptTime
            binding.tvVerifyTime.text = verifyTime
            binding.tvReceiveTime.text = receiveTime

            binding.edtText.setText("")
        }

    }
}