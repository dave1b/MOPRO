package ch.hslu.persistenz

import android.os.Bundle
import android.preference.PreferenceFragment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceFragmentCompat


class TeaPreferenceFragment :  PreferenceFragmentCompat() {
    companion object {
        fun newInstance(): TeaPreferenceFragment {
            return TeaPreferenceFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)
    }
}