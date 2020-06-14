package com.gmail.tatsukimatsumo.imagelab.model.imageloder

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

class ContentsProviderImageLoader(private val context: Context) : ImageLoader {
    override suspend fun loadBitmap(uri: Uri): Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
}