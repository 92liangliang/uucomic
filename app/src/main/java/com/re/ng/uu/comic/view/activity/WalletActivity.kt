package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.util.StartActUtil
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.layout_top.*

class WalletActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top

        tTitle.setText(getString(R.string.my_wallet))
    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }
        ll_recharge_history.setOnClickListener {
            StartActUtil.toOrderHistory(this, "recharge", "充值记录")
        }
        ll_order_history.setOnClickListener {
            StartActUtil.toOrderHistory(this, "order", "消费记录")
        }
        ll_buy.setOnClickListener {
            StartActUtil.toMoreAct(this, "购买作品", "购买作品")
        }
    }

}
