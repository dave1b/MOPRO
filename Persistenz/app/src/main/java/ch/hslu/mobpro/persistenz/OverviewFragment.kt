package ch.hslu.mobpro.persistenz

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.hslu.mobpro.persistenz.databinding.FragmentOverviewBinding
import java.util.jar.Manifest

class OverviewFragment : Fragment(R.layout.fragment_overview)
{
    private val fileReaderModel: FileReaderViewModel by activityViewModels()
    private val sharedPreferencesViewModel: SharedPreferencesViewModel by activityViewModels()
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission(), object : ActivityResultCallback<Boolean>
        {
            override fun onActivityResult(result: Boolean)
            {
                if (!result)
                {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    return
                }
                else
                {
                    readSms()
                }
            }
        })

    companion object
    {
        private const val SHARED_PREFERENCES_OVERVIEW = "sharedPreferencesOverview"
        const val COUNTER_KEY = "resumeCount"
        fun newInstance(): OverviewFragment
        {
            return OverviewFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.mainButtonOpenPreferences.setOnClickListener({ openPreferencesOnClick() })
        binding.mainButtonSetDefaultPreferences.setOnClickListener({ setDefaultPreferencesOnClick() })
        binding.mainButtonSmsShow.setOnClickListener({ showSmsList() })
        binding.mainButtonLoad.setOnClickListener({ onClickLoadFile() })
        binding.mainButtonSave.setOnClickListener({ onClickSavedFile() })

        sharedPreferencesViewModel.getPreferencesSummary().observe(viewLifecycleOwner, { text ->
            binding.mainPreferencesSummary.text = text
        })

        fileReaderModel.getStorageStatText().observe(viewLifecycleOwner, { text ->
            binding.mainExternalStorageState.text = text
        })

        fileReaderModel.getStoredText().observe(viewLifecycleOwner, { text ->
            binding.mainFileText.setText(text)
        })

        fileReaderModel.isExternalStorageEnabled().observe(viewLifecycleOwner, { isExternalStorageEnabled ->
            binding.mainCheckboxExternalStorage.isEnabled = isExternalStorageEnabled
        })
    }

    override fun onResume()
    {
        super.onResume()
        saveResumeCountToPrivatePreferences()
        updateResumeCounterView()
        sharedPreferencesViewModel.buildPreferenceSummaryString()
        fileReaderModel.updateStorageState()
    }

    private fun saveResumeCountToPrivatePreferences()
    {
        val preferences = requireActivity().getSharedPreferences(SHARED_PREFERENCES_OVERVIEW, Context.MODE_PRIVATE)
        val newResumeCount = preferences.getInt(COUNTER_KEY, 0) + 1
        val editor = preferences.edit()
        editor.putInt(COUNTER_KEY, newResumeCount)
        editor.apply()
    }

    private fun updateResumeCounterView()
    {
        val resumeCount =
            requireActivity().getSharedPreferences(SHARED_PREFERENCES_OVERVIEW, Context.MODE_PRIVATE).getInt(
                COUNTER_KEY, 0
            )
        val resumeCounter = binding.mainResumeCounter as TextView
        val text = "MainActivity.onResume() wurde seit der Installation dieser App $resumeCount mal aufgerufen"
        resumeCounter.text = text
    }

    private fun openPreferencesOnClick()
    {
        parentFragmentManager.beginTransaction().replace(R.id.main_activity, TeaPreferenceFragment.newInstance())
            .addToBackStack("preferences").commit()
    }

    private fun setDefaultPreferencesOnClick()
    {
        sharedPreferencesViewModel.setDefaultPreferences()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun performOperationWithPermission(permission: String)
    {
        val result = requireActivity().checkSelfPermission(permission)
        val granted: Boolean = (result == PackageManager.PERMISSION_GRANTED)
        if (!granted)
        {
            requestPermissionLauncher.launch(permission)
        }
        else
        {
            readSms()
        }
    }

    private fun onClickSavedFile()
    {
        writeFile()
    }

    private fun writeFile()
    {
        fileReaderModel.writeTextToFile(
            binding.mainFileText.text.toString(),
            binding.mainCheckboxExternalStorage.isChecked
        )
    }

    private fun onClickLoadFile()
    {
        readFile()
    }

    private fun readFile()
    {
        fileReaderModel.loadTextFromFile(binding.mainCheckboxExternalStorage.isChecked)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showSmsList()
    {
        performOperationWithPermission(android.Manifest.permission.READ_SMS)
    }

    private fun readSms()
    {
        val cursor = requireActivity().contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(Telephony.Sms.Inbox._ID, Telephony.Sms.Inbox.BODY),
            null,
            null,
            null
        )
        AlertDialog.Builder(context)
            .setTitle("SMS in Inbox")
            .setCursor(cursor, null, Telephony.TextBasedSmsColumns.BODY)
            .setNeutralButton("Ok", null)
            .create()
            .show()
    }

}