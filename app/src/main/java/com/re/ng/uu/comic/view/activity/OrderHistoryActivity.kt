package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.view.fragment.OrderHistoryFragment
import kotlinx.android.synthetic.main.layout_top.*

class OrderHistoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        val intent = intent
        if (intent != null) {
            val fragment = OrderHistoryFragment()
            fragment.setType("${intent.getStringExtra("type")}")
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_fragment_container, fragment)
            transaction.commit()
            tv_title_layout_top.text = intent.getStringExtra("title")
        }
        iv_back_layout_top.setOnClickListener { finish() }
    }
}
