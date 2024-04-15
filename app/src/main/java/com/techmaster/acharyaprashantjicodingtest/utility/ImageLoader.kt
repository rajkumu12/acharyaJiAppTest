import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.LruCache
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageLoader(applicationContext: Context,private val imageUrl: String, private val callback: (Bitmap?) -> Unit) : AsyncTask<Void, Void, Bitmap?>() {

    private val cacheDir: File = File(applicationContext.cacheDir, "images")

    override fun doInBackground(vararg params: Void?): Bitmap? {
        return try {
            // Check if the image is already cached
            val cachedFile = File(cacheDir, imageUrl.hashCode().toString())
            if (cachedFile.exists()) {
                decodeFile(cachedFile)
            } else {
                // Load the image from the network
                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val input = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(input)

                // Save the image to cache
                saveToCache(bitmap, cachedFile)

                bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        callback(result)
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

    private fun saveToCache(bitmap: Bitmap, file: File) {
        try {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

