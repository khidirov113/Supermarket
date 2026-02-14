package com.example.supermarket.presentation.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

fun generateQrCode(text: String): androidx.compose.ui.graphics.ImageBitmap? {
    return try {
        val size = 512
        val matrix = MultiFormatWriter().encode(
            text, BarcodeFormat.QR_CODE, size, size
        )
        val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap[x, y] = if (matrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}