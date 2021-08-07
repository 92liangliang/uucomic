package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.view.fragment.TagFragment
import kotlinx.android.synthetic.main.layout_top.*

class TagBooksActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_books)

        val intent = intent
        if (intent != null) {
            var title = intent.getStringExtra("title")
            val fragment = TagFragment()
            fragment.bookTag = "${intent.getStringExtra("tag")}"
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fl_root_act_type_detail, fragment)
            transaction.commit()
            tv_title_layout_top.text = title
        }
        iv_back_layout_top.setOnClickListener { finish() }
    }
}
