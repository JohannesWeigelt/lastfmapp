package lastfmapp.home.de.lastfmapp.data

import java.io.Serializable

/**
 * Repräsentiert ein von LastFM extrahiertes Musikalbum, jedoch nur mit für die Anwendung relevanten Daten.
 */
data class Album(val name: String, val artist: String, val imageUrl: String): Serializable