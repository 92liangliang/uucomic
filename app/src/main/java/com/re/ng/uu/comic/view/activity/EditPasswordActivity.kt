package com.re.ng.uu.comic.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import kotlinx.android.synthetic.main.activity_edit_nickname.text_submit
import kotlinx.android.synthetic.main.activity_edit_password.*
import kotlinx.android.synthetic.main.layout_top.*


class EditPasswordActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var editOdPasswordText: EditText
    lateinit var editPasswordText: EditText
    lateinit var editConfirmPasswordText: EditText
    lateinit var textSubmit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        editOdPasswordText = et_old_password
        editPasswordText = et_new_password
        editConfirmPasswordText = et_confirm_password
        textSubmit = text_submit

        tTitle.setText("修改密码")
    }

    protected fun initListener() {
        mIvBack.setOnClickListener {
            onBackPressed()
        }
        textSubmit.setOnClickListener {
            submitNick()
        }
    }

    fun submitNick() {
        var userInfo = APP.getInstance().userInfo

        var oldPassword = editOdPasswordText.text.toString()
        var newPassword = editPasswordText.text.toString()
        var confirmPassword = editConfirmPasswordText.text.toString()
        var old = userInfo.password
        if (TextUtils.isEmpty(oldPassword)) {
            showToast("请输入旧密码")
            return
        } else if (TextUtils.isEmpty(newPassword)) {
            showToast("请输入新密码")
            return
        } else if (TextUtils.isEmpty(confirmPassword)) {
            showToast("请输入确定密码")
            return
        } else if (!confirmPassword.equals(newPassword)) {
            showToast("和新密码不正确")
            return
        } else if (!old.equals(oldPassword)) {
            showToast("旧密码不正确")
            return
        }
        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().resetPassword(APP.getInstance().uToken, newPassword),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    showToast(result.msg)
                    if (result.isSuccess) {
                        userInfo.password = newPassword
                        APP.getInstance().setUserInfo(userInfo)
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }
            })

    }
}
