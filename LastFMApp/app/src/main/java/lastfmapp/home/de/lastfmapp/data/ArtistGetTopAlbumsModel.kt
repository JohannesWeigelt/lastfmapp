package lastfmapp.home.de.lastfmapp.data
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Datenträger für Ergebnisse, welche mit der LastFM - Methode artist.getTopAlbums erlangt wurden.
 * Die gesuchten Top-Alben sind hier enthalten.
 */
object ArtistGetTopAlbumsModel {
    data class Result(
        @SerializedName("topalbums") val topAlbums: TopAlbums
    )

    data class TopAlbums(@SerializedName("album") val albums: List<Album>)

    data class Album(
        @SerializedName("name") val name: String,
        @SerializedName("artist") val artist: Artist,
        @SerializedName("image") val images: List<Image>
    ): Serializable

    data class Artist(
        @SerializedName("name") val name: String
    )

    data class Image(@SerializedName("#text") val url: String)
}