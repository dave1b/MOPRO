package ch.hslu.ui_demo

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels



class ConstraintFragment : Fragment(R.layout.fragment_constraint_layout) {

    private var counter: Int = 0
    private val counterViewModel: CounterViewModel2 by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModelCounterButton(view)
        setUpFragmentCounterButton(view)
    }


    private fun setUpViewModelCounterButton(view: View) {
        view.findViewById<Button>(R.id.viewModelCounter).text =
            "ViewModel: ${counterViewModel.getCounter()}++"
        view.findViewById<Button>(R.id.viewModelCounter).setOnClickListener {
            counterViewModel.incCounter()
            view.findViewById<Button>(R.id.viewModelCounter).text =
                "ViewModel: ${counterViewModel.getCounter()}++"
        }
    }

    private fun setUpFragmentCounterButton(view: View) {
        view.findViewById<Button>(R.id.fragmentCounter).text = "Fragment: ${this.counter}++"
        view.findViewById<Button>(R.id.fragmentCounter).setOnClickListener {
            this.counter++
            view.findViewById<Button>(R.id.fragmentCounter).text = "Fragment: ${this.counter}++"
        }
    }
}