package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.BaseBean
import com.re.ng.uu.comic.http.bean.RechargeMoney
import com.re.ng.uu.comic.util.LogUtil
import com.re.ng.uu.comic.util.Util
import com.re.ng.uu.comic.view.adapter.VipMoneyAdapter
import kotlinx.android.synthetic.main.activity_open_vip.*
import kotlinx.android.synthetic.main.layout_top.*

class OpenVipActivity : BaseActivity() {

    lateinit var mAdapter: VipMoneyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_vip)
        initView()
    }

    private fun initView() {
        iv_back_layout_top.setOnClickListener { onBackPressed() }
        tv_title_layout_top.text = "VIP购买"
        view_recycler.layoutManager = LinearLayoutManager(this)
        mAdapter = VipMoneyAdapter(this, getDatas())
        view_recycler.adapter = mAdapter
        tv_submit.setOnClickListener {
            var data = mAdapter.selected
            pay(data)
        }
        iv_online_service.setOnClickListener {
            Util.openCustomerService(this)
        }
    }

    private fun getDatas(): List<RechargeMoney> {
        var dataList = ArrayList<RechargeMoney>()
        dataList.add(RechargeMoney("1个月VIP", 1, 49, false, "每月￥49"))
        dataList.add(RechargeMoney("6个月VIP", 6, 99, false, "每月￥16"))
        dataList.add(RechargeMoney("12个月VIP",12, 199, true, "每月￥16"))
        return dataList
    }

    private fun pay(data: RechargeMoney) {
        showLoadingDialog()
        var time = UUClient.getTime()
        var token = UUClient.getToken(time)
        UUClient.sub(UUClient.getDefault().buyVip(APP.getInstance().uToken, data.month, time, token),
            object : SimpleObserver<BaseBean>(dialog) {
                override fun onNext(result: BaseBean) {
                    super.onNext(result)
                    if(result.err == 0){
                        showToast("支付成功，请重新登录后查看")
                        finish()
                    }
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    LogUtil.e("error==="+e.message)
                }
            })
    }

//    private fun refreshVipDate(){
//        UUClient.sub(UUClient.getDefault().vipExpireTime(APP.getInstance().uToken),
//            object : SimpleObserver<BaseBean>() {
//                override fun onNext(result: BaseBean) {
//                    super.onNext(result)
//                    if(result.isSuccess){
//                        APP.getInstance().userInfo.vip_expire_time = result.time
//                    }
//                    finish()
//                }
//
//                override fun onError(e: Throwable) {
//                    super.onError(e)
//                }
//            })
//    }
}
