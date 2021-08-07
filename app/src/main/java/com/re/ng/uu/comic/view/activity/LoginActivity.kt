package com.re.ng.uu.comic.view.activity

import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.EventMessage
import com.re.ng.uu.comic.http.bean.LoginData
import com.re.ng.uu.comic.http.bean.UserInfo
import com.re.ng.uu.comic.util.StartActUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_top.*
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()

    }

    private fun initView() {
        tv_to_register.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
        tv_to_register.paint.isAntiAlias = true//抗锯齿
        tv_forgot_pwd.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_forgot_pwd.paint.isAntiAlias = true
        iv_back_layout_top.setOnClickListener { finish() }
        tv_title_layout_top.text = "登陆"
        tv_to_register.setOnClickListener {
            StartActUtil.toRegister(this)
            finish()
        }
        tv_login.setOnClickListener { onLoginClick() }
        tv_forgot_pwd.setOnClickListener {
            StartActUtil.toResetPwd(this)
        }
        var userInfo = LitePal.findFirst(UserInfo::class.java)
        if (userInfo != null) {
            et_username.setText(userInfo.username+"")
            et_password.setText(userInfo.password+"")
        }
    }

    private fun onLoginClick() {
        var pwd = et_password.text.toString()
        var username = et_username.text.toString()
        if (TextUtils.isEmpty(username)) {
            showToast(R.string.please_input_username)
        } else if (TextUtils.isEmpty(pwd)) {
            showToast(R.string.please_input_password)
        } else {
            login(username.toLowerCase(), pwd.toLowerCase())
        }
    }

    private fun login(username: String, password: String) {
        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().login(username, password),
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
        EventBus.getDefault().post(EventMessage<UserInfo>(EventMessage.USER_INFO, userInfo))
        finish()
    }
}
