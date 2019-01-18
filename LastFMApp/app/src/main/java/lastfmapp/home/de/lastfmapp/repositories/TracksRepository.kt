package lastfmapp.home.de.lastfmapp.repositories

import android.arch.lifecycle.MutableLiveData
import lastfmapp.home.de.lastfmapp.network.OnFailureListener
import lastfmapp.home.de.lastfmapp.data.AlbumGetInfoModel
import lastfmapp.home.de.lastfmapp.data.Track
import lastfmapp.home.de.lastfmapp.network.LastFMApiService
import lastfmapp.home.de.lastfmapp.network.LastFMConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Führt Zugriffe auf die LastFM-Api zum Erhalt der Lieder eines Albums aus und stellt dem TrackViewModel diese Daten als LiveData zur Verfügung.
 */
class TracksRepository {
    private val lastFMApiService by lazy {
        LastFMApiService.create()
    }

    val tracks = MutableLiveData<List<Track>>()

    /**
     *  Startet eine asynchronene Abfrage aller Lieder eines Albums, dessen Name und Künstler übergeben wurde.
     *  Bei Erfolg wird jeder Listener der onSuccessListeners benachrichtigt.
     *  Falls ein Fehler bei der Abfrage auftrat oder die Antwort leer ist wird der onFailureListener benachrichtigt.
     */
    fun fetchTracks(artist: String, album: String, onFailureListener: OnFailureListener, onSuccessListeners: List<() -> Unit> ) {
        lastFMApiService
            .getTracksOfAlbum(LastFMConstants.LAST_FM_GET_ALBUM_INFO_METHOD, artist, album, LastFMConstants.LAST_FM_API_KEY, LastFMConstants.LAST_FM_FORMAT_JSON)
            .enqueue(object: Callback<AlbumGetInfoModel.Result> {
                override fun onResponse(call: Call<AlbumGetInfoModel.Result>, response: Response<AlbumGetInfoModel.Result>) {
                    if(response.isSuccessful) {
                        tracks.value = transformModelToData(response.body()!!)

                        onSuccessListeners.forEach { listener ->
                            listener()
                        }
                    }
                }

                override fun onFailure(call: Call<AlbumGetInfoModel.Result>, t: Throwable) {
                    onFailureListener.onFailure()
                }
            })
    }

    /**
     * Wandelt das Ergebnis der Abfrage der Lieder eines Albums in eine Liste von Alben um.
     */
    private fun transformModelToData(getTracksResult: AlbumGetInfoModel.Result): List<Track> {
        val result = ArrayList<Track>()

        getTracksResult.album.tracks.tracks.forEach { trackModel ->
            val track = Track(trackModel.name)

            result.add(track)
        }

        return result
    }

    companion object {
        private var instance: TracksRepository? = null

        fun getInstance(): TracksRepository {
            if(instance == null) {
                instance = TracksRepository()
            }

            return instance!!
        }
    }
}