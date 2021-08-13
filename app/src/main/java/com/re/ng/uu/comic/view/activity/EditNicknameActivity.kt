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
import kotlinx.android.synthetic.main.activity_edit_nickname.*
import kotlinx.android.synthetic.main.layout_top.*


class EditNicknameActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var editText: EditText
    lateinit var textSubmit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_nickname)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        editText = et_nick_name
        textSubmit = text_submit

        var userInfo = APP.getInstance().userInfo
        tTitle.setText("昵称")
        if (!TextUtils.isEmpty(userInfo.nick_name)) {
            editText.setText(userInfo.nick_name)
        }
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
        var nickName = editText.text.toString()
        if (TextUtils.isEmpty(nickName)) {
            showToast("请输入昵称")
            return
        }
        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().updateNickName(APP.getInstance().uToken, nickName),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    showToast(result.msg)
                    if (result.isSuccess) {
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }
            })

    }
}
