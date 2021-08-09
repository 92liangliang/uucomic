package com.re.ng.uu.comic.view.activity

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.re.ng.uu.comic.http.bean.*
import com.re.ng.uu.comic.http.bean.rv_cell.BookCellRaking
import com.re.ng.uu.comic.util.StartActUtil
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_top.*
import java.util.*

class RankingActivity : BaseActivity() {
    lateinit var mIvBack: ImageView
    lateinit var tTitle: TextView
    lateinit var tNew: TextView
    lateinit var vNew: View
    lateinit var lLNew: LinearLayout
    lateinit var tHot: TextView
    lateinit var vHot: View
    lateinit var lLHot: LinearLayout
    lateinit var tFull: TextView
    lateinit var vFull: View
    lateinit var lLFull: LinearLayout
    lateinit var tTopuo: TextView
    lateinit var vTopup: View
    lateinit var lLTopup: LinearLayout
    lateinit var mRvList: RecyclerView
    lateinit var mHistoryAdapter: RvSimpleAdapter
    lateinit var refreshLayout: SmartRefreshLayout
    var type: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)
        initView()
        initListener()
    }

    protected fun initView() {
        mIvBack = iv_back_layout_top
        tTitle = tv_title_layout_top
        tNew = tv_new_ranking
        vNew = v_new_ranking
        tHot = tv_hot
        vHot = v_hot_ranking
        tFull = tv_full_ranking
        vFull = v_full_ranking
        tTopuo = tv_topup_ranking
        vTopup = v_topup_ranking
        lLNew = linear_layout_new
        lLHot = linear_layout_hot
        lLFull = linear_layout_full
        lLTopup = linear_layout_topup
        mRvList = rv_list
        refreshLayout = refresh_layout
        mHistoryAdapter = RvSimpleAdapter()

        var padding = DensityUtil.dp2px(15f)
        rv_list.setPadding(padding, padding, padding, padding)
        refreshLayout.setEnableLoadMore(false)
        rv_list.setLayoutManager(LinearLayoutManager(this))
        rv_list.setAdapter(mHistoryAdapter)

        tTitle.setText(getString(R.string.ranking))
        reloadType(1)

        refreshLayout.setOnRefreshListener(OnRefreshListener {
            reloadType(type)
        })
        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener {
//           onLoadMore()
        })

    }

    protected fun initListener() {
        mIvBack.setOnClickListener { onBackPressed() }
        lLNew.setOnClickListener {
            reloadType(1)
        }
        lLHot.setOnClickListener {
            reloadType(2)
        }
        lLFull.setOnClickListener {
            reloadType(3)
        }
        lLTopup.setOnClickListener {
            reloadType(4)
        }
    }

    fun reloadType(type: Int) {
        this.type = type
        if (type == 1) {
            reloadView(tNew, vNew, true)
            reloadView(tHot, vHot, false)
            reloadView(tFull, vFull, false)
            reloadView(tTopuo, vTopup, false)
            getNew()
        } else if (type == 2) {
            reloadView(tNew, vNew, false)
            reloadView(tHot, vHot, true)
            reloadView(tFull, vFull, false)
            reloadView(tTopuo, vTopup, false)
            getHottest()
        } else if (type == 3) {
            reloadView(tNew, vNew, false)
            reloadView(tHot, vHot, false)
            reloadView(tFull, vFull, true)
            reloadView(tTopuo, vTopup, false)
            getEndBooks()
        } else {
            reloadView(tNew, vNew, false)
            reloadView(tHot, vHot, false)
            reloadView(tFull, vFull, false)
            reloadView(tTopuo, vTopup, true)
            mostCharged()
        }
    }

    fun reloadView(text: TextView, view: View, click: Boolean) {
        if (click) {
            text.setTypeface(text.getTypeface(), Typeface.BOLD)
            view.visibility = View.VISIBLE
        } else {
            text.setTypeface(null)
            view.visibility = View.INVISIBLE
        }
    }

    private fun getNew() {
        showLoadingDialog()
        mHistoryAdapter.clear()
        UUClient.sub(UUClient.getDefault().newestBooks("100"),
            object : SimpleObserver<NewestBooks>(dialog) {
                override fun onNext(result: NewestBooks) {
                    super.onNext(result)
                    if (result.newest != null) {
                        addList(result.newest)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    refreshLayout.finishRefresh()
                }
            })
    }

    private fun getHottest() {
        showLoadingDialog()
        mHistoryAdapter.clear()
        UUClient.sub(UUClient.getDefault().hotBooks("100"),
            object : SimpleObserver<HotBooks>(dialog) {
                override fun onNext(result: HotBooks) {
                    super.onNext(result)
                    if (result.hots != null) {
                        addList(result.hots)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    refreshLayout.finishRefresh()
                }
            })
    }

    private fun getEndBooks() {
        showLoadingDialog()
        mHistoryAdapter.clear()
        UUClient.sub(UUClient.getDefault().endBooks("100"),
            object : SimpleObserver<EndBooks>(dialog) {
                override fun onNext(result: EndBooks) {
                    super.onNext(result)
                    if (result.ends != null) {
                        addList(result.ends)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    refreshLayout.finishRefresh()
                }
            })
    }

    private fun mostCharged() {
        showLoadingDialog()
        mHistoryAdapter.clear()
        UUClient.sub(UUClient.getDefault().mostCharged("100"),
            object : SimpleObserver<mostChargedBooks>(dialog) {
                override fun onNext(result: mostChargedBooks) {
                    super.onNext(result)
                    if (result.most != null) {
                        addList(result.most)
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    refreshLayout.finishRefresh()
                }
            })
    }

    private fun addList(list: List<BookBean>) {
        refreshLayout.finishRefresh()
        val cellList: MutableList<BaseRvCell<*>> = ArrayList()
        for (bookBean in list) {
            val bookCell = BookCellRaking(bookBean)
            bookCell.setListener(object : OnClickViewRvListener {
                override fun onClick(view: View, position: Int) {}
                override fun <C> onClickItem(data: C, position: Int) {
                    StartActUtil.toBookDetail(this@RankingActivity, "${bookBean.book_id}")
                }
            })
            cellList.add(bookCell)
        }
        mHistoryAdapter.addAll(cellList)
    }


}
