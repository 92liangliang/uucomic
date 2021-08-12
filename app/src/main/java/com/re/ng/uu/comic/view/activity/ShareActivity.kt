package com.re.ng.uu.comic.view.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.DownLineBean
import com.re.ng.uu.comic.http.bean.ShareData
import com.re.ng.uu.comic.http.bean.rv_cell.DownLineCell
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.layout_top.*
import java.util.*

class ShareActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var tGetLink: TextView
    lateinit var tvAmount: TextView
    lateinit var tvShareUrl: TextView
    lateinit var mRvList: RecyclerView
    lateinit var v: View
    lateinit var mHistoryAdapter: RvSimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        tGetLink = tv_submit
        tvAmount = text_amount
        tvShareUrl = tv_url
        mRvList = rv_list
        v = view
        mHistoryAdapter = RvSimpleAdapter()

        mRvList.setLayoutManager(LinearLayoutManager(this))
        mRvList.setAdapter(mHistoryAdapter)
        tTitle.setText(getString(R.string.share_title))
    }

    protected fun initListener() {
        mIvBack.setOnClickListener {
            onBackPressed()
        }
        tGetLink.setOnClickListener {
            var url = tvShareUrl.text.toString();
            if (url != null) {
                copyMethod(url)
            } else {
                getLink()
            }
        }
        getLink()
    }

    fun getLink() {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().getShare(APP.getInstance().uToken),
            object : SimpleObserver<ShareData>(dialog) {
                override fun onNext(result: ShareData) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        var s = result.data
                        tvAmount.setText(s.promotion_sum)
                        tvShareUrl.setText(s.shareUrl)
                        if (s.rewards != null) {
                            var rewad = s.rewards
                            if (rewad.data != null) {
                                v.visibility = View.VISIBLE
                                addList(rewad.data)
                            } else {
                                v.visibility = View.GONE
                            }
                        }
                    }
                }
            })
    }

    private fun addList(list: List<DownLineBean>) {
        val cellList: MutableList<BaseRvCell<*>> = ArrayList()
        for (downBean in list) {
            val downLineCell = DownLineCell(downBean)
            downLineCell.setListener(object : OnClickViewRvListener {
                override fun onClick(view: View, position: Int) {}
                override fun <C> onClickItem(data: C, position: Int) {
                }
            })
            cellList.add(downLineCell)
        }
        mHistoryAdapter.addAll(cellList)
    }

    private fun copyMethod(shareUrl: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", shareUrl)
        clipboard.setPrimaryClip(clip)
        showToast(shareUrl + "\n 复制成功，请分享")
    }

}
