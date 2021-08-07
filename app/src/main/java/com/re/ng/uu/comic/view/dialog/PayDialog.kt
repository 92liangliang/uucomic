package com.re.ng.uu.comic.view.dialog

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.PayResult
import com.re.ng.uu.comic.util.DialogUtil
import com.re.ng.uu.comic.util.StartActUtil
import kotlinx.android.synthetic.main.dialog_pay.*

/**
 * Date    : 2019-06-20
 */
class PayDialog(val mContext: Context, val chapterId: String, title: String,
                money: String, val mListener: OnPayResultListener) : Dialog(mContext, R.style.Dialog_Fullscreen) {

    init {
        val window = window!!
        val params = window.attributes
        params.width = ActionBar.LayoutParams.MATCH_PARENT
        params.height = ActionBar.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.BOTTOM
        window.attributes = params
        window.setBackgroundDrawableResource(R.color.transparent)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_pay, null)
        setContentView(view)

        tv_title.text = "$title"
        tv_money.text = "${money}元"
        tv_amount.text = "当前余额${APP.getInstance().userInfo.balance}元"
        btn_submit.setOnClickListener {
            purchase()
        }
        tv_to_recharge.setOnClickListener {
            toRecharge()
        }
        btn_close.setOnClickListener {
            cancel()
            (mContext as Activity).finish()
        }
    }

    private fun purchase(){
        var dialog = DialogUtil.showProgressDialog(mContext as BaseActivity)
        UUClient.sub(UUClient.getDefault().buyChapter(APP.getInstance().uToken, chapterId),
            object : SimpleObserver<PayResult>(dialog) {
                override fun onNext(result: PayResult) {
                    super.onNext(result)
                    mContext.showToast("${result.msg}")
                    if(result.isSuccess){
                        APP.getInstance().userInfo.balance = result.balance
                        mListener.onPayResult(true)
                        cancel()
                    }
                }
        })
    }

    private fun toRecharge() {
        StartActUtil.toRecharge(mContext)
        cancel()
    }

    public interface OnPayResultListener{
        public fun onPayResult(isSuccess: Boolean)
    }

}
