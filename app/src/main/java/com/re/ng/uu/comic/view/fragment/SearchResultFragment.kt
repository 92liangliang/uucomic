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
import com.re.ng.uu.comic.http.bean.BookBean
import com.re.ng.uu.comic.http.bean.SearchBean
import com.re.ng.uu.comic.http.bean.rv_cell.BookCell
import com.re.ng.uu.comic.util.DialogUtil
import com.re.ng.uu.comic.util.StartActUtil
import java.util.*

/**
 * Date    : 2020-12-14
 */

public class SearchResultFragment : BaseAbsFragment() {

    private var mType: String? = null
    private var mKey: String? = null
    private lateinit var mDialog: Dialog

    fun setData(type: String?, key: String?) {
        mType = type
        mKey = key
    }

    override fun onPullRefresh() {
    }

    override fun onLoadMore() {
    }

    override fun onRecyclerViewInitialized() {
        mRefreshLayout.isEnableRefresh = false
        mRefreshLayout.isEnableLoadMore = false
        search()
    }

    override fun initLayoutManger(): RecyclerView.LayoutManager {
        return GridLayoutManager(mActivity, 3)
    }

    private fun showLoadingDialog(){
        mDialog = DialogUtil.showProgressDialog(activity)
        mDialog.setCancelable(true)
        mDialog.show()
    }

    private fun search() {
        showLoadingDialog()
        UUClient.sub( UUClient.getDefault().search("$mKey"),
            object : SimpleObserver<SearchBean>(mDialog) {
                override fun onNext(result: SearchBean) {
                    super.onNext(result)
                    if(result.isSuccess){
                        setSearchResult(result.books)
                    }
                }
            })
    }

    fun setSearchResult(list: List<BookBean>) {
        mRefreshLayout.finishLoadMore()
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