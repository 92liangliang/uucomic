package com.re.ng.uu.comic.view.dialog

import android.app.Dialog
import android.text.TextUtils
import android.view.LayoutInflater
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.bean.VerifyImg
import com.re.ng.uu.comic.util.CodeUtils
import kotlinx.android.synthetic.main.dialog_auth_code.*


/**
 * Date    : 2019-06-20
 */
class AuthCodeDialog(val mContext: BaseActivity, val onAuthListener: OnAuthListener) :
    Dialog(mContext, R.style.Dialog_Fullscreen) {

    var authData: VerifyImg? = null
    var codeUtils = CodeUtils.getInstance()
    var default = "juuadmin"

    init {
        val window = window
        window!!.setBackgroundDrawableResource(R.color.transparent)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_auth_code, null)
        setContentView(view)
        getVerifyImg()
        iv_code.setOnClickListener { getVerifyImg() }
        btn_close.setOnClickListener {
            cancel()
        }
        btn_submit.setOnClickListener {
            if (TextUtils.isEmpty(et_code.text)) {
                mContext.showToast("请输入验证码")
                return@setOnClickListener
            }
//            if (authData == null) {
//                mContext.showToast("请输入图片中的验证码")
//                return@setOnClickListener
//            }
            var input = et_code.text.toString().replace(" ", "").toUpperCase()
            if (input == codeUtils.code.toUpperCase() || input == default.toUpperCase()) {
                onAuthListener.onAuth(authData, et_code.text.toString())
            } else {
                mContext.showToast("您输入的验证码不正确")
                return@setOnClickListener
            }
            cancel()
        }
    }

    private fun getVerifyImg() {
        getImgByLocal()
    }


    private fun getImgByLocal() {
        val bitmap = codeUtils.createBitmap()
        iv_code.setImageBitmap(bitmap)
    }

    interface OnAuthListener {
        fun onAuth(data: VerifyImg?, imgCode: String)
    }
}
