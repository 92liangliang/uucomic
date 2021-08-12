package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import kotlinx.android.synthetic.main.activity_exchange_vip.*
import kotlinx.android.synthetic.main.layout_top.*

class ExChangerVipActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var tSubmit: TextView
    lateinit var etCode: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_vip)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        tSubmit = tv_submit
        etCode = et_code

        tTitle.setText(getString(R.string.exchange_vip))
    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }
        tSubmit.setOnClickListener {
            var code = etCode.text.toString();
            if(code !=null) {
                exchangeVip(code)
            }else{
                showToast("请输入兑换码")
            }
        }

    }

    private fun exchangeVip(code: String) {

        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().exchangeVip(APP.getInstance().uToken, code),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    showToast("成功")
                }
            })
    }

}
