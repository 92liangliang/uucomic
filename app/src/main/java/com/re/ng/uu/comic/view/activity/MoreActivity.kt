package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.view.View
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.view.fragment.MoreFragment
import kotlinx.android.synthetic.main.layout_top.*

class MoreActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val intent = intent
        if (intent != null) {
            var mTitle = intent.getStringExtra("title")
            tv_title_layout_top.text = "$mTitle"
            val f = MoreFragment()
            f.setData(
                intent.getStringExtra("type")
            )
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_root_act_type_detail, f)
            transaction.commit()
        }
        iv_back_layout_top.setOnClickListener(View.OnClickListener { onBackPressed() })
    }


}
