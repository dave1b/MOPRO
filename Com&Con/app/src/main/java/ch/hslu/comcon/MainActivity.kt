package ch.hslu.comcon

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import ch.hslu.comcon.databinding.ActivityMainBinding
import java.util.concurrent.Executors


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
        binding.btnResetViewModel.setOnClickListener { resetBandsData() }
        binding.btnServerAnfrage.setOnClickListener {
            serverAnfrage(); Log.i(
            "info",
            "from serverAnfrage()"
        )
        }
        binding.btnShowBandSelection.setOnClickListener { showBandDialog() }
        demoThread = createDemoThread(THREADS_SLEEP_TIME_IN_MS)
        bandsViewModel.bands.observe(this) { bands ->
            Log.i("MainActivity|bandListRequest", "bands observed")
            binding.mainNumberOfBands.text = "#Bands " + bands.size
        }
        bandsViewModel.currentBandInfo.observe(this) { bandInfo ->
            this.showSelectedBandInfo(bandInfo)
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
    }

    private fun resetBandsData() {
        this.bandsViewModel.resetBandsData()
    }

    private fun showBandDialog() {
        if (this.bandsViewModel.bands.value.isNullOrEmpty()) {
            Log.i("bandSelection", "entered!!")
            Toast.makeText(
                applicationContext,
                "Die Liste von Bands ist leer. Lade sie zuersts.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val bandNames = arrayOfNulls<String>(this.bandsViewModel.bands.value!!.size)

        for (i in this.bandsViewModel.bands.value!!.indices) {
            bandNames[i] = this.bandsViewModel.bands.value!![i].name
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Welche Band?")
        builder.setItems(bandNames) { dialog, index ->
            bandsViewModel.getCurrentBand(this.bandsViewModel.bands.value!![index].code)
        }
        builder.setNegativeButton("Abbrechen") { _, _ ->
            Toast.makeText(this@MainActivity, "Nichts ausgewählt", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    private fun showSelectedBandInfo(bandInfo: BandInfo?) {

        if(bandInfo?.name.isNullOrEmpty()){
            binding.bandName.text = ""
            binding.originFounding.text = ""
            binding.imageBand.visibility = GONE
            return
        }
        val bandInfo: BandInfo = bandInfo!!
        binding.bandName.text = bandInfo.name
        binding.originFounding.text = bandInfo.homeCountry + " Gründung: " + bandInfo.foundingYear

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val `in` = java.net.URL(bandInfo.bestOfCdCoverImageUrl).openStream()
                var image: Bitmap? = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    binding.imageBand.setImageBitmap(image)
                    binding.imageBand.visibility = VISIBLE
                }
            }
            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}