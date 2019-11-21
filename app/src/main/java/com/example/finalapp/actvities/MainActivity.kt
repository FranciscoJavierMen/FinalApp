package com.example.finalapp.actvities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.finalapp.R
import com.example.finalapp.adapters.PagerAdapter
import com.example.finalapp.fragments.ChatFragment
import com.example.finalapp.fragments.InfoFragment
import com.example.finalapp.fragments.RatesFragment
import com.example.mylibrary.ToolbarActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    private lateinit var adapter: PagerAdapter
    private var prevBottomSelected: MenuItem? = null

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        setUpPagerView(getPagerAdapter())
        setUpButtomNavigationBar()
    }

    private fun initComponents() {
        toolbarToLoad(toolbarView as Toolbar)
    }

    private fun getPagerAdapter():PagerAdapter{
        adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(ChatFragment())
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        return adapter
    }

    private fun setUpPagerView(adapter: PagerAdapter){
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null){
                    bottomNavigation.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }

                bottomNavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = bottomNavigation.menu.getItem(position)
            }

        })
    }

    private fun setUpButtomNavigationBar(){
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_info -> {
                    viewPager.currentItem = 0
                    true
                }

                R.id.nav_rate -> {
                    viewPager.currentItem = 1
                    true
                }

                R.id.nav_chat -> {
                    viewPager.currentItem = 2
                    true
                }

                else -> false
            }
        }
    }
}
