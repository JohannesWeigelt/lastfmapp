package lastfmapp.home.de.lastfmapp.repositories

import android.arch.lifecycle.MutableLiveData
import lastfmapp.home.de.lastfmapp.network.OnFailureListener
import lastfmapp.home.de.lastfmapp.data.Album
import lastfmapp.home.de.lastfmapp.data.ArtistGetTopAlbumsModel
import lastfmapp.home.de.lastfmapp.network.LastFMApiService
import lastfmapp.home.de.lastfmapp.network.LastFMConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

/**
 * Führt Zugriffe auf die LastFM-Api zum Erhalt der Top-Alben aus und stellt dem AlbumViewModel diese Daten als LiveData zur Verfügung.
 */
class AlbumRepository private constructor() {
    private val lastFMApiService by lazy {
        LastFMApiService.create()
    }

    val albums = MutableLiveData<List<Album>>()


    /**
     *  Startet eine asynchronene Abfrage der Top-Alben des Künstlers, dessen Name übergeben wurde.
     *  Bei Erfolg wird jeder Listener der onSuccessListeners benachrichtigt.
     *  Falls ein Fehler bei der Abfrage auftrat oder die Antwort leer ist wird der onFailureListener benachrichtigt.
     */
    fun getAlbums(searchString: String, onFailureListener: OnFailureListener, onSuccessListeners: List<() -> Unit>) {
        lastFMApiService
            .getTopAlbums(LastFMConstants.LAST_FM_GET_TOP_ALBUMS_METHOD, searchString, LastFMConstants.LAST_FM_API_KEY, LastFMConstants.LAST_FM_FORMAT_JSON)
            .enqueue( object: Callback<ArtistGetTopAlbumsModel.Result> {

                override fun onResponse(call: Call<ArtistGetTopAlbumsModel.Result>, response: Response<ArtistGetTopAlbumsModel.Result>) {
                    try {
                        if(response.isSuccessful) {
                            albums.value = transformModelToData(response.body()!!)

                            onSuccessListeners.forEach { listener ->
                                listener()
                            }
                        }
                    } catch (e: NullPointerException) {
                        onFailureListener.onFailure()
                    }
                }

                override fun onFailure(call: Call<ArtistGetTopAlbumsModel.Result>, t: Throwable) {
                    onFailureListener.onFailure()
                }
            })
    }

    /**
     * Wandelt das Ergebnis der Abfrage der TopAlben in eine Liste von Alben um. Nebenbei werden unsinnige Ergebnisse entfernt.
     *
     * Hinweis: Alben ohne Lieder werden hier nicht entfernt, da dazu ein weiterer API-Zugriff erforderlich wäre.
     */
    private fun transformModelToData(getAlbumsResult: ArtistGetTopAlbumsModel.Result): List<Album> {
        val result = ArrayList<Album>()

        getAlbumsResult.topAlbums.albums.forEach { albumModel ->
            if(!albumModel.name.equals("(null)")) {
                val album = Album(albumModel.name, albumModel.artist.name, albumModel.images[2].url)
                result.add(album)
            }
        }

        return result
    }

    companion object {
        private var instance: AlbumRepository? = null

        fun getInstance(): AlbumRepository {
            if(instance == null) {
                instance = AlbumRepository()
            }

            return instance!!
        }
    }
}