package com.re.ng.uu.comic.view.fragment

import android.app.Dialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell
import com.re.ng.uu.comic.base.BaseAbsFragment
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.*
import com.re.ng.uu.comic.http.bean.rv_cell.BookCell
import com.re.ng.uu.comic.util.DialogUtil
import com.re.ng.uu.comic.util.StartActUtil
import java.util.*

/**
 * Date    : 2020-12-14
 */

public class MoreFragment : BaseAbsFragment() {

    private var mType: String? = null
    private lateinit var mDialog: Dialog

    fun setData(type: String?) {
        mType = type
    }

    override fun onPullRefresh() {
        loadData()
    }

    override fun onLoadMore() {
    }

    override fun onRecyclerViewInitialized() {
        loadData()
    }

    fun loadData() {
        mBaseAdapter.clear()
        if (mType.equals("最新")) {
            getNewest()
        } else if (mType.equals("热门")) {
            getHottest()
        } else if (mType.equals("完结")) {
            getEndBooks()
        } else if (mType.equals("热血")) {
            getTagBooks("热血")
        }
    }

    override fun initLayoutManger(): RecyclerView.LayoutManager {
        return GridLayoutManager(mActivity, 3)
    }

    private fun showLoadingDialog() {
        mDialog = DialogUtil.showProgressDialog(activity)
        mDialog.setCancelable(true)
        mDialog.show()
    }

    private fun getNewest() {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().newestBooks("20"),
            object : SimpleObserver<NewestBooks>(mDialog) {
                override fun onNext(result: NewestBooks) {
                    super.onNext(result)
                    if (result.newest != null) {
                        setList(result.newest)
                    }
                }
            })

    }

    private fun getHottest() {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().hotBooks("20"),
            object : SimpleObserver<HotBooks>(mDialog) {
                override fun onNext(result: HotBooks) {
                    super.onNext(result)
                    if (result.hots != null) {
                        setList(result.hots)
                    }
                }
            })
    }

    private fun getEndBooks() {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().endBooks("20"),
            object : SimpleObserver<EndBooks>(mDialog) {
                override fun onNext(result: EndBooks) {
                    super.onNext(result)
                    if (result.ends != null) {
                        setList(result.ends)
                    }
                }
            })
    }

    private fun getTagBooks(tag: String) {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().tagBooks(tag, 0, 10),
            object : SimpleObserver<BookList>(mDialog) {
                override fun onNext(result: BookList) {
                    super.onNext(result)
                    if (result.books != null) {
                        setList(result.books)
                    }
                }
            })
    }

    fun setList(list: List<BookBean>) {
        mRefreshLayout.finishRefresh()
        val cellList: MutableList<BaseRvCell<*>> = ArrayList()
        for (bookBean in list) {
            val bookCell = BookCell(bookBean)
            bookCell.setGrid(true)
            bookCell.setListener(object : OnClickViewRvListener {
                override fun onClick(view: View, position: Int) {}
                override fun <C> onClickItem(data: C, position: Int) {
                    StartActUtil.toBookDetail(mActivity, "${bookBean.book_id}")
                }
            })
            cellList.add(bookCell)
        }
        mBaseAdapter.addAll(cellList)
    }

}