package lastfmapp.home.de.lastfmapp.adapters

import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lastfmapp.home.de.lastfmapp.network.DownloadImageTask
import lastfmapp.home.de.lastfmapp.R
import lastfmapp.home.de.lastfmapp.data.Album
import lastfmapp.home.de.lastfmapp.databinding.ItemAlbumBinding

/**
 * ListAdapter für Alben, deren Daten von LastFM extrahiert wurden.
 *
 * @param images Die zwischengespeicherten Bilder, welche auf den einzelnen Items zu sehen sind
 * @param createClickListener Funktion, welche einen View.OnClickListener erschafft. Dieser Listener wird beim anklicken eines Items ausgeführt.
 *                            Diese Funktion kommt von außen, da Views zum Anzeigen des Ladefortschritts gegebenenfalls verändert werden müssen
 *                            und diese nicht dem Adapter übergeben werden sollen.
 */
class AlbumListAdapter(private val images: HashMap<Album, Bitmap>, private val createClickListener: (Album) -> View.OnClickListener): ListAdapter<Album, AlbumListAdapter.AlbumViewHolder>(AlbumDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemAlbumBinding = DataBindingUtil.inflate(inflater, R.layout.item_album, parent, false)

        return AlbumViewHolder(images, binding)
    }

    override fun onBindViewHolder(viewHolder: AlbumViewHolder, position: Int) {
        val album = getItem(position)

        viewHolder.bind(album, createClickListener(album))
    }

    /**
     * Viewholder zur Darstellung von Alben in der RecyclerView.
     *
     * @param images Die im AlbumViewModel zwischengespeicherten Bilder, welche auf den einzelnen Items zu sehen sind
     */
    class AlbumViewHolder(private val images: HashMap<Album, Bitmap>, private val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {

        /**
         * Bindet die Daten des Albums sowie den Listener an die View des Items. Wurde das Bild des Albums noch nicht
         * heruntergeladen, so wird dies hier im Hintergrund erledigt.
         */
        fun bind(album: Album, clickListener: View.OnClickListener) {
            binding.album = album
            binding.onClickListener = clickListener

            binding.imageViewCover.setImageResource(R.mipmap.ic_launcher)

            val image = images[album]

            if(image == null) {
                DownloadImageTask(album.imageUrl, { bitmap ->
                    binding.imageViewCover.setImageBitmap(bitmap)
                    images[album] = bitmap
                }).execute()

            } else {
                binding.imageViewCover.setImageBitmap(image)
            }
        }
    }

    class AlbumDiffCallback: DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(old: Album, new: Album): Boolean {
            return old == new
        }

        override fun areContentsTheSame(old: Album, new: Album): Boolean {
            return old == new
        }
    }
}