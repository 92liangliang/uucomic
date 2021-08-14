package com.re.ng.uu.comic.view.activity

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rdc.bms.easy_rv_adapter.fragment.AbsFragment
import com.re.ng.uu.comic.APP
import com.re.ng.uu.comic.R
import com.re.ng.uu.comic.base.BaseActivity
import com.re.ng.uu.comic.base.BaseLazyFragment
import com.re.ng.uu.comic.config.ApiConstant
import com.re.ng.uu.comic.http.SimpleObserver
import com.re.ng.uu.comic.http.UUClient
import com.re.ng.uu.comic.http.bean.*
import com.re.ng.uu.comic.util.AnimateUtil
import com.re.ng.uu.comic.util.ImageUtil
import com.re.ng.uu.comic.util.LogUtil
import com.re.ng.uu.comic.util.StartActUtil
import com.re.ng.uu.comic.view.fragment.BookDetail.TabDetailFragment
import com.re.ng.uu.comic.view.fragment.BookDetail.TabDirFragment
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.layout_top.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal
import java.util.*
import kotlin.collections.ArrayList

class BookDetailActivity : BaseActivity() {

    private lateinit var mTabLayout: TabLayout
    private lateinit var mIvBack: ImageView
    private lateinit var mIvCollect: ImageView
    private lateinit var mTopLayout: View
    private lateinit var mTvTitle: TextView
    private lateinit var mHeaderLayout: ConstraintLayout
    private lateinit var mVpContainer: ViewPager
    private lateinit var mIvCover: ImageView
    private lateinit var mIvTopBg: ImageView
    private lateinit var mTvName: TextView
    private lateinit var mTvAuthor: TextView
    private lateinit var mTagFlowLayout: TagFlowLayout
    private lateinit var mTvPopularity: TextView
    private lateinit var mToolbar: Toolbar
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var mBtnStartRead: Button

