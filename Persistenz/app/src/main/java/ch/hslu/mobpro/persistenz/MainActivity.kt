package ch.hslu.mobpro.persistenz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(R.layout.activity_main)
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
        {
            supportFragmentManager.beginTransaction().add(R.id.main_activity, OverviewFragment.newInstance()).commit()
        }
    }
}