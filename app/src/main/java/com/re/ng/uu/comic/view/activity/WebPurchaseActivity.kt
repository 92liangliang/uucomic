package com.re.ng.uu.comic.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.util.LogUtil
import com.re.ng.uu.comic.util.Util
import kotlinx.android.synthetic.main.activity_web.*

class WebPurchaseActivity : BaseActivity() {

    companion object {
        fun intentEnter(context: Context, url: String, title: String) {
            var intent = Intent(context, WebPurchaseActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }

        fun intentEnter(context: Context, html: String) {
            var intent = Intent(context, WebPurchaseActivity::class.java)
            intent.putExtra("html", html)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initView()
    }

    fun initView() {
        var title = intent.getStringExtra("title")
        var url = intent.getStringExtra("url")
        var html = intent.getStringExtra("html")
        if (!TextUtils.isEmpty(title)) {
            setTitle(title)
        }
        initWebSettings()
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url!!)
        }
        if (!TextUtils.isEmpty(html)) {
            webView.loadDataWithBaseURL(null, html!!, "text/html", "utf-8", null)
        }
        webView.setDownloadListener(object : DownloadListener {
            override fun onDownloadStart(
                url: String?,
                userAgent: String?,
                contentDisposition: String?,
                mimetype: String?,
                contentLength: Long
            ) {
//                LogUtil.d("onDownloadStart url = $url ")
                Util.openBrowser(this@WebPurchaseActivity, url)
            }
        })
    }

    private fun initWebSettings() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.blockNetworkImage = false
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progress_bar.progress = newProgress
                if (newProgress >= 100) {
                    progress_bar.visibility = View.GONE
                } else {
                    if (progress_bar.visibility == View.GONE) {
                        progress_bar.visibility = View.VISIBLE
                    }
                }
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                if (request != null
                    && request.url.scheme != null
                ) {
                    LogUtil.d("shouldOverrideUrlLoading: ${request.url} ${request.url.scheme}")
                    if (request.url.scheme == "alipays") {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, request.url)
                            startActivity(intent)
                            return true
                        } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                            return false
                        }
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                if (url == null) {
//                    return false
//                }
//                LogUtil.d("shouldOverrideUrlLoading: $url")
//                try {
//                    if (url.startsWith("weixin://")
//                        || url.startsWith("alipays://")
//                        || url.startsWith("mailto://")
//                        || url.startsWith("tel://") //其他自定义的scheme
//                    ) {
//
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                        startActivity(intent)
//                        return true
//                    }
//                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
//                    return false
//                }
//                return true
//            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}
