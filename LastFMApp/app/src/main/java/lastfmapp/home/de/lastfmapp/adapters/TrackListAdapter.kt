package lastfmapp.home.de.lastfmapp.adapters

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import lastfmapp.home.de.lastfmapp.R
import lastfmapp.home.de.lastfmapp.data.AlbumGetInfoModel
import lastfmapp.home.de.lastfmapp.data.Track
import lastfmapp.home.de.lastfmapp.databinding.ItemTrackBinding

/**
 * ListAdapter für Lieder, deren Daten von LastFM extrahiert wurden.
 */
class TrackListAdapter: ListAdapter<Track, TrackListAdapter.TrackViewHolder>(TrackDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TrackListAdapter.TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTrackBinding = DataBindingUtil.inflate(inflater, R.layout.item_track, parent, false)

        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: TrackListAdapter.TrackViewHolder, position: Int) {
        val track = getItem(position)

        viewHolder.bind(track)
    }

    class TrackViewHolder(private val binding: ItemTrackBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(track: Track) {
            binding.track = track
        }
    }

    class TrackDiffCallback: DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(old: Track, new: Track): Boolean {
            return old == new
        }

        override fun areContentsTheSame(old: Track, new: Track): Boolean {
            return old == new
        }
    }
}