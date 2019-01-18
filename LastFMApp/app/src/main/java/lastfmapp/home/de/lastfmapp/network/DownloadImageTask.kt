package lastfmapp.home.de.lastfmapp.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.io.IOException
import java.net.URL

/**
 * Lädt ein Bild als Bitmap von einer übergebenen Quelle über deren URL herunter und verarbeitet
 * das Bild nach Ende des Downloads weiter.
 */
class DownloadImageTask(private val imageUrl: String,  private val setImage: (Bitmap) -> Unit): AsyncTask<Unit, Unit, Unit>() {
    private var bitmap: Bitmap? = null

    override fun doInBackground(vararg params: Unit?) {
        try {
            val inputStream = URL(imageUrl).openStream()

            bitmap = BitmapFactory.decodeStream(inputStream)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onPostExecute(result: Unit?) {
        if(bitmap != null) {
            setImage(bitmap!!)
        }
    }
}