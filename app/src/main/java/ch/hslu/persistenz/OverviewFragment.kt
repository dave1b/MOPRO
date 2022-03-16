package ch.hslu.persistenz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    companion object {
    private const val SHARED_PREFERENCES_OVERVIEW = "sharedPreferencesOverview"
        const val COUNTER_KEY = "resumeCount"
        fun newInstance(): OverviewFragment {
            return OverviewFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        saveResumeCountToPrivatePreference()
        updateResumeCounterView()
        //sharedPreferencesViewModel.buildPreferenceSummaryString()
        //fileReaderModel.updateStorageState()
    }

    private fun saveResumeCountToPrivatePreference() {
        val preferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_OVERVIEW, Context.MODE_PRIVATE)
        val newResumeCount = preferences.getInt(COUNTER_KEY,0)+1
        val editor = preferences.edit()
        editor.putInt(COUNTER_KEY, newResumeCount)
        editor.apply()


    }
    private fun updateResumeCounterView() {
        val resumeCount = requireActivity().getSharedPreferences(SHARED_PREFERENCES_OVERVIEW, Context.MODE_PRIVATE)
            .getInt(COUNTER_KEY, 0)
        val resumerCounter = this.view?.findViewById<TextView>(R.id.onResumeTextView)
        val text = "MainActivity.onResume() wurde seit der Isntallation dieser App $resumeCount mal aufgerufen."
        resumerCounter?.text = text
    }




}