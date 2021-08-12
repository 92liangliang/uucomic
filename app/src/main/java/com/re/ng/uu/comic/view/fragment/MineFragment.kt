package com.re.ng.uu.comic.view.fragment

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseLazyFragment
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.LoginData
import com.re.ng.uu.comic.http.bean.UserInfo
import com.re.ng.uu.comic.util.DateUtil
import com.re.ng.uu.comic.util.StartActUtil
import kotlinx.android.synthetic.main.fragment_mine.*
import org.litepal.LitePal
import java.util.*


/**
 * Date    : 2020-11-12
 */
class MineFragment : BaseLazyFragment() {
    var LAUNCH_SECOND_ACTIVITY: Int = 12333

    override fun lazyLoad() {

    }

    override fun setLayoutResourceId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {

    }

    override fun onResume() {
        super.onResume()
        refreshView()
    }

    private fun refreshView() {
        if (APP.getInstance().isUserLogin) {
            ll_user_info.visibility = View.VISIBLE
            rl_to_login.visibility = View.GONE
            ll_logout.visibility = View.VISIBLE
            var userInfo = APP.getInstance().userInfo
            if (TextUtils.isEmpty(userInfo.nick_name)) {
                tv_username.text = "${userInfo.username}"
            } else {
                tv_username.text = "${userInfo.nick_name}"
            }

            if (!TextUtils.isEmpty(userInfo.mobile)) {
                text_bind_phone.text = "查看手机"
            } else {
                text_bind_phone.text = "绑定手机"
            }
            var days = DateUtil.diffDays(Date(), Date(userInfo.vip_expire_time * 1000L))
            if (days <= 0) {
                days = 0
            }
            tv_vip_time.text = "VIP: 剩余${days}天"
            tv_amount.text = "账户余额: ${userInfo.balance}元"
            ll_bind_phone.setOnClickListener {
                StartActUtil.toBindPhone(activity, LAUNCH_SECOND_ACTIVITY)
            }
            ll_share.setOnClickListener {
                StartActUtil.toShare(context)
            }
            ll_wallet.setOnClickListener {
                StartActUtil.toWallet(context)
            }
            ll_recharge.setOnClickListener {
                StartActUtil.toRecharge(context)
            }
            ll_exchange_vip.setOnClickListener {
                StartActUtil.toExchange(activity, LAUNCH_SECOND_ACTIVITY)
            }
            ll_get_vip.setOnClickListener {
                StartActUtil.toGetVIP(context)
            }
            ll_logout.setOnClickListener {
                logout()
            }
//            ll_recharge_history.setOnClickListener {
//                StartActUtil.toOrderHistory(context, "recharge", "充值记录")
//            }
//            ll_order_history.setOnClickListener {
//                StartActUtil.toOrderHistory(context, "order", "消费记录")
//            }
        } else {
            ll_user_info.visibility = View.GONE
            rl_to_login.visibility = View.VISIBLE
            ll_logout.visibility = View.GONE
        }
        iv_avatar.setOnClickListener {
            if (!APP.getInstance().isUserLogin) {
                toLogin()
            }
        }
        rl_to_login.setOnClickListener {
            if (!APP.getInstance().isUserLogin) {
                toLogin()
            }
        }
    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                reloadInfo()
            }
        }
    }

    private fun logout() {
        APP.getInstance().setUserInfo(null)
        LitePal.deleteAll(UserInfo::class.java)
        refreshView()
    }

    private fun toLogin() {
        StartActUtil.toLogin(context)
    }

    public fun reloadInfo() {
        UUClient.sub(
            UUClient.getDefault().refresh(APP.getInstance().uToken),
            object : SimpleObserver<LoginData>() {
                override fun onNext(result: LoginData) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        result.userInfo.password = APP.getInstance().password
                        result.userInfo.utoken = APP.getInstance().uToken
                        APP.getInstance().setUserInfo(result.userInfo)
                    }
                    refreshView()
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    refreshView()
                }
            })
    }
}