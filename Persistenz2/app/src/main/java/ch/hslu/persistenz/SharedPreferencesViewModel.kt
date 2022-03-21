package ch.hslu.persistenz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class SharedPreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private val PREF_TEA_PREFFERED = "teaPreferred"
    private val PREF_TEA_WITH_SUGAR = false
    private val PREF_TEA_SWEETENER = "teaSweetener"

    private var preferencesSummary = "Ich trinke am liebsten $PREF_TEA_PREFFERED, mit $PREF_TEA_SWEETENER"
    init {
    setDefaultPreferences()
    }

    fun  getPreferencesSummary(): String {
        return preferencesSummary
    }

    fun setDefaultPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val editor = prefs.edit()
        val value = "Lipton/Pfefferminztee"
        editor.putString(PREF_TEA_PREFFERED, value)
        editor.putBoolean(PREF_TEA_WITH_SUGAR.toString(), true)
        editor.putString(PREF_TEA_SWEETENER, "natural")
        editor.apply()
        buildPrefencesSummaryString()
    }

    private fun buildPrefencesSummaryString() {
        if(this.PREF_TEA_WITH_SUGAR){
        this.preferencesSummary = "Ich trinke am liebsten $PREF_TEA_PREFFERED, mit $PREF_TEA_SWEETENER"
        }

    }



}