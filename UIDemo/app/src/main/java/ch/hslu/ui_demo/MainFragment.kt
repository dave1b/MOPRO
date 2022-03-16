package ch.hslu.ui_demo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.app.Dialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(R.layout.fragment_main), OnItemSelectedListener  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var main_ratingBar: RadioButton
    private var firstSpinnerSelection = true


    fun onCreate(savedInstanceState: Bundle?, view: View) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
            }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupRatingBar(view);
        this.setupSpinner(view)
        this.setupDialog()
        this.setupDialogButton(view)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint()
    private fun setupRatingBar(view: View) {
        view.findViewById<RatingBar>(R.id.main_ratingBar).setOnRatingBarChangeListener { ratingBar, fl, b ->
            view.findViewById<TextView>(R.id.main_ratingBar_textView).text = "Aha, Du wählst " + ratingBar.rating
        }
    }

    private fun setupSpinner(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if (!this.firstSpinnerSelection) {
            val message = (resources.getString(R.string.spinnerAuswahl) + (parent?.getItemAtPosition(pos) ?: 0))
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
        this.firstSpinnerSelection = false
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    private fun setupDialog(): Dialog {
        val items = resources.getStringArray(R.array.alertDialog)
        val dialog = AlertDialog.Builder(activity)
            .setTitle("Was willst du?")
            .setItems(items) { _, itemPos ->
                val msg = ("Du hast '" + items[itemPos] + "'gewählt")
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show()
            }.setNegativeButton(
                "Stop", null
            )
        return dialog.create()
    }

    private fun setupDialogButton(view: View) {
        view.findViewById<Button>(R.id.dialog_button).setOnClickListener {
            setupDialog().show()
        }
    }



}