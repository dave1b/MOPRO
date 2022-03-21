package ch.hslu.mobpro.persistenz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import java.lang.IllegalArgumentException
import java.lang.StringBuilder

class SharedPreferencesViewModel(application: Application) : AndroidViewModel(application)
{
    private val PREF_TEA_PREFFERED = "teaPreferred"
    private val PREF_TEA_WITH_SUGAR = "teaWithSugar"
    private val PREF_TEA_SWEETENER = "teaSweetener"

    private var preferencesSummary: MutableLiveData<String> = MutableLiveData()

    fun getPreferencesSummary(): LiveData<String>
    {
        return preferencesSummary
    }

    fun setDefaultPreferences()
    {
        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val editor = prefs.edit()
        val value = "Lipton/Pfefferminztee"
        editor.putString(PREF_TEA_PREFFERED, value)
        editor.putBoolean(PREF_TEA_WITH_SUGAR, true)
        editor.putString(PREF_TEA_SWEETENER, "natural")
        editor.apply()
        buildPreferenceSummaryString()
    }

    fun buildPreferenceSummaryString()
    {
        val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication<Application>().applicationContext)
        val summary = StringBuilder()
        summary.append("Ich trinke am liebsten ")
        summary.append(prefs.getString(PREF_TEA_PREFFERED, "Lipton/Pfefferminztee"))
        summary.append(", ")
        if (prefs.getBoolean(PREF_TEA_WITH_SUGAR, false))
        {
            val sweetenerDisplayValue = getSweetenerDisplayValue(prefs.getString(PREF_TEA_SWEETENER, "natural"))
            summary.append("mit ")
            summary.append(sweetenerDisplayValue)
            summary.append(" gesüsst.")
        }
        else
        {
            summary.append("ungesüsst.")
        }
        preferencesSummary.value = summary.toString()
    }

    private fun getSweetenerDisplayValue(sweetenerValue: String?): String
    {
        val res = getApplication<Application>().applicationContext.resources
        val sweetenerValues = res.getStringArray(R.array.teaSweetenerValues)
        for (i in sweetenerValues.indices)
        {
            if (sweetenerValues[i] == sweetenerValue)
            {
                return res.getStringArray(R.array.teaSweetener)[i]
            }
        }
        throw IllegalArgumentException("Sweetener value " + sweetenerValue + "has no display value?")
    }
}