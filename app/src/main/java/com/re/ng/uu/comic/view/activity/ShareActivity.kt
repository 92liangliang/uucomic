package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_top.*

class ShareActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var tGetLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        tGetLink = tv_submit

        tTitle.setText(getString(R.string.share_title))
    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }
        tGetLink.setOnClickListener { getLink() }
    }

    fun getLink() {
        showLoadingDialog()
        var time = UUClient.getTime()
        var token = APP.getInstance().uToken
        UUClient.sub(
            UUClient.getDefault().getShare(time,token),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
//                    if (result.hots != null) {
//                        addList(result.hots)
//                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
//                    refreshLayout.finishRefresh()
                }
            })
    }

}
