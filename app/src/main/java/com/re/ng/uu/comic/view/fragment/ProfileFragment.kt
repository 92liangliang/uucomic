package com.re.ng.uu.comic.view.fragment

import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseLazyFragment
import com.re.ng.uu.comic.util.StartActUtil
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseLazyFragment() {

    override fun lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded) {
            return
        }
        isLoaded = true
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.fragment_profile
    }

    override fun initView() {
        btn_regiter.setOnClickListener {
            StartActUtil.toRegister(mBaseActivity)
        }
        btn_login.setOnClickListener {
            StartActUtil.toLogin(mBaseActivity)
        }
    }

}
