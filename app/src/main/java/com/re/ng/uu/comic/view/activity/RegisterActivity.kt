package com.re.ng.uu.comic.view.activity

import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import com.re.ng.uu.comic.http.bean.LoginData
import com.re.ng.uu.comic.http.bean.UserInfo
import com.re.ng.uu.comic.http.bean.VerifyImg
import com.re.ng.uu.comic.util.StartActUtil
import com.re.ng.uu.comic.view.dialog.AuthCodeDialog
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_top.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    private fun initView() {
        tv_to_login.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
        tv_to_login.paint.isAntiAlias = true//抗锯齿
        iv_back_layout_top.setOnClickListener { finish() }
        tv_title_layout_top.text = "注册"
        tv_to_login.setOnClickListener {
            StartActUtil.toLogin(this)
            finish()
        }
        tv_register.setOnClickListener { onRegisterClick() }
    }

    private fun onRegisterClick() {
        var pwd = et_password.text.toString()
        var pwd2 = et_password_again.text.toString()
        if (TextUtils.isEmpty(et_username.text)) {
            showToast(R.string.please_input_username)
        } else if (TextUtils.isEmpty(pwd)) {
            showToast(R.string.please_input_password)
        } else if (pwd != pwd2) {
            showToast(R.string.password_not_match)
        } else {
            showAuthDialog()
        }
    }

    private fun showAuthDialog() {
        AuthCodeDialog(this, object : AuthCodeDialog.OnAuthListener {
            override fun onAuth(data: VerifyImg?, imgCode: String) {
                register()
            }
        }).show()
    }

    private fun register() {
        showLoadingDialog()
        var username = et_username.text.toString().toLowerCase()
        var password = et_password.text.toString().toLowerCase()
        var invite = et_invite.text.toString().toLowerCase()
        UUClient.sub(UUClient.getDefault().register(username, password, invite),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        showToast("注册成功，正在登陆")
                        login(username, password)
                    }
                }
            })
    }

    private fun login(username: String, password: String) {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().login(username, password),
            object : SimpleObserver<LoginData>(dialog) {
                override fun onNext(result: LoginData) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        result.userInfo.password = password
                        loginSuccess(result.userInfo)
                    }
                }
            })
    }

    private fun loginSuccess(userInfo: UserInfo) {
        APP.getInstance().setUserInfo(userInfo)
        finish()
    }
}
