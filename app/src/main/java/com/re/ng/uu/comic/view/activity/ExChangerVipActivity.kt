package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import kotlinx.android.synthetic.main.layout_top.*

class ExChangerVipActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_vip)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top

        tTitle.setText(getString(R.string.exchange_vip))
    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }

    }

}
