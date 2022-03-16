package ch.hslu.ui_demo

import androidx.lifecycle.ViewModel

class CounterViewModel2: ViewModel() {
    private var counter = 0
    fun incCounter() {
        counter++
    }
    fun getCounter(): Int {
        return counter
    }


}