package lastfmapp.home.de.lastfmapp.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import lastfmapp.home.de.lastfmapp.network.OnFailureListener
import lastfmapp.home.de.lastfmapp.data.Album
import lastfmapp.home.de.lastfmapp.repositories.AlbumRepository

/**
 * ViewModel, welches die von LastFM erhaltenen Top-Alben und bereits heruntergeladene Cover dieser Alben trägt.
 */
class AlbumViewModel: ViewModel() {
    private val albumRepository by lazy {
        AlbumRepository.getInstance()
    }

    val albums: LiveData<List<Album>>
    val albumImages: HashMap<Album, Bitmap>

    init {
        albums = albumRepository.albums
        albumImages = HashMap()
    }

    /**
     * Leitet die Top-Album-Suche an das AlbumRepository weiter und lässt bei Erfolg der Anfrage
     * die bisher zwischengespeicherten Cover löschen.
     */
    fun search(searchString: String, onFailureListener: OnFailureListener, vararg onSuccessListeners: () -> Unit) {
        val tmp = ArrayList<() -> Unit>()

        onSuccessListeners.forEach { listener ->
            tmp.add(listener)
        }

        tmp.add(albumImages::clear)

        albumRepository.getAlbums(searchString, onFailureListener, tmp)
    }
}