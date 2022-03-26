package ch.hslu.comcon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.work.Data
import androidx.work.Logger
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ch.hslu.comcon.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var demoThread: Thread
    private val THREADS_SLEEP_TIME_IN_MS: Long = 7000
    private val bandsViewModel: BandsViewModel by viewModels()
    //private val bandsViewModel: BandsViewModel = ViewModelProvider(this).get(BandsViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSleep.setOnClickListener { sleepInMainThread(THREADS_SLEEP_TIME_IN_MS) }
        binding.btnDemoThread.setOnClickListener { startDemoThread(THREADS_SLEEP_TIME_IN_MS) }
        binding.btnDemoWorker.setOnClickListener { startDemoWorker(THREADS_SLEEP_TIME_IN_MS) }
        binding.btnServerAnfrage.setOnClickListener { serverAnfrage(); Log.i("info", "from serverAnfrage()") }
        demoThread = createDemoThread(THREADS_SLEEP_TIME_IN_MS)
        bandsViewModel.bands.observe(this) { bands ->
            Log.i("MainActivity|bandListRequest", "bands observed")
            binding.mainNumberOfBands.text = "#Bands " + bands.size
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun sleepInMainThread(msToSleep: Long) {
        Thread.sleep(msToSleep);

    }

    private fun startDemoThread(msToSleep: Long) {
        if (!demoThread.isAlive) {
            demoThread = createDemoThread(msToSleep)
            demoThread.start()
            binding.btnDemoThread.text = "Demo Thread läuft..."
        } else {
            Toast.makeText(this, "DemoThread läuft bereits!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createDemoThread(msToSleep: Long): Thread {
        return object : Thread("hsluDemoThread") {
            override fun run() {
                try {
                    sleep(msToSleep)
                    runOnUiThread {
                        binding.btnDemoThread.text = "Demo-Thread Starten"
                        Toast.makeText(this@MainActivity, "Demo Thread beendet", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("Thread", "Thread did not work intenionally")
                }
            }
        }
    }


    private fun startDemoWorker(msToSleep: Long) {
        val workManager = WorkManager.getInstance(application)
        val demoWorkerRequest = OneTimeWorkRequestBuilder<DemoWorker>()
            .setInputData(
                Data.Builder()
                    .putLong("msToSleep", msToSleep)
                    .build()
            )
            .build()
        workManager.enqueue(demoWorkerRequest)
    }


    private fun serverAnfrage() {
        bandsViewModel.getBands()
        //binding.mainNumberOfBands.text = "#Bands " + bandsViewModel.bands
    }

}