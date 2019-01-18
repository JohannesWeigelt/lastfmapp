package lastfmapp.home.de.lastfmapp

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.findNavController
import lastfmapp.home.de.lastfmapp.adapters.AlbumListAdapter
import lastfmapp.home.de.lastfmapp.data.Album
import lastfmapp.home.de.lastfmapp.databinding.FragmentMusicMasterBinding
import lastfmapp.home.de.lastfmapp.network.OnFailureListener
import lastfmapp.home.de.lastfmapp.viewmodels.AlbumViewModel
import lastfmapp.home.de.lastfmapp.viewmodels.TrackViewModel

/**
 * Ansicht der App, in welcher ein Künstler eingegeben und dessen Top-Alben angesehen werden können.
 */
class MusicMasterFragment: Fragment() {
    private lateinit var binding: FragmentMusicMasterBinding

    /**
     * Erstellt die View aus dem Layout-Resource-File. Anschließend wird die RecyclerView mit einem AlbumListAdapter versehen,
     * die im AlbumViewModel gespeicherten Alben überwacht und dem Button ein Listener gesetzt, welcher
     * beim Klick die Abfrage der Top-Alben startet.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMusicMasterBinding.inflate(layoutInflater, container, false)
        val viewModel: AlbumViewModel = ViewModelProviders.of(activity!!).get(AlbumViewModel::class.java)

        val adapter = AlbumListAdapter(viewModel.albumImages, this::createAlbumClickListener)
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlbums.adapter = adapter

        viewModel.albums.observe( viewLifecycleOwner, Observer { albums ->
            adapter.submitList(albums)
            adapter.notifyDataSetChanged()
        })

        binding.buttonStartSearch.setOnClickListener {
            val input = binding.editTextMusicianInput.text

            if(!input.equals("")) {
                val onSuccessListener = {
                    binding.information = null
                }

                val onFailureListener = object : OnFailureListener {
                    override fun onFailure() {
                        binding.information = getString(R.string.error_albums)
                    }
                }

                viewModel.search(input.toString(), onFailureListener, onSuccessListener)

                binding.information = getString(R.string.message_loading_albums)

                hideKeyboardFrom(it)
            }
        }

        return binding.root
    }

    /**
     * Erstellt einen ClickListener, welcher eine Abfrage der Lieder eines übergebenen Albums startet
     * und im Erfolgsfall zur Detailansicht des Albums mit all seinen Liedern wechselt. Im Fehlerfall
     * wird eine Fehlermeldung angezeigt.
     */
    private fun createAlbumClickListener(album: Album): View.OnClickListener {
        return View.OnClickListener {
            setViewClickable(binding.recyclerViewAlbums, false)

            val onSuccessListener = {
                val args = Bundle()
                args.putSerializable(MusicDetailFragment.ALBUM_KEY, album)

                binding.information = null
                setViewClickable(binding.recyclerViewAlbums, true)

                it.findNavController().navigate(R.id.dest_music_detail, args)
            }

            val onFailureListener = object : OnFailureListener {
                override fun onFailure() {
                    binding.information = getString(R.string.error_tracks)
                    setViewClickable(binding.recyclerViewAlbums, true)
                }
            }

            binding.information = getString(R.string.message_loading_details)
            ViewModelProviders.of(activity!!).get(TrackViewModel::class.java).fetchTracks(album.artist, album.name, onFailureListener, onSuccessListener)
        }
    }

    /**
     * Verbirgt die Tastatur von Android für eine übergebene View.
     */
    private fun hideKeyboardFrom(view: View) {
        val inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Macht eine View, bzw. eine ViewGroup und all ihre Kinder klickbar oder unklickbar, je nach übergebenem Boolean.
     */
    private fun setViewClickable(view: View, clickable: Boolean) {
        view.isClickable = clickable

        if(view is ViewGroup) {

            for(i in 0..view.childCount - 1) {
                setViewClickable(view.getChildAt(i), clickable)
            }
        }
    }
}