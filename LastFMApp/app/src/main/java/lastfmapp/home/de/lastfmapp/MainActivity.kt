package lastfmapp.home.de.lastfmapp

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import lastfmapp.home.de.lastfmapp.databinding.ActivityMainBinding

/**
 * Dient als Grundlage der 2 Hauptansichten der App (MusicMasterFragment und MusicDetailFragment).
 * Sie initialisiert die Toolbar und die Navigation und verbindet jene miteinander.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navigationController = Navigation.findNavController(this, R.id.fragment_nav)

        setSupportActionBar(binding.toolbar)
        NavigationUI.setupActionBarWithNavController(this, navigationController)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragment_nav).navigateUp()
}
