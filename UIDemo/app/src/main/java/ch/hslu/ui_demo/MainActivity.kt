package ch.hslu.ui_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainAcitvityView, MainFragment())
                .commit()
        }
    }


 fun layoutLinearSelected(view: View) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.mainAcitvityView, LinearLayout())
            .commit()
    }

    fun layoutConstraintSelected(view: View) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.mainAcitvityView, ConstraintFragment())
            .commit()
    }

}