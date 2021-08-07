package com.re.ng.uu.comic.view.fragment

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseLazyFragment
import kotlinx.android.synthetic.main.fragment_type.*

class TypeFragment : BaseLazyFragment() {

    lateinit var mTabLayout: TabLayout
    lateinit var mVpContainer: ViewPager
    private var mKindTitleList: ArrayList<String> = ArrayList()
    private var mFList: ArrayList<TypeContainerFragment> = ArrayList()

    override fun initView() {
        mTabLayout = tl_top_fragment_type
        mVpContainer = vp_container_fragment_type
        mKindTitleList.add("题材")
//        mKindTitleList.add("进度")
    }

    override fun lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded) {
            return
        }
        for (i in mKindTitleList.indices) {
            val f = TypeContainerFragment()
            f.setKind(i)
            mFList.add(f)
        }
        mVpContainer.setAdapter(object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFList.get(position)
            }

            override fun getCount(): Int {
                return mFList.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mKindTitleList.get(position)
            }
        })
        mTabLayout.setupWithViewPager(mVpContainer)
        mVpContainer.setOffscreenPageLimit(mFList.size)
        isLoaded = true
    }

    private fun getKindTitleList() {

    }

    override fun setLayoutResourceId(): Int {
        return R.layout.fragment_type
    }

}
