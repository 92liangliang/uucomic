package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import com.re.ng.uu.comic.http.bean.VerifyImg
import com.re.ng.uu.comic.util.Util
import com.re.ng.uu.comic.view.dialog.AuthCodeDialog
import kotlinx.android.synthetic.main.activity_forget_pwd.*
import kotlinx.android.synthetic.main.layout_top.*

class ForgetPwdActivity : BaseActivity() {

    var phoneNumber: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pwd)
        initView()
    }


    fun initView() {
        tv_title_layout_top.text = "重置密码"
        tv_get_code.setOnClickListener {
            var phone = et_phone_number.text.toString()
            if (TextUtils.isEmpty(phone)) {
                showToast("请输入手机号！")
            } else {
                phoneNumber = phone
                showAuthDialog()
            }
        }
        btn_submit.setOnClickListener {
            toAuth()
        }
        dialog.setCancelable(true)
        tv_connect_service.setOnClickListener {
            Util.openCustomerService(this)
        }

    }


    private fun showAuthDialog() {
        AuthCodeDialog(this, object : AuthCodeDialog.OnAuthListener {
            override fun onAuth(data: VerifyImg?, imgCode: String) {
                getSmsCode()
            }
        }).show()
    }

    private fun toAuth() {
        if(TextUtils.isEmpty(phoneNumber)){
            showToast("请先获取手机验证码")
            return
        }
        if(TextUtils.isEmpty(et_auth_code.text.toString())) {
            showToast("请输入手机验证码")
            return
        }
        if(TextUtils.isEmpty(et_password.text.toString())) {
            showToast("请输入新密码")
            return
        }
        dialog.show()
//        Client.getInstance().apiService.forgetPassword(ReqForgetPwd(phoneNumber!!,
//            countryCode, et_auth_code.text.toString()))
//            .subscribe(object : SimpleObserver<Any>(dialog) {
//                override fun onNext(result: Any) {
//                    super.onNext(result)
////                    showToast("验证成功", TOAST_TYPE_SUCCESS)
//                    resetPwd(et_password.text.toString())
//                }
//            })
    }

    private fun resetPwd(pwd: String) {
        dialog.show()
//        Client.getInstance().apiService
//            .updatePassword(ReqUpdatePwd(phoneNumber!!, countryCode, pwd, pwd))
//            .subscribe(object : SimpleObserver<Any>(dialog) {
//                override fun onNext(result: Any) {
//                    super.onNext(result)
//                    showToast("重置密码成功!", TOAST_TYPE_SUCCESS)
//                    finish()
//                }
//            })
    }

    private fun getSmsCode() {
        UUClient.sub(
            UUClient.getDefault().sendcms(phoneNumber),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                }
            })
    }

    private fun resetSendBtn() {
        tv_get_code.isEnabled = true
        tv_get_code.text = "获取验证码"
    }

    private inner class TimerCounter(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            resetSendBtn()
        }

        override fun onTick(millisUntilFinished: Long) {
            tv_get_code.text = (millisUntilFinished / 1000).toString()
        }
    }

}
