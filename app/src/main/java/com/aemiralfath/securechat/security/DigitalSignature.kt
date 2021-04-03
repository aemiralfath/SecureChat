package com.aemiralfath.securechat.security

import org.bouncycastle.util.encoders.Base64
import java.security.*
import java.security.spec.X509EncodedKeySpec

class DigitalSignature {

    fun getKeyPair(): KeyPair {
        val keyGen = KeyPairGenerator.getInstance("RSA")
        keyGen.initialize(521)
        return keyGen.generateKeyPair()
    }

    fun getPublic(key: String): PublicKey {
        val pubKey = key.replace("\\n", "")
        val spec = X509EncodedKeySpec(Base64.decode(pubKey))
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(spec)
    }


    fun sign(data: String, privateKey: PrivateKey): String {
        val contentBytes = data.toByteArray(charset("UTF8"))
        val signature = Signature.getInstance("MD5withRSA")
        signature.initSign(privateKey)
        signature.update(contentBytes)

        val signs = signature.sign()
        return String(Base64.encode(signs))
    }

    fun verifySign(data: String, sign: String, publicKey: PublicKey): Boolean {
        val contentBytes = data.toByteArray(charset("UTF8"))
        val signature = Signature.getInstance("MD5withRSA")
        signature.initVerify(publicKey)
        signature.update(contentBytes)
        return signature.verify(Base64.decode(sign))
    }
}