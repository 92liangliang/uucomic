package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.*
import com.re.ng.uu.comic.util.LogUtil
import com.re.ng.uu.comic.util.Util
import com.re.ng.uu.comic.view.adapter.ChannelAdapter
import com.re.ng.uu.comic.view.adapter.RechargeMoneyAdapter
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_recharge.*
import kotlinx.android.synthetic.main.layout_top.*


class RechargeActivity : BaseActivity() {

    lateinit var mRvList: RecyclerView
    lateinit var mHistoryAdapter: ChannelAdapter
    lateinit var mAdapter: RechargeMoneyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)
        initView()
    }

    private fun initView() {
        iv_back_layout_top.setOnClickListener { onBackPressed() }
        tv_title_layout_top.text = "充值"
        mRvList = view_recycler_pay

        view_recycler.layoutManager = GridLayoutManager(this, 2)

        tv_submit.setOnClickListener {
            recharge()
        }
        iv_online_service.setOnClickListener {
            Util.openCustomerService(this)
        }

        mRvList.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        getChannel()
        getAmount()
    }

    private fun getDatas(date: List<String>): List<RechargeMoney> {
        var dataList = ArrayList<RechargeMoney>()
        for (amount in date) {
            dataList.add(RechargeMoney(amount + "元", 0, stringToInt(amount), false))
        }
        if (dataList != null) {
            dataList.get(0).isSelected = true
        }

        return dataList
    }

    fun stringToInt(s: String): Int {
        try {
            return Integer.parseInt(s);
        } catch (e: NumberFormatException) {
            return 0
        }
    }

    private fun getChannel() {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().getChannel(APP.getInstance().getUToken()),
            object : SimpleObserver<Channel>(dialog) {
                override fun onNext(result: Channel) {
                    super.onNext(result)
                    if (result.date != null) {
                        addList(result.date)
                    }
                }
            })
    }

    private fun getAmount() {
        showLoadingDialog()
        UUClient.sub(UUClient.getDefault().getAmount(APP.getInstance().getUToken()),
            object : SimpleObserver<Amount>(dialog) {
                override fun onNext(result: Amount) {
                    super.onNext(result)
                    if (result.date != null) {
                        mAdapter =
                            RechargeMoneyAdapter(this@RechargeActivity, getDatas(result.date))
                        view_recycler.adapter = mAdapter
                    }
                }
            })
    }

    private fun addList(list: List<ChannelBeen>) {
        val channelBeens2: MutableList<ChannelBeen> = java.util.ArrayList()
        for (a in 0..list.size - 1) {
            if (a == 0) {
                list.get(a).click = true
            }
            if (list.get(a).switch == 1) {
                channelBeens2.add(list.get(a))
            }
        }

        mHistoryAdapter = ChannelAdapter(this, channelBeens2)
        mRvList.setAdapter(mHistoryAdapter)
    }

    private fun recharge() {
        showLoadingDialog()
        var rechargeMoney = mAdapter.selected
        var time = UUClient.getTime()
        var token = UUClient.getToken(time)
        var type = mHistoryAdapter.selected.type
        UUClient.sub(UUClient.getDefault().recharge(
            APP.getInstance().uToken,
            "${rechargeMoney.value}", type, time, token
        ),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
//                    Util.openHtmlWithWebview(this@RechargeActivity, result)
                    if (result.success != 0) {
                        Util.openBrowser(this@RechargeActivity, result.url)
                        showToast("支付成功后，请重新登录查看余额")
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    LogUtil.e("Error=====", e)
                }
            })

    }
}
