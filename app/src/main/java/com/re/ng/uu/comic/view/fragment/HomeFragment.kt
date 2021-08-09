package com.re.ng.uu.comic.view.fragment

import android.content.Context
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseLazyFragment
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.*
import com.re.ng.uu.comic.listener.OnScrollYChangeForAlphaListener
import com.re.ng.uu.comic.util.ScreenUtil
import com.re.ng.uu.comic.util.StartActUtil
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseLazyFragment() {

    override fun lazyLoad() {
        if (!isVisible || !isPrepared || isLoaded) {
            return
        }
        getHomePageData()
        isLoaded = true
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.fragment_home
    }

    fun setListener() {
//        ll_book_bill_fragment_home.setOnClickListener(View.OnClickListener {
//            StartActUtil.toBookBillAct(
//                mBaseActivity,
//                Constant.TYPE_BOOK_BILL
//            )
//        })
//        ll_rank_fragment_home.setOnClickListener(View.OnClickListener {
//            StartActUtil.toBookBillAct(
//                mBaseActivity,
//                Constant.TYPE_RANK
//            )
//        })
        iv_bg_search_fragment_home.setOnClickListener(View.OnClickListener {
            StartActUtil.toSearchAct(
                mBaseActivity
            )
        })
    }

    override fun initView() {
        // 200dp为banner高度,75dp为标题栏高度
        scrollView_fragment_home.setOffsetDistance(ScreenUtil.dip2px(mBaseActivity, 200f - 75))
        scrollView_fragment_home.setOnScrollYChangeForAlphaListener(object :
            OnScrollYChangeForAlphaListener {
            override fun changeTopLayoutTransport(alpha: Int) {
                setTopLayoutTransparency(alpha)
            }
        })
        setTopLayoutTransparency(0)
        refreshLayout_fragment_home.isEnableLoadMore = false
        refreshLayout_fragment_home.setOnRefreshListener {
            getHomePageData()
        }
        setListener()
        iv_search_fragment_home.setOnClickListener {
            StartActUtil.toSearchAct(context)
        }
        iv_raking.setOnClickListener {
            StartActUtil.toRankingAct(
                mBaseActivity
            )
        }
        iv_time.setOnClickListener {
            StartActUtil.toTimeAct(
                mBaseActivity
            )
        }
    }

    /**
     * 设置标题栏及其内容透明度
     * 0:完全透明
     * 255:完全不透明
     * @param alpha
     */
    fun setTopLayoutTransparency(alpha: Int) {
        cl_top_fragment_home.getBackground().mutate().setAlpha(alpha)
        val scale = alpha / 255f
        //白->红
//        iv_search_fragment_home.setColorFilter(
//            ColorMatrixColorFilter(
//                floatArrayOf(
//                    0f, 0f, 0f, 0f,
//                    255 - scale * 12,
//                    0f, 0f, 0f, 0f,
//                    255 - scale * 153,
//                    0f, 0f, 0f, 0f,
//                    255 - scale * 172,
//                    0f, 0f, 0f, 1f, 0f
//                )
//            )
//        )
        if (scale < 0.2) {
            iv_search_fragment_home.setImageDrawable(getResources().getDrawable(R.mipmap.svg_white_nav_bar_search))
            tv_title_fragment_home.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorGray_9
                )
            )
        } else if (scale > 0.2 && scale < 0.7) {
            iv_search_fragment_home.setImageDrawable(getResources().getDrawable(R.mipmap.svg_white_nav_bar_search))
            tv_title_fragment_home.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorMidRed
                )
            )
        } else {
            iv_search_fragment_home.setImageDrawable(getResources().getDrawable(R.mipmap.svg_red_nav_bar_search))
            tv_title_fragment_home.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorRed
                )
            )
        }
