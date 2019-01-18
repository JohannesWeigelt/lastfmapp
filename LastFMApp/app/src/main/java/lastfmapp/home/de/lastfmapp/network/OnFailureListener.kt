package lastfmapp.home.de.lastfmapp.network

/**
 * Stellt Methode onFailure bereit, welche immer dann aufgerufen wird, wenn
 * bei der Abfrage der Daten von LastFM ein Fehler aufgetreten ist.
 *
 * Er dient zur Abgrenzung von den anderen Listenern, welche bei
 * erfolgreicher Abfrage aufgerufen werden.
 */
interface OnFailureListener {
    fun onFailure()
}