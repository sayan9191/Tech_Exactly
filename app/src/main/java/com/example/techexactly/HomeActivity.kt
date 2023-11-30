package com.example.techexactly

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.techexactly.MyViewPageAdapter
import com.example.techexactly.R
import com.example.techexactly.ui.theme.DataLoadListener
import com.google.android.material.tabs.TabLayout

class HomeActivity : AppCompatActivity(), DataLoadListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.viewPager)

        // Create an adapter that returns a fragment for each of the two tabs
        val pagerAdapter = MyViewPageAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager)

        // Set the titles for the tabs
        tabLayout.getTabAt(0)?.text = "Application"
        tabLayout.getTabAt(1)?.text = "Settings"
    }

    override fun isLoaded(isLoaded: Boolean) {
        if (isLoaded) {

        }
    }
}
