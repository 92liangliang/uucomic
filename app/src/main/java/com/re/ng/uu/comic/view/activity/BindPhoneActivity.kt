package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

        tTitle.setText(getString(R.string.bind_phone))
    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }
        tvGetCode.setOnClickListener {
            getCode()
        }
        tvSubmit.setOnClickListener {
            submitBind()
        }
    }

    var RESEND_DURATION = (10 * 60 * 1000).toLong()
    private var resendTimer: CountDownTimer? = null

    fun getCode() {
        var number = etNumber.text.toString()
        sendCode(number);
        resendTimer = object : CountDownTimer(RESEND_DURATION, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val text = String.format(
                    Locale.getDefault(), "%d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
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

    fun submitBind() {
        var number = etNumber.text.toString()
        var verifyCode = etVerifyCode.text.toString()
    }

    private fun sendCode(phoneNumber: String) {
        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().sendcms(phoneNumber),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    showToast("验证码已经发送")
                }
            })
    }
}
