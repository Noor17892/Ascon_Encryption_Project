package com.study.wearable.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CompactChip
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.study.common.AsconHelper
import com.study.common.R
import com.study.common.utils.log
import com.study.wearable.presentation.theme.AsconEncryptionAndroidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    private val job: Job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val key = "1234567890123456".toByteArray(StandardCharsets.UTF_8)
    private val nonce = "6543210987654321".toByteArray(StandardCharsets.UTF_8)
    private val additionalData = "Ascon Encryption".toByteArray(StandardCharsets.UTF_8)

    private var cipherData: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp(
                onEncryptClick = ::performEncryption,
                onDecryptClick = ::performDecryption
            )
        }
    }

    private fun performEncryption() {
        ioScope.launch {
            log("opening the file...")
            resources.openRawResource(R.raw.projecttest).use {
                log("reading the file...")
                val bytes = it.readBytes()

//                log("original file: size=${bytes.size} b data=$bytes")
                log("original file: size=${bytes.size} data=${bytes.toString(StandardCharsets.UTF_8)}")

                log("encryption started...")
                val startTime = System.currentTimeMillis()
                log("key: size=${key.size} data=${key.toString(StandardCharsets.UTF_8)}")
                log("nonce: size=${nonce.size} data=${nonce.toString(StandardCharsets.UTF_8)}")
                log(
                    "additionalData: size=${additionalData.size} data=${
                        additionalData.toString(
                            StandardCharsets.UTF_8
                        )
                    }"
                )

                cipherData = AsconHelper.ascon128v12Encryption(
                    key = key,
                    nonce = nonce,
                    plaintext = bytes,
                    additionalData = additionalData
                )
                log("cipherData: size=${cipherData?.size} b data=${cipherData?.toString(StandardCharsets.UTF_8)}")
                log("encryption finished...")
                log("encryption time: ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }

    private fun performDecryption() {
        cipherData?.let { bytes ->
            ioScope.launch {
                val startTime = System.currentTimeMillis()
                log("decryption started...")
                log("key: size=${key.size} data=${key.toString(StandardCharsets.UTF_8)}")
                log("nonce: size=${nonce.size} data=${nonce.toString(StandardCharsets.UTF_8)}")
                log(
                    "additionalData: size=${additionalData.size} data=${
                        additionalData.toString(
                            StandardCharsets.UTF_8
                        )
                    }"
                )

                val decryptedBytes = AsconHelper.ascon128v12Decryption(
                    key = key,
                    nonce = nonce,
                    ciphertext = bytes,
                    additionalData = additionalData
                )
//                log("decryptedBytes: size=${decryptedBytes.size} b data=$decryptedBytes")
                log(
                    "decryptedBytes: size=${decryptedBytes.size} data=${
                        decryptedBytes.toString(
                            StandardCharsets.UTF_8
                        )
                    }"
                )
                log("decryption finished...")
                log("decryption time: ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}

@Composable
fun WearApp(
    onEncryptClick: () -> Unit = {},
    onDecryptClick: () -> Unit = {}
) {
    AsconEncryptionAndroidTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CompactChip(
                onClick = onEncryptClick,
                label = {
                    Text(text = "Encrypt")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            CompactChip(
                onClick = onDecryptClick,
                label = {
                    Text(text = "Decrypt")
                }
            )
        }
    }
}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}