package ch.hslu.mobpro.persistenz

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class TeaPreferenceFragment : PreferenceFragmentCompat()
{
    companion object
    {
        fun newInstance(): TeaPreferenceFragment
        {
            return TeaPreferenceFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}