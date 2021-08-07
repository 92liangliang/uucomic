package com.re.ng.uu.comic.view.dialog

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.CommentData
import com.re.ng.uu.comic.util.Util
import kotlinx.android.synthetic.main.dialog_comment_input.*

/**
 * Date    : 2019-06-20
 */
class CommentInputDialog(val mContext: Context, val bookId: String) : Dialog(mContext, R.style.Dialog_Fullscreen) {

    init {
        val window = window
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        val params = window.attributes
        params.width = ActionBar.LayoutParams.MATCH_PARENT
        params.height = ActionBar.LayoutParams.MATCH_PARENT
        params.gravity = Gravity.BOTTOM
        window.attributes = params
        window.setBackgroundDrawableResource(R.color.transparent)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_comment_input, null)
        setContentView(view)

        view_shadow.setOnClickListener {
            Util.hideSoftKeyboard(et_message)
            cancel()
        }
        Util.showKeyboard(et_message)

        tv_send_message.setOnClickListener {
            var text = et_message.text.toString()
            if (!checkContent(text)) {
                return@setOnClickListener
            }
            submit(text)
        }
    }

    private fun submit(text: String) {
        var dialog = (mContext as BaseActivity).dialog
        dialog.setCancelable(true)
        dialog.show()
        UUClient.sub(
            UUClient.getDefault().subComments(
                bookId,
                text, APP.getInstance().uToken
            ),
            object : SimpleObserver<CommentData?>(dialog) {
                override fun onNext(result: CommentData) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        showToast(result.msg)
                        cancel()
                    }
                }
            })
    }

    private fun showToast(text: String) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show()
    }

    private fun checkContent(text: String): Boolean {
        if (TextUtils.isEmpty(text)) {
            showToast("请输入评论内容!")
            return false
        }
        if (text.length < 3) {
            showToast("评论内容必须超过三个字!")
            return false
        }
//        if (!Util.isCommentValid(text)) {
//            APP.getInstance().showToast(R.string.comment_not_valid, TOAST_TYPE_INFO)
//            return false
//        }
        return true
    }


}
