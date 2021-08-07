package com.re.ng.uu.comic.view.fragment

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell
import com.re.ng.uu.comic.base.BaseLazyAbsFragment
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BookBean
import com.re.ng.uu.comic.http.bean.BookList
import com.re.ng.uu.comic.http.bean.rv_cell.BookCell
import com.re.ng.uu.comic.util.StartActUtil
import java.util.*

class TagFragment : BaseLazyAbsFragment() {

    private var page = 0
    private var pageSize = 12
    var bookTag = ""

    override fun initLayoutManger(): RecyclerView.LayoutManager? {
        return GridLayoutManager(mActivity, 3)
    }

    override fun lazyLoad() {
    }

    override fun onPullRefresh() {
        page = 0
        getTagBooks()
    }

    override fun onLoadMore() {
        page ++
        getTagBooks()
    }

    override fun onRecyclerViewInitialized() {
        mRefreshLayout.isEnableLoadMore = false
        page = 0
        getTagBooks()
    }

    private fun getTagBooks(){
        UUClient.sub(UUClient.getDefault().tagBooks(bookTag, page, pageSize),
            object : SimpleObserver<BookList>(mRefreshLayout) {
            override fun onNext(result: BookList) {
                super.onNext(result)
                if(result.books != null){
                    setSearchResult(result.books)
                }
            }
        } )
    }

    fun setSearchResult(list: List<BookBean>) {
        isLoaded = true
        if (mRefreshLayout.isRefreshing) {
            mBaseAdapter.clear()
        }
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

//    fun bookDetail(bookId: Int){
//        UUClient.sub(UUClient.getDefault().bookDetail(bookId),
//            object : SimpleObserver<Any>(mRefreshLayout) {
//                override fun onNext(result: Any) {
//                    super.onNext(result)
//                }
//            } )
//    }

}
