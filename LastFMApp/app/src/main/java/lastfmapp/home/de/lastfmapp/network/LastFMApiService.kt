package lastfmapp.home.de.lastfmapp.network

import lastfmapp.home.de.lastfmapp.data.AlbumGetInfoModel
import lastfmapp.home.de.lastfmapp.data.ArtistGetTopAlbumsModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Beschreibt die REST-Requests der LastFM-Methoden artist.getTopAlbums und album.getInfo.
 * Die Implementation wird von Retrofit generiert.
 */
interface LastFMApiService {

    @GET(LastFMConstants.LAST_FM_BASE_URL)
    fun getTopAlbums(
        @Query("method") method: String,
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String): Call<ArtistGetTopAlbumsModel.Result>


    @GET(LastFMConstants.LAST_FM_BASE_URL)
    fun getTracksOfAlbum(
        @Query("method") method: String,
        @Query("artist") artist: String,
        @Query("album") album: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String): Call<AlbumGetInfoModel.Result>

    companion object {

        fun create(): LastFMApiService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(LastFMConstants.LAST_FM_BASE_URL)
                .build()

            return retrofit.create(LastFMApiService::class.java)
        }
    }
}