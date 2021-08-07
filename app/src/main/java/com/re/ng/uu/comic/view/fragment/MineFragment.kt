package com.re.ng.uu.comic.view.fragment

import android.text.TextUtils
import android.view.View
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseLazyFragment
import com.re.ng.uu.comic.util.DateUtil
import com.re.ng.uu.comic.util.StartActUtil
import kotlinx.android.synthetic.main.fragment_mine.*
import java.util.*

/**
 * Date    : 2020-11-12
 */
class MineFragment : BaseLazyFragment() {

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
            var userInfo = APP.getInstance().userInfo
            if (TextUtils.isEmpty(userInfo.nick_name)) {
                tv_username.text = "${userInfo.username}"
            } else {
                tv_username.text = "${userInfo.nick_name}"
            }
            var days = DateUtil.diffDays(Date(), Date(userInfo.vip_expire_time * 1000L))
            if (days <= 0) {
                days = 0
            }
            tv_vip_time.text = "VIP: 剩余${days}天"
            tv_amount.text = "账户余额: ${userInfo.balance}元"
            ll_recharge.setOnClickListener {
                StartActUtil.toRecharge(context)
            }
            ll_get_vip.setOnClickListener {
                StartActUtil.toGetVIP(context)
            }
            ll_logout.setOnClickListener {
                logout()
            }
            ll_recharge_history.setOnClickListener {
                StartActUtil.toOrderHistory(context, "recharge", "充值记录")
            }
            ll_order_history.setOnClickListener {
                StartActUtil.toOrderHistory(context, "order", "消费记录")
            }
        } else {
            ll_user_info.visibility = View.GONE
            rl_to_login.visibility = View.VISIBLE
        }
        iv_avatar.setOnClickListener {
            if (!APP.getInstance().isUserLogin) {
                toLogin()
            }
        }
        rl_to_login.setOnClickListener { if (!APP.getInstance().isUserLogin) {
            toLogin()
        } }
    }

    private fun logout() {
        APP.getInstance().setUserInfo(null)
        refreshView()
    }

    private fun toLogin() {
        StartActUtil.toLogin(context)
    }
}