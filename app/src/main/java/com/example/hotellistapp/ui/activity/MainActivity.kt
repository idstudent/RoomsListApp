package com.example.hotellistapp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.hotellistapp.R
import com.example.hotellistapp.adapter.FragmentAdapter
import com.example.hotellistapp.adapter.RoomsListAdapter
import com.example.hotellistapp.model.ProductInfos
import com.example.hotellistapp.ui.fragment.LikeRoomsFragment
import com.example.hotellistapp.ui.fragment.RoomsFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.tab_button.view.*

class MainActivity : BaseActivity() {
    private lateinit var roomsListAdapter : RoomsListAdapter

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

        main_viewPager.addOnPageChangeListener(object  : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val rememberRecyclerView =  findViewById<RecyclerView>(R.id.remember_list)
                Log.e("tag", "position "+ position)
                val listItems = ArrayList<ProductInfos>()
                compositeDisposable.add(
                    dbManager.roomsRememberDAO().select()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            for (i in it.indices) {
                                listItems.add(
                                    ProductInfos(
                                        it[i].id, it[i].name, it[i].thumbnail, it[i].imgPath,
                                        it[i].subject, it[i].price, it[i].rate, it[i].check
                                    )
                                )
                            }
                            roomsListAdapter = RoomsListAdapter(applicationContext, listItems)

                            rememberRecyclerView.adapter = roomsListAdapter
                            rememberRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                            roomsListAdapter.notifyDataSetChanged()
                        },{

                        })
                )
            }

            override fun onPageSelected(position: Int) {
            }
        })

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