package com.re.ng.uu.comic.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.base.BaseLazyFragment
import com.re.ng.uu.comic.view.fragment.BookShelf.BookShelfFragment
import com.re.ng.uu.comic.view.fragment.HomeFragment
import com.re.ng.uu.comic.view.fragment.MineFragment
import com.re.ng.uu.comic.view.fragment.TypeFragment
import com.re.ng.uu.comic.view.widget.NoScrollViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var mVpContainer: NoScrollViewPager
    lateinit var mBnvBottom: BottomNavigationView
    lateinit var mFramgnet: MineFragment

    private var mFList: ArrayList<BaseLazyFragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mVpContainer = vp_container_act_main
        mBnvBottom = bnv_menus_act_main
        mFramgnet = MineFragment()
        mFList.add(HomeFragment())
        mFList.add(TypeFragment())
        mFList.add(BookShelfFragment())
        mFList.add(mFramgnet)
        initView()
        initListener()
    }

    private fun initView() {
        mVpContainer.setAdapter(object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFList[position]
            }

            override fun getCount(): Int {
                return mFList.size
            }
        })
        mVpContainer.offscreenPageLimit = 3
        mVpContainer.setScroll(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12333) {
            if (resultCode == Activity.RESULT_OK) {
                mFramgnet.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    protected fun initListener() {
        mBnvBottom.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_homepage -> {
                    mVpContainer.setCurrentItem(0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_type -> {
                    mVpContainer.setCurrentItem(1)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_bookshelf -> {
                    mVpContainer.setCurrentItem(2)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_mine -> {
                    mVpContainer.setCurrentItem(3)
                    mFramgnet.reloadInfo()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
//        mVpContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                when (position) {
//                    0 -> mBnvBottom.setSelectedItemId(R.id.menu_homepage)
//                    1 -> mBnvBottom.setSelectedItemId(R.id.menu_type)
//                    2 -> mBnvBottom.setSelectedItemId(R.id.menu_bookshelf)
//                    3 -> mBnvBottom.setSelectedItemId(R.id.menu_mine)
//                }
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//        })
    }

}
