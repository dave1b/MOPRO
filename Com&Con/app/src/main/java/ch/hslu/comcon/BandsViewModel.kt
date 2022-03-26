package ch.hslu.comcon

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection


class BandsViewModel : ViewModel() {
    val bands: MutableLiveData<List<BandCode>> = MutableLiveData()
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://wherever.ch/hslu/rock-bands/all.json/")
        .build()
    private val bandsService = retrofit.create(BandsApiService::class.java)

    fun getBands() {
        Log.i("viemodel", "in view model")
        val call = bandsService.getBandNames()
        call.enqueue(object: Callback<List<BandCode>> {
            override fun onResponse(
                call: Call<List<BandCode>>,
                response: Response<List<BandCode>>
            ) {
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    bands.value = response.body().orEmpty()
                }
            }

            override fun onFailure(call: Call<List<BandCode>>, t: Throwable) {
                Log.e("BandsViewModel|getBandList", t.localizedMessage ?: "call to API onFailure()")
            }
        })
    }
}