    private lateinit var mBookId: String
    private var mBookBean: BookBean? = null
    private var mFList: ArrayList<BaseLazyFragment> = ArrayList()
    private var isCollected = false
    private var isFullView = true
    private val mTitles = arrayOf("详情", "目录", "评论")
    private var mRecentReadChapter: ChapterBean? = null //最近阅读的一话
    private var mFirstChapterBean: ChapterBean? = null //第一话


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        initData()
        initView()
        initListener()
        requestDatas()
    }

    private fun initData() {
        val intent = intent
        mBookId = if (intent.getStringExtra("bookId") == null) {
            ""
        } else {
            intent.getStringExtra("bookId")!!
        }
        if (mBookId != null) {
            mFList.add(TabDetailFragment())
            mFList.add(TabDirFragment())
//            mFList.add(TabCommentFragment())
        }
    }

    private fun initView() {
        mTabLayout = tabLayout_act_book_detail
        mIvBack = iv_back_layout_top
        mIvCollect = iv_right_layout_top
        mTopLayout = layout_top_act_book_detail
        mTvTitle = tv_title_layout_top
        mHeaderLayout = cl_top_layout_act_book_detail
        mVpContainer = vp_container_act_book_detail
        mIvCover = iv_cover_act_book_detail
        mIvTopBg = iv_bg_top_act_book_detail
        mTvName = tv_name_act_book_detail
        mTvAuthor = tv_author_act_book_detail
        mTagFlowLayout = tfl_tag_act_book_detail
        mTvPopularity = tv_popularity_act_book_detail
        mToolbar = toolbar_act_book_detail
        mAppBarLayout = app_bar_layout_act_book_detail
        mCollapsingToolbarLayout = collapsing_layout_act_book_detail
        mBtnStartRead = btn_start_read_act_book_detail

        mTopLayout.setBackgroundResource(R.color.transparent)
        mIvCollect.visibility = View.VISIBLE
        mIvBack.setImageResource(R.drawable.svg_white_nav_bar_back)
        setSupportActionBar(mToolbar)
        mAppBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset <= -mHeaderLayout.height / 2) {
                isFullView = false
                mCollapsingToolbarLayout.title = ""
                setStatusTextColor(true)
                mIvBack.setImageResource(R.mipmap.svg_red_nav_bar_back)
                mIvCollect.setImageResource(if (isCollected) R.drawable.svg_red_pressed_like else R.drawable.svg_red_like_normal)
                mTvTitle.visibility = View.VISIBLE
            } else {
                mCollapsingToolbarLayout.title = ""
                setStatusTextColor(false)
                isFullView = true
                mIvBack.setImageResource(R.drawable.svg_white_nav_bar_back)
                mIvCollect.setImageResource(if (isCollected) R.drawable.svg_red_pressed_like else R.drawable.svg_white_normal_like)
                mTvTitle.visibility = View.INVISIBLE
            }
        })
        mVpContainer.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return mFList.get(position)
            }

            override fun getCount(): Int {
                return mFList.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles.get(position)
            }
        }
        mVpContainer.offscreenPageLimit = 3
        mTabLayout.setupWithViewPager(mVpContainer)
    }

    protected fun initListener() {
        mIvCollect.setOnClickListener {
            if (!APP.getInstance().checkLogin(this)) {
                return@setOnClickListener
            }
            AnimateUtil.likeAnimate(mIvCollect, isCollected, isFullView)
            if (isCollected) {
                showToast("已取消收藏！")
            } else {
                showToast("已收藏！")
            }
            isCollected = !isCollected
            switchFavor()
        }
        mIvBack.setOnClickListener { onBackPressed() }
        mBtnStartRead.setOnClickListener {
            readBook()
        }
        mIvCover.setOnClickListener {
            readBook()
        }
    }

    private fun readBook() {
        if (mBookBean == null) {
            showToast("获取漫画信息失败，请稍后再试")
            return
        }
        if (mRecentReadChapter != null) {
            StartActUtil.toComicAct(
                this@BookDetailActivity, mRecentReadChapter,
                mRecentReadChapter!!.chapterId,
                mRecentReadChapter!!.chapter_name
            )
            addHistory(mRecentReadChapter!!)
        } else if (mFirstChapterBean != null) {
            StartActUtil.toComicAct(
                this@BookDetailActivity, mRecentReadChapter,
                mFirstChapterBean!!.chapterId,
                mRecentReadChapter!!.chapter_name
            )
            addHistory(mFirstChapterBean!!)
        } else {
            showToast("请稍后再试")
        }
    }

    private fun addHistory(chapterBean: ChapterBean) {
        mBookBean?.also {
            mBookBean!!.firstOpenLastChapterId = chapterBean.chapterId.toLong()
            mBookBean!!.recentChapter = chapterBean.chapter_name
            mBookBean!!.recentChapterId = chapterBean.chapterId.toLong()
            mBookBean!!.lastRecordChapter = chapterBean.chapter_name
            mBookBean!!.lastRecordChapterId = chapterBean.chapterId.toLong()
            mBookBean!!.time = Date()
            mBookBean!!.saveAsync().listen {
                readHistory()
            }
        }

    }

    private fun readHistory() {
        LitePal.findAllAsync(BookBean::class.java)
            .listen { list ->
                Log.d(AbsFragment.TAG, "findAll BookBean onFinish: $list")
            }
    }

    private fun requestDatas() {
        getBookDetail()
        hasFavor()
        getComments()
    }

    private fun getBookDetail() {
        showLoadingDialog()
        UUClient.sub(
            UUClient.getDefault().bookDetail(mBookId),
            object : SimpleObserver<BookDetail>(dialog) {
                override fun onNext(result: BookDetail) {
                    super.onNext(result)
                    if (result.isSuccess) {
                        setBookDetailInfo(result.book)
                    }
                }
            })
    }

    private fun getComments() {

    }

    private fun hasFavor() {
        if (!APP.getInstance().isUserLogin) {
            return
        }
        UUClient.sub(UUClient.getDefault().hasFavor(
            mBookId,
            APP.getInstance().uToken
        ), object : SimpleObserver<FavorInfo>() {
            override fun onNext(result: FavorInfo) {
                super.onNext(result)
                if (result.isSuccess) {
                    isCollected = result.isfavor == 1
                    mIvCollect.setImageResource(
                        if (isCollected) R.drawable.svg_red_pressed_like else
                            if (isFullView) R.drawable.svg_white_normal_like else R.drawable.svg_red_like_normal
                    )
                }
            }
        })
    }

    private fun switchFavor() {
        var isfavor = if (isCollected) 1 else 0
        UUClient.sub(UUClient.getDefault().switchFavor(
            mBookId, isfavor,
            APP.getInstance().uToken
        ), object : SimpleObserver<BaseBean>() {
            override fun onNext(result: BaseBean) {
                super.onNext(result)
            }
        })
    }

    private fun setBookDetailInfo(bean: BookBean) {
        this.mBookBean = bean
        var coverUrl = ApiConstant.getFormatUrl(bean.cover_url)
        Glide.with(this)
            .load(coverUrl)
            .into(mIvCover)
        Glide.with(this)
            .load(coverUrl)
            .apply(
                RequestOptions().transform(BlurTransformation(25))
            )
            .into(mIvTopBg)
        ImageUtil.changeBrightness(mIvTopBg, -70)
        mTvAuthor.text = "${bean.author_name}"
        mTvName.text = "${bean.book_name}"
        mTvTitle.text = "${bean.book_name}"
        mTvPopularity.text = ""
        val tagsBackground = intArrayOf(
            R.drawable.bg_hot_key_blue, R.drawable.bg_hot_key_orange,
            R.drawable.bg_hot_key_dark_green, R.drawable.bg_hot_key_purple,
            R.drawable.bg_hot_key_red, R.drawable.bg_hot_key_light_green
        )
        var tagStr = bean.tags
        var tagList = ArrayList<String>()
        if (!TextUtils.isEmpty(tagStr)) {
            tagList.addAll(tagStr.split("|"))
        }
        mTagFlowLayout.adapter = object : TagAdapter<String>(tagList) {
            override fun getView(
                parent: FlowLayout,
                position: Int,
                o: String?
            ): View {
                val textView = LayoutInflater.from(this@BookDetailActivity)
                    .inflate(R.layout.tag_tagflowlayout, parent, false) as TextView
                textView.text = o
                textView.setBackgroundResource(tagsBackground[position % (tagsBackground.size - 1)])
                return textView
            }
        }
        setTagsInfo(bean)
        var chapterOne = ChapterBean()
        chapterOne.chapterId = "${bean.start}"
        setLastRecordChapter(chapterOne, true)

    }

    public fun setLastRecordChapter(chapter: ChapterBean, first: Boolean) {
        var bookBean = LitePal.where("book_id = ?", mBookBean!!.book_id.toString())
            .findFirst(BookBean::class.java)
        if (bookBean != null && bookBean.recentChapterId > mBookBean!!.start) {
            var chapterLast = ChapterBean()
            chapterLast.bookId = bookBean.book_id
            chapterLast.chapterId = "${bookBean.recentChapterId}"
            chapterLast.chapter_name = "${bookBean.recentChapter}"
            mRecentReadChapter = chapterLast
            mBtnStartRead.text = "续看" + bookBean.recentChapter
        } else {
            mRecentReadChapter = chapter
            mFirstChapterBean = chapter
            mBtnStartRead.text = "开始阅读"
        }
    }

    private fun setTagsInfo(bean: BookBean) {
        val tagsBackground = intArrayOf(
            R.drawable.bg_hot_key_blue, R.drawable.bg_hot_key_orange,
            R.drawable.bg_hot_key_dark_green, R.drawable.bg_hot_key_purple,
            R.drawable.bg_hot_key_red, R.drawable.bg_hot_key_light_green
        )
        mTagFlowLayout.adapter = object : TagAdapter<String?>(mTitles) {
            override fun getView(
                parent: FlowLayout,
                position: Int,
                o: String?
            ): View {
                val textView = LayoutInflater.from(this@BookDetailActivity)
                    .inflate(R.layout.tag_tagflowlayout, parent, false) as TextView
                textView.text = o
                textView.setBackgroundResource(tagsBackground[position % (tagsBackground.size - 1)])
                return textView
            }
        }
        (mFList[0] as TabDetailFragment).setData(bean)
        (mFList[1] as TabDirFragment).setData(bean)
//        (mFList[2] as TabCommentFragment).setData(bean)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMessage(event: EventMessage<UserInfo>) {
        LogUtil.d("BookDetailActivity onEventMessage: " + event.messageType)
        if (event.messageType == EventMessage.USER_INFO) {
            hasFavor()
        }
    }

}
