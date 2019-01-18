package lastfmapp.home.de.lastfmapp.adapters

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("android:visibility")
fun bindVisibility(view: View, visible: Boolean) {
    view.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}