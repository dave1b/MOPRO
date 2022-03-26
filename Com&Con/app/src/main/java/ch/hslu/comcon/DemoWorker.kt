package ch.hslu.comcon

import android.content.Context
import androidx.work.Worker
import java.lang.Exception
import androidx.work.WorkerParameters


class DemoWorker(ctx: Context, private val params: WorkerParameters) : Worker(ctx, params) {

    private var msToSleep : Long = 0L

    override fun doWork(): Result {
        return try {
            val waitingTime = params.inputData.getLong("msToSleep", msToSleep)
            Thread.sleep(waitingTime)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}