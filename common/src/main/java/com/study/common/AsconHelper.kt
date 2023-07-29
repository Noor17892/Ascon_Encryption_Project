package com.study.common

import com.study.common.utils.log

object AsconHelper {

    fun ascon128v12Encryption(
        key: ByteArray,
        nonce: ByteArray,
        plaintext: ByteArray,
        additionalData: ByteArray?
    ): ByteArray {
        // sanity checks
        if (key.size != 16) {
            log("the key length has to be 16, found " + key.size)
            return ByteArray(0)
        }
        if (nonce.size != 16) {
            log("the nonce length has to be 16, found " + nonce.size)
            return ByteArray(0)
        }
        val additionalDataLength: Int = additionalData?.size ?: 0
        val s = byteArrayOf()
        val ciphertext = ByteArray(plaintext.size + Ascon128v12.CRYPTO_ABYTES)
        Ascon128v12.crypto_aead_encrypt(
            ciphertext,
            ciphertext.size,
            plaintext,
            plaintext.size,
            additionalData,
            additionalDataLength,
            s,
            nonce,
            key
        )
        return ciphertext
    }

    fun ascon128v12Decryption(
        key: ByteArray,
        nonce: ByteArray,
        ciphertext: ByteArray,
        additionalData: ByteArray?
    ): ByteArray {
        // sanity checks
        if (key.size != 16) {
            log("the key length has to be 16, found " + key.size)
            return ByteArray(0)
        }
        if (nonce.size != 16) {
            log("the nonce length has to be 16, found " + nonce.size)
            return ByteArray(0)
        }
        val additionalDataLength: Int = additionalData?.size ?: 0
        val s = byteArrayOf()
        val plaintext = ByteArray(ciphertext.size + Ascon128v12.CRYPTO_ABYTES)
        val plen = Ascon128v12.crypto_aead_decrypt(
            plaintext,
            plaintext.size,
            s,
            ciphertext,
            ciphertext.size,
            additionalData,
            additionalDataLength,
            nonce,
            key
        )
        return if (plen != -1) {
            plaintext.copyOfRange(0, plen)
        } else {
            ByteArray(0)
        }
    }
}
