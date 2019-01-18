package lastfmapp.home.de.lastfmapp

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import lastfmapp.home.de.lastfmapp.adapters.TrackListAdapter
import lastfmapp.home.de.lastfmapp.data.Album
import lastfmapp.home.de.lastfmapp.databinding.FragmentMusicDetailBinding
import lastfmapp.home.de.lastfmapp.network.DownloadImageTask
import lastfmapp.home.de.lastfmapp.viewmodels.AlbumViewModel
import lastfmapp.home.de.lastfmapp.viewmodels.TrackViewModel

/**
 * Ansicht der App, in welcher Name, Künstler und alle Lieder eines vorher ausgewählten Top-Albums angezeigt werden.
 */
class MusicDetailFragment : Fragment() {
    companion object {
        const val ALBUM_KEY = "lastfmapp.home.de.lastfmapp.MusicDetailFragment.album"
    }

    /**
     * Erstellt die View aus dem Layout-Resource-File. Anschließend wird die RecyclerView mit einem TrackListAdapter versehen
     * und das Cover des Albums angezeigt, bzw. vorher heruntergeladen, falls dies noch nicht geschehen ist.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentMusicDetailBinding.inflate(layoutInflater, container, false)
        val args = arguments
        val album = args!!.getSerializable(ALBUM_KEY) as Album

        val trackViewModel = ViewModelProviders.of(activity!!).get(TrackViewModel::class.java)
        val albumViewModel = ViewModelProviders.of(activity!!).get(AlbumViewModel::class.java)

        val image = albumViewModel.albumImages[album]

        binding.album = album

        if(image == null) {
            binding.imageViewCover.setImageResource(R.mipmap.ic_launcher)

            DownloadImageTask(album.imageUrl, { bitmap ->
                binding.imageViewCover.setImageBitmap(bitmap)
                albumViewModel.albumImages[album] = bitmap
            }).execute()

        } else {
            binding.imageViewCover.setImageBitmap(image)
        }

        val adapter = TrackListAdapter()
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTracks.adapter = adapter

        trackViewModel.tracks.observe(viewLifecycleOwner, Observer { tracks ->
            if (tracks != null) {
                adapter.submitList(tracks)
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }
}