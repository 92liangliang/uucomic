package com.re.ng.uu.comic.view.activity

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BookBean
import com.re.ng.uu.comic.http.bean.rv_cell.BookCell
import com.re.ng.uu.comic.http.bean.updateBooks
import com.re.ng.uu.comic.util.DialogUtil
import com.re.ng.uu.comic.util.LogUtil
import com.re.ng.uu.comic.util.StartActUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_time.*
import kotlinx.android.synthetic.main.activity_time.refresh_layout
import kotlinx.android.synthetic.main.activity_time.rv_list
import kotlinx.android.synthetic.main.layout_top.*
import java.text.SimpleDateFormat
import java.util.*

class TimeActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var mRvList: RecyclerView
    lateinit var mHistoryAdapter: RvSimpleAdapter
    lateinit var refreshLayout: SmartRefreshLayout
    private lateinit var mDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        initView()
        initListener()
    }

    override fun showLoadingDialog() {
        mDialog = DialogUtil.showProgressDialog(this)
        mDialog.setCancelable(true)
        mDialog.show()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        mRvList = rv_list
        refreshLayout = refresh_layout
        mHistoryAdapter = RvSimpleAdapter()

        refreshLayout.setEnableLoadMore(false)
        mRvList.setLayoutManager(GridLayoutManager(this, 3))
        mRvList.setAdapter(mHistoryAdapter)

        tTitle.setText("漫画更新周期表")

//        refreshLayout.setOnRefreshListener(OnRefreshListener {
//        })
    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }

        val sdf = SimpleDateFormat("EEEE")
        val sdf2 = SimpleDateFormat("yyyy-MM-dd")
        val d = Date()
        var dayOfTheWeek: String = sdf.format(d)
        var dateFormate: String = sdf2.format(d)
        dayOfTheWeek = dayOfTheWeek.replace("星期", "周")
        LogUtil.e("date check =" + dayOfTheWeek)
        reloadDate(dayOfTheWeek)
    }


    private fun getList(date: String) {
        mHistoryAdapter.clear()
        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault2().getUpdate("0", date),
            object : SimpleObserver<updateBooks>(mDialog) {
                override fun onNext(result: updateBooks) {
                    super.onNext(result)
                    if (result.books != null) {
                        setList(result.books)
                    }
                }
            })
    }

    fun setList(list: List<BookBean>) {
        refreshLayout.finishRefresh()
        val cellList: MutableList<BaseRvCell<*>> = ArrayList()
        for (bookBean in list) {
            val bookCell = BookCell(bookBean)
            bookCell.setGrid(true)
            bookCell.setListener(object : OnClickViewRvListener {
                override fun onClick(view: View, position: Int) {}
                override fun <C> onClickItem(data: C, position: Int) {
                    StartActUtil.toBookDetail(this@TimeActivity, "${bookBean.book_id}")
                }
            })
            cellList.add(bookCell)
        }
        mHistoryAdapter.addAll(cellList)
    }

    private fun addAllComicItem(click: Boolean, date: String, dateFormate: String) {
        val rootView = LayoutInflater.from(this).inflate(R.layout.item_time, null)
        val tvDate = rootView.findViewById<TextView>(R.id.text_date)
        val linearLayout = rootView.findViewById<LinearLayout>(R.id.linear_layout)
        if (click) {
            tvDate.setTypeface(tvDate.getTypeface(), Typeface.BOLD)
            getList(dateFormate)
        } else {
            tvDate.setTypeface(null)
        }
        tvDate.setText(date)
        linearLayout.setOnClickListener {
            reloadDate(date)
        }
        linear_layout_tab.addView(rootView)
    }

    fun reloadDate(date: String) {
        linear_layout_tab.removeAllViews()
        for (a in 0..6) {
            var dt = Date()
            val c = Calendar.getInstance()
            c.time = dt
            c.add(Calendar.DATE, -a)
            dt = c.time
            val sdf = SimpleDateFormat("EEEE")
            val sdf2 = SimpleDateFormat("yyyy-MM-dd")
            var dayOfTheWeek: String = sdf.format(dt)
            var dateFormate: String = sdf2.format(dt)
            LogUtil.e("date check =" + dateFormate)

            dayOfTheWeek = dayOfTheWeek.replace("星期", "周")
            if (date.equals(dayOfTheWeek)) {
                addAllComicItem(true, dayOfTheWeek, dateFormate)
            } else {
                addAllComicItem(false, dayOfTheWeek, dateFormate)
            }
        }
    }

}
