package com.re.ng.uu.comic.view.fragment


import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell
import com.re.ng.uu.comic.base.BaseLazyAbsFragment
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.TypeBean
import com.re.ng.uu.comic.http.bean.TypeList
import com.re.ng.uu.comic.http.bean.rv_cell.TypeBeanCell
import com.re.ng.uu.comic.util.StartActUtil
import com.scwang.smartrefresh.layout.util.DensityUtil
import java.util.*

class TypeContainerFragment : BaseLazyAbsFragment() {

    private var mKind = -1

    fun setKind(kind: Int) {
        mKind = kind
    }

    override fun initLayoutManger(): RecyclerView.LayoutManager? {
        return GridLayoutManager(mActivity, 3)
    }

    override fun lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded) {
            return
        }
        if (mKind != -1) {
            getTypeList(mKind)
        }
    }

    override fun onLoadMore() {
    }

    override fun onRecyclerViewInitialized() {
        var padding = DensityUtil.dp2px(15f)
        mRecyclerView.setPadding(padding, padding, padding, padding)
        mRefreshLayout.isEnableLoadMore = false
    }

    override fun onPullRefresh() {
        getTypeList(mKind)
    }

    public fun getTypeList(kind: Int){
        when(kind){
            0 -> {
                getTypeList()
            }
            1 -> {}
        }
    }

    private fun getTypeList(){
        UUClient.sub(UUClient.getDefault().tagList(), object : SimpleObserver<TypeList>(mRefreshLayout) {
            override fun onNext(result: TypeList) {
                super.onNext(result)
                setTypeList(result.tags)
            }
        } )
    }

    fun setTypeList(list: List<TypeBean>) {
        isLoaded = true
        if (mRefreshLayout.isRefreshing) {
            mBaseAdapter.clear()
        }
        val cellList: MutableList<BaseRvCell<*>> = ArrayList()
        for (bean in list) {
            val beanCell = TypeBeanCell(bean)
            beanCell.setListener(object : OnClickViewRvListener {
                override fun onClick(view: View, position: Int) {
                }
                override fun <C> onClickItem(data: C, position: Int) {
                    StartActUtil.toTagBooksAct(mActivity, bean.tag_name, bean.tag_name)
                }
            })
            cellList.add(beanCell)
        }
        mBaseAdapter.addAll(cellList)
    }

}
