package com.example.hotellistapp.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.hotellistapp.R
import com.example.hotellistapp.view.adapter.FragmentAdapter
import com.example.hotellistapp.view.fragment.LikeRoomsFragment
import com.example.hotellistapp.view.fragment.RoomsFragment
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.tab_button.view.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roomsViewPager()
    }
    private fun roomsViewPager()  {
        val roomsFragment = RoomsFragment()
        val likeRoomsFragment = LikeRoomsFragment()

        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.addItems(roomsFragment)
        adapter.addItems(likeRoomsFragment)

        main_viewPager.adapter = adapter
        main_tablayout.setupWithViewPager(main_viewPager)

        main_tablayout.getTabAt(0)?.customView = createTab("리스트")
        main_tablayout.getTabAt(1)?.customView = createTab("즐겨찾기")

    }
    @SuppressLint("InflateParams")
    private fun createTab(tabName: String): View {
        val tabView = LayoutInflater.from(applicationContext).inflate(R.layout.tab_button, null)
        tabView.tab_text.text = tabName

        return tabView
    }
}