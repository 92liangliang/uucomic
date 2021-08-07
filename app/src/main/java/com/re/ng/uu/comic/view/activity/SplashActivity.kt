package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.os.Handler
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.LoginData
import com.re.ng.uu.comic.http.bean.UserInfo
import com.re.ng.uu.comic.util.StartActUtil
import org.litepal.LitePal

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            login()
        }, 500)
    }

    private fun login(){
        var userinfo = LitePal.findFirst(UserInfo::class.java)
        if(userinfo != null){
                UUClient.sub(UUClient.getDefault().login(userinfo.username, userinfo.password),
                    object : SimpleObserver<LoginData>() {
                        override fun onNext(result: LoginData) {
                            super.onNext(result)
                            if (result.isSuccess) {
                                result.userInfo.password = userinfo.password
                                APP.getInstance().setUserInfo(result.userInfo)
                            }
                            toMainActivity()
                        }

                        override fun onError(e: Throwable) {
                            super.onError(e)
                            toMainActivity()
                        }
                    })
        }else{
            toMainActivity()
        }
    }

    private fun toMainActivity(){
        StartActUtil.toMainAct(this)
        finish()
    }
}
