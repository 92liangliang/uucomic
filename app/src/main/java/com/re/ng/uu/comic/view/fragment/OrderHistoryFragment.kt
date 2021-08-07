package com.re.ng.uu.comic.view.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.base.BaseAbsFragment
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.ChargeHistoryData
import com.re.ng.uu.comic.http.bean.OrderBean
import com.re.ng.uu.comic.http.bean.OrderData
import com.re.ng.uu.comic.http.bean.rv_cell.OrderHistoryCell

/**
 * Date    : 2020-12-14
 */

public class OrderHistoryFragment : BaseAbsFragment() {

    override fun onPullRefresh() {
    }

    override fun onLoadMore() {
    }

    override fun onRecyclerViewInitialized() {
        mRefreshLayout.isEnableRefresh = false
        mRefreshLayout.isEnableLoadMore = false
    }

    override fun initLayoutManger(): RecyclerView.LayoutManager {
        return LinearLayoutManager(mActivity)
    }


    fun setType(type: String){
        when(type){
            "recharge" -> getRechargeHistory()
            else -> getOrderHistory()
        }
    }

    private fun getOrderHistory(){
        UUClient.sub( UUClient.getDefault().orderHistory(APP.getInstance().uToken),
            object : SimpleObserver<OrderData>() {
                override fun onNext(result: OrderData) {
                    super.onNext(result)
                    if(result.isSuccess){
                        setResult(result.spendings)
                    }
                }
            })
    }

    private fun getRechargeHistory(){
        UUClient.sub( UUClient.getDefault().rechargeHistory(APP.getInstance().uToken),
            object : SimpleObserver<ChargeHistoryData>() {
                override fun onNext(result: ChargeHistoryData) {
                    super.onNext(result)
                    if(result.isSuccess){
                        setResult(result.charges)
                    }
                }
            })
    }

    fun setResult(list: List<OrderBean>) {
        for (data in list) {
            val cell = OrderHistoryCell(data)
            mBaseAdapter.add(cell)
        }
    }


}