//        iv_bg_left_fragment_home.setAlpha(1 - scale)
        iv_bg_search_fragment_home.setAlpha(1 - scale)
    }


    private fun getHomePageData() {
        ll_item_container.removeAllViews()
        getNewest()
        Handler().postDelayed({ getHottest() }, 500)
        Handler().postDelayed({ getEndBooks() }, 1000)
        Handler().postDelayed({ getTagBooks("热血") }, 1500)
        getBanners()
    }

    private fun getNewest() {
        UUClient.sub(UUClient.getDefault().newestBooks(),
            object : SimpleObserver<NewestBooks>(refreshLayout_fragment_home) {
                override fun onNext(result: NewestBooks) {
                    super.onNext(result)
                    if (result.newest != null) {
                        var itemBean = ItemBean()
                        itemBean.bookBeanList = result.newest
                        itemBean.title = "最新"
                        itemBean.intro = "本季新番火钳刘明"
                        itemBean.iconResId = R.mipmap.svg_pic_list_new
                        addAllComicItem(itemBean)
                    }
                }
            })

    }

    private fun getHottest() {
        UUClient.sub(UUClient.getDefault().hotBooks(),
            object : SimpleObserver<HotBooks>(refreshLayout_fragment_home) {
                override fun onNext(result: HotBooks) {
                    super.onNext(result)
                    if (result.hots != null) {
                        var itemBean = ItemBean()
                        itemBean.bookBeanList = result.hots
                        itemBean.title = "热门"
                        itemBean.intro = ""
                        itemBean.iconResId = R.mipmap.svg_pic_list_jd
                        addAllComicItem(itemBean)
                    }
                }
            })
    }

    private fun getEndBooks() {
        UUClient.sub(UUClient.getDefault().endBooks(), object : SimpleObserver<EndBooks>() {
            override fun onNext(result: EndBooks) {
                super.onNext(result)
                if (result.ends != null) {
                    var itemBean = ItemBean()
                    itemBean.bookBeanList = result.ends
                    itemBean.title = "完结"
                    itemBean.intro = ""
                    itemBean.iconResId = R.mipmap.svg_pic_list_dream
                    addAllComicItem(itemBean)

                }
            }
        })
    }

    private fun getTagBooks(tag: String) {
        UUClient.sub(UUClient.getDefault().tagBooks(tag, 0, 10),
            object : SimpleObserver<BookList>(refreshLayout_fragment_home) {
                override fun onNext(result: BookList) {
                    super.onNext(result)
                    if (result.books != null) {
                        var itemBean = ItemBean()
                        itemBean.bookBeanList = result.books
                        itemBean.title = tag
                        itemBean.intro = ""
                        itemBean.iconResId = R.mipmap.svg_pic_list_fire
                        addAllComicItem(itemBean)
                    }
                }
            })
    }

    /**
     * 添加分类漫画项
     * @param itemBeanList
     */
    private fun addAllComicItem(bean: ItemBean) {
        val rootView = LayoutInflater.from(mBaseActivity).inflate(R.layout.cell_item, null)
        val ivIcon = rootView.findViewById<ImageView>(R.id.iv_icon_cell_item)
        val tvTitle = rootView.findViewById<TextView>(R.id.tv_title_cell_item)
        val tvIntro = rootView.findViewById<TextView>(R.id.tv_intro_cell_item)
        val tvMore = rootView.findViewById<RelativeLayout>(R.id.relative_layout_mode)
        val rvComicList: RecyclerView = rootView.findViewById(R.id.rv_book_cell_item)
        Glide.with(mBaseActivity).load(bean.iconResId).into(ivIcon)
        tvTitle.setText(bean.getTitle())
        tvIntro.setText(bean.getIntro())
        rvComicList.layoutManager = LinearLayoutManager(
            mBaseActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        val rvSimpleAdapter = RvSimpleAdapter()
        rvComicList.adapter = rvSimpleAdapter
        rvSimpleAdapter.addAll(bean.getComicCellList { comicId ->
            StartActUtil.toBookDetail(mBaseActivity, comicId)
        })
        tvMore.setOnClickListener {
            StartActUtil.toMoreAct(mBaseActivity, bean.title, bean.title)
        }
        //添加进布局
        ll_item_container.addView(rootView)
    }

    private fun getBanners() {
        UUClient.sub(UUClient.getDefault().banners(10),
            object : SimpleObserver<BannerData>(refreshLayout_fragment_home) {
                override fun onNext(result: BannerData) {
                    super.onNext(result)
                    initBanner(result.banners)
                }
            })
    }


    private fun initBanner(bannerList: List<BannerBean>) {
        val imageUrlList: MutableList<String> = ArrayList()
        val titleList: MutableList<String> = ArrayList()
        for (bannerBean in bannerList) {
            imageUrlList.add(bannerBean.pic_name)
            titleList.add(bannerBean.title)
        }
        banner_home.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        banner_home.setImageLoader(object : ImageLoader() {
            override fun displayImage(
                context: Context,
                path: Any,
                imageView: ImageView
            ) {
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                Glide.with(context).load(path).into(imageView)
            }
        })
        banner_home.setImages(imageUrlList)
        banner_home.setBannerTitles(titleList)
        banner_home.setOnBannerListener(OnBannerListener { position ->
            if (bannerList[position].book_id > 0) {
                StartActUtil.toBookDetail(mBaseActivity, "${bannerList[position].book_id}")
            }
        })
        banner_home.start()
    }
}
