package lastfmapp.home.de.lastfmapp.data

import com.google.gson.annotations.SerializedName

/**
 * Datenträger für Ergebnisse, welche mit der LastFM - Methode album.getInfo erlangt wurden.
 * Die gesuchten Lieder sind hier enthalten.
 */
object AlbumGetInfoModel {
    data class Result(
        @SerializedName("album") val album: Album
    )

    data class Album(
        @SerializedName("name") val name: String,
        @SerializedName("artist") val artist: String,
        @SerializedName("bitmap") val images: List<Image>,
        @SerializedName("tracks") val tracks: Tracks
    )

    data class Image(
        @SerializedName("#text") val url: String
    )

    data class Tracks(
        @SerializedName("track") val tracks: List<Track>
    )

    data class Track(
        @SerializedName("name") val name: String
    )
}