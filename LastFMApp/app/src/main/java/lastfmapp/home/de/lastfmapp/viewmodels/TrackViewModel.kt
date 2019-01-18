package lastfmapp.home.de.lastfmapp.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import lastfmapp.home.de.lastfmapp.network.OnFailureListener
import lastfmapp.home.de.lastfmapp.data.Track
import lastfmapp.home.de.lastfmapp.repositories.TracksRepository

/**
 * ViewModel, welches Lieder eines ausgewählten Albums trägt.
 */
class TrackViewModel: ViewModel() {
    private val tracksRepository by lazy {
        TracksRepository.getInstance()
    }

    val tracks: LiveData<List<Track>>

    init {
        tracks = tracksRepository.tracks
    }

    fun fetchTracks(artist: String, album: String, onFailureListener: OnFailureListener, vararg onSuccessListeners: () -> Unit) {
        tracksRepository.fetchTracks(artist, album, onFailureListener, onSuccessListeners.asList())
    }
}