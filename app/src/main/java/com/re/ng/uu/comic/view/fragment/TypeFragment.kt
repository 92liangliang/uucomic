package com.re.ng.uu.comic.view.fragment

import android.app.Dialog
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseLazyFragment
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BookBean
import com.re.ng.uu.comic.http.bean.BookList
import com.re.ng.uu.comic.http.bean.TypeBean
import com.re.ng.uu.comic.http.bean.TypeList
import com.re.ng.uu.comic.http.bean.rv_cell.BookCell
import com.re.ng.uu.comic.util.DialogUtil
import com.re.ng.uu.comic.util.LogUtil
import com.re.ng.uu.comic.util.StartActUtil
import com.re.ng.uu.comic.view.adapter.TypeAdapter
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_type.*

class TypeFragment : BaseLazyFragment() {

    lateinit var mRvList: RecyclerView
    lateinit var typeAdapter: TypeAdapter
    lateinit var llAll: LinearLayout
    lateinit var tvAll: TextView
    lateinit var llSerialize: LinearLayout
    lateinit var tvSerialize: TextView
    lateinit var llFinish: LinearLayout
    lateinit var tvFinish: TextView
    lateinit var refreshLayout: SmartRefreshLayout
    lateinit var mRvListResult: RecyclerView
    var type: Int = -1
    var bookTag: String = ""
    private var page = 0
    private var pageSize = 102
    lateinit var mHistoryAdapter: RvSimpleAdapter
    private lateinit var mDialog: Dialog

    override fun setLayoutResourceId(): Int {
        return R.layout.fragment_type
    }

    override fun lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded) {
            return
        }
        isLoaded = true
    }

    private fun showLoadingDialog() {
        mDialog = DialogUtil.showProgressDialog(activity)
        mDialog.setCancelable(true)
        mDialog.show()
    }

    override fun initView() {
        mRvList = recycler_view
        llAll = linear_layout_all
        tvAll = tv_all
        llSerialize = linear_layout_serialize
        tvSerialize = tv_serialize
        llFinish = linear_layout_finish
        tvFinish = tv_finish
        refreshLayout = refresh_layout
        mRvListResult = recycler_view_result

        refreshLayout.setOnRefreshListener(OnRefreshListener {
            reload()
        })
//        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener {
//            page++
//            getTagBooks(bookTag)
//        })

        llAll.setOnClickListener {
            reloadClick(tvAll, llAll, true)
            reloadClick(tvSerialize, llSerialize, false)
            reloadClick(tvFinish, llFinish, false)
            type = -1
            reload()
        }
        llSerialize.setOnClickListener {
            reloadClick(tvAll, llAll, false)
            reloadClick(tvSerialize, llSerialize, true)
            reloadClick(tvFinish, llFinish, false)
            type = 2
            reload()
        }
        llFinish.setOnClickListener {
            reloadClick(tvAll, llAll, false)
            reloadClick(tvSerialize, llSerialize, false)
            reloadClick(tvFinish, llFinish, true)
            type = 1
            reload()
        }

        mHistoryAdapter = RvSimpleAdapter()
        mRvListResult.setLayoutManager(GridLayoutManager(context, 3))
        mRvListResult.setAdapter(mHistoryAdapter)


        mRvList.setLayoutManager(
            GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
        )

        getTypeList();
    }

    fun reload() {
        page = 0
        getTagBooks(bookTag)
    }

    fun getTypeList() {
        UUClient.sub(UUClient.getDefault().tagList(),
            object : SimpleObserver<TypeList>() {
                override fun onNext(result: TypeList) {
                    super.onNext(result)
                    var typeBean = TypeBean("", 0, "全部", true)
                    var listOfVehicleNames: ArrayList<TypeBean> = result.tags as ArrayList<TypeBean>
                    listOfVehicleNames.add(0, typeBean)
                    setTypeList(listOfVehicleNames)
                }
            })
    }

    fun getTagBooks(bookTag: String) {
        var tag = ""
        if (bookTag.equals("全部")) {
            tag = "";
        } else {
            tag = bookTag
        }
        LogUtil.e("chceck tag=" + tag)
        LogUtil.e("chceck type=" + type)
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().tagBooks(tag, type, page, pageSize),
            object : SimpleObserver<BookList>(mDialog) {
                override fun onNext(result: BookList) {
                    super.onNext(result)
                    if (result.books != null) {
                        setSearchResult(result.books)
                    }
                }
            })
    }

    fun setTypeList(list: List<TypeBean>) {
        typeAdapter = TypeAdapter(mBaseActivity, list)
        typeAdapter.setListener(object : TypeAdapter.OnClickListener {
            override fun onClickItem(type: TypeBean?) {
                if (type != null) {
                    bookTag = type.tag_name
                    getTagBooks(bookTag)
                }
            }

        })
        mRvList.setAdapter(typeAdapter)
        getTagBooks(bookTag);
    }

    fun reloadClick(textView: TextView, linearLayout: LinearLayout, click: Boolean) {
        if (click) {
            textView.setTextColor(
                ContextCompat.getColor(
                    mBaseActivity, R.color.colorWhite
                )
            )
            linearLayout.setBackgroundResource(R.drawable.shape_red)
        } else {
            linearLayout.setBackgroundResource(0)
            textView.setTextColor(
                ContextCompat.getColor(
                    mBaseActivity, R.color.colorBlack
                )
            )
        }
    }

    fun setSearchResult(list: List<BookBean>) {
        isLoaded = true
        if (page == 0) {
            mHistoryAdapter.clear()
        }
        val cellList: MutableList<BaseRvCell<*>> = java.util.ArrayList()
        for (bookBean in list) {
            val bookCell = BookCell(bookBean)
            bookCell.setGrid(true)
            bookCell.setListener(object : OnClickViewRvListener {
                override fun onClick(view: View, position: Int) {}
                override fun <C> onClickItem(data: C, position: Int) {
                    StartActUtil.toBookDetail(mBaseActivity, "${bookBean.book_id}")
                }
            })
            cellList.add(bookCell)
        }
        mHistoryAdapter.addAll(cellList)
    }
}
