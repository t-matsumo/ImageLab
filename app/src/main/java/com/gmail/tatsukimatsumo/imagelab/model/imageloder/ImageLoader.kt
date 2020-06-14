package com.gmail.tatsukimatsumo.imagelab.model.imageloder

import android.graphics.Bitmap
import android.net.Uri

interface ImageLoader {
    suspend fun loadBitmap(uri: Uri): Bitmap
}