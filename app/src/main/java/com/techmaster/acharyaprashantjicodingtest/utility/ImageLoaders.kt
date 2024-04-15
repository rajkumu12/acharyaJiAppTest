package com.techmaster.acharyaprashantjicodingtest.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageLoaders(
    private val applicationContext: Context,
    private val imageUrl: String,
    private val callback: (Bitmap?) -> Unit
) {
    private val cacheDir: File = File(applicationContext.cacheDir, "images")

    suspend fun loadImage() {
        val cachedBitmap = getCachedBitmap()

        if (cachedBitmap != null) {
            callback(cachedBitmap)
        } else {
            try {
                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)

                saveToCache(bitmap)

                callback(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
                callback(null)
            }
        }
    }

    private suspend fun getCachedBitmap(): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val cachedFile = File(cacheDir, imageUrl.hashCode().toString())
                if (cachedFile.exists()) {
                    decodeFile(cachedFile)
                } else {
                    null
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun decodeFile(file: File): Bitmap? {
        return try {
            val fis = FileInputStream(file)
            val bitmap = BitmapFactory.decodeStream(fis)
            fis.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun saveToCache(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            try {
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs()
                }
                val cachedFile = File(cacheDir, imageUrl.hashCode().toString())
                val fos = FileOutputStream(cachedFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}