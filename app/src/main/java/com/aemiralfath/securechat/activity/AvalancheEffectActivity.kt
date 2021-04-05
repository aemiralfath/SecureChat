package com.aemiralfath.securechat.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.aemiralfath.securechat.databinding.ActivityAvalancheEffectBinding
import com.aemiralfath.securechat.observer.MyButtonObserver
import com.aemiralfath.securechat.security.AES

class AvalancheEffectActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AvalancheEffectActivity"
        private const val TITLE = "Avalanche Effect"
    }

    private lateinit var aes: AES
    private lateinit var binding: ActivityAvalancheEffectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvalancheEffectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        aes = AES()

        binding.edtTextB.addTextChangedListener(
            MyButtonObserver(
                binding.btnAvalancheEffect,
                binding.edtTextA
            )
        )

        binding.btnAvalancheEffect.setOnClickListener {
            val textA = binding.edtTextA.text.toString()
            val textB = binding.edtTextB.text.toString()

            val cipherTextA = aes.encrypt(textA)
            val cipherTextB = aes.encrypt(textB)

            val byteA = cipherTextA.toByteArray(charset("UTF8"))
            val byteB = cipherTextB.toByteArray(charset("UTF8"))

            var diff = 0
            for (i in byteA.indices) {
                val bitA = Integer.toBinaryString(byteA[i].toInt() and 255 or 256).substring(1)
                val bitB = Integer.toBinaryString(byteB[i].toInt() and 255 or 256).substring(1)



                if (bitA != bitB) {
                    diff += 1
                }
            }

            Log.d(TAG, diff.toString())
            Log.d(TAG, byteA.size.toString())
            val total: Float = (diff.toFloat() / byteA.size.toFloat())*100
            val avalancheEffect = String.format("%.2f%%", total)

            binding.tvTextA.text = textA
            binding.tvTextB.text = textB
            binding.tvCiphertextA.text = cipherTextA
            binding.tvCiphertextB.text = cipherTextB
            binding.tvTextLengthA.text = textA.length.toString()
            binding.tvTextLengthB.text = textB.length.toString()
            binding.tvAvalancheEffect.text = avalancheEffect

            binding.edtTextA.setText("")
            binding.edtTextB.setText("")
            binding.btnAvalancheEffect.isEnabled = false
        }
    }
}