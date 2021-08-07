package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import com.re.ng.uu.comic.http.bean.RechargeMoney
import com.re.ng.uu.comic.util.Util
import com.re.ng.uu.comic.view.adapter.RechargeMoneyAdapter
import kotlinx.android.synthetic.main.activity_recharge.*
import kotlinx.android.synthetic.main.layout_top.*


class RechargeActivity : BaseActivity() {

    lateinit var mAdapter: RechargeMoneyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)
        initView()
    }

    private fun initView() {
        iv_back_layout_top.setOnClickListener { onBackPressed() }
        tv_title_layout_top.text = "充值"
        ll_alipay.isSelected = true
        view_recycler.layoutManager = GridLayoutManager(this, 2)
        mAdapter = RechargeMoneyAdapter(this, getDatas())
        view_recycler.adapter = mAdapter
        tv_submit.setOnClickListener {
            recharge()
        }
        iv_online_service.setOnClickListener {
            Util.openCustomerService(this)
        }
    }

    private fun getDatas(): List<RechargeMoney> {
        var dataList = ArrayList<RechargeMoney>()
        dataList.add(RechargeMoney("30元", 0, 30, false))
        dataList.add(RechargeMoney("50元", 0, 50, false))
        dataList.add(RechargeMoney("100元", 0, 100, true))
        dataList.add(RechargeMoney("200元", 0, 200, false))
        dataList.add(RechargeMoney("300元", 0, 300, false))
        return dataList
    }

    private fun recharge() {
        showLoadingDialog()
        var rechargeMoney = mAdapter.selected
        var time = UUClient.getTime()
        var token = UUClient.getToken(time)
        UUClient.sub(UUClient.getDefault().recharge(
            APP.getInstance().uToken,
            "${rechargeMoney.value}", 1, time, token
        ),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
//                    Util.openHtmlWithWebview(this@RechargeActivity, result)
                    if (result.success == 0) {
                        Util.openBrowser(this@RechargeActivity, result.url)
                        showToast("支付成功后，请重新登录查看余额")
                    }
                }
            })

    }
}
