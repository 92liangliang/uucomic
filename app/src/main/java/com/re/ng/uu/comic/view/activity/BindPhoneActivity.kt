package com.re.ng.uu.comic.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import kotlinx.android.synthetic.main.activity_bind_phone.*
import kotlinx.android.synthetic.main.layout_top.*
import java.util.*
import java.util.concurrent.TimeUnit


class BindPhoneActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var etNumber: EditText
    lateinit var etVerifyCode: EditText
    lateinit var tvGetCode: TextView
    lateinit var tvSubmit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bind_phone)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        etNumber = et_number
        etVerifyCode = et_verify_code
        tvGetCode = tv_get_code
        tvSubmit = tv_submit


        var userInfo = APP.getInstance().userInfo
        if (!TextUtils.isEmpty(userInfo.mobile)) {
            tTitle.setText("查看手机")
            etNumber.setText(userInfo.mobile)
            relative_layout_send.visibility = View.GONE
            relative_layout_submit.visibility = View.GONE
            etNumber.isClickable = false
            etNumber.isSelected = false
            etNumber.isFocusable = false
            etNumber.isFocusableInTouchMode = false
        } else {
            tTitle.setText("绑定手机")
            relative_layout_send.visibility = View.VISIBLE
            relative_layout_submit.visibility = View.VISIBLE
        }
    }

    protected fun initListener() {
        mIvBack.setOnClickListener {
            onBackPressed()
        }
        tvGetCode.setOnClickListener {
            getCode()
        }
        tvSubmit.setOnClickListener {
            submitBind()
        }
    }

    var RESEND_DURATION = (1 * 60 * 1000).toLong()
    private var resendTimer: CountDownTimer? = null

    fun getCode() {
        var number = etNumber.text.toString()
        if (TextUtils.isEmpty(number)) {
            showToast("请输入手机号码")
            return
        }
        sendCode(number);
    }

    fun submitBind() {
        var number = etNumber.text.toString()
        var verifyCode = etVerifyCode.text.toString()
        if (TextUtils.isEmpty(number)) {
            showToast("请输入手机号码")
            return
        }
        if (TextUtils.isEmpty(verifyCode)) {
            showToast("请输入手机验证码")
            return
        }

        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().bindphone(APP.getInstance().uToken, number, verifyCode),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()
                    }
                }
            })

    }

    private fun sendCode(phoneNumber: String) {

        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().sendcms(APP.getInstance().uToken, phoneNumber),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        showToast("验证码已经发送")
                        resendTimer = object : CountDownTimer(RESEND_DURATION, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                val text = String.format(
                                    Locale.getDefault(), "%02d",
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                                )
                                tvGetCode.setText(text + "s")
                                tvGetCode.setClickable(false)
                            }

                            override fun onFinish() {
                                tvGetCode.setText("获取验证码")
                                tvGetCode.setClickable(true)
                            }
                        }
                        (resendTimer as CountDownTimer).start()
                    }
                }
            })
    }
}
