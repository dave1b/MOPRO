package ch.hslu.mobpro.firstappfinal.lifecyclelog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import ch.hslu.mobpro.firstappfinal.R

class LifecycleLogFragment : Fragment(R.layout.fragment_lifecycle_logger) {

	companion object {
		fun newInstance(): LifecycleLogFragment {
			return LifecycleLogFragment()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.i("hslu_mobApp", "Fragment onCreate() aufgerufen")
	}

	fun onCreateView(){
		Log.i("hslu_mobApp", "Fragment onCreateView() aufgerufen")
	}
	fun onViewCreated(){
		Log.i("hslu_mobApp", "Fragment onViewCreated() aufgerufen")
	}
	fun onActivityCreated(){
		Log.i("hslu_mobApp", "Fragment onActivityCreated() aufgerufen")
	}
	override fun onStart(){
		super.onStart()
		Log.i("hslu_mobApp", "Fragment onStart() aufgerufen")
	}
	override fun onResume(){
		super.onResume()
		Log.i("hslu_mobApp", "Fragment onResume() aufgerufen")
	}
	override fun onPause(){
		super.onPause()
		Log.i("hslu_mobApp", "Fragment onPause() aufgerufen")
	}
	override fun onStop(){
		super.onStop()
		Log.i("hslu_mobApp", "Fragment onStop() aufgerufen")
	}
	override fun onDestroy(){
		super.onDestroy()
		Log.i("hslu_mobApp", "Fragment onDestroy() aufgerufen")
	}
	override fun onDestroyView(){
		super.onDestroyView()
		Log.i("hslu_mobApp", "Fragment onDestroyView() aufgerufen")
	}



	// TODO: Add further implementions of onX-methods.
}
