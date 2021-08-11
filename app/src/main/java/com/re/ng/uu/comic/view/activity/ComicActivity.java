package com.re.ng.uu.comic.view.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter;
import com.rdc.leavesloading.LeavesLoading;
import com.re.ng.uu.comic.APP;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.base.BaseActivity;
import com.re.ng.uu.comic.config.Constant;
import com.re.ng.uu.comic.http.SimpleObserver;
import com.re.ng.uu.comic.http.UUClient;
import com.re.ng.uu.comic.http.bean.BookBean;
import com.re.ng.uu.comic.http.bean.ChapterBean;
import com.re.ng.uu.comic.http.bean.ChapterDetail;
import com.re.ng.uu.comic.http.bean.ComicBean;
import com.re.ng.uu.comic.http.bean.rv_cell.ComicCell;
import com.re.ng.uu.comic.http.bean.rv_cell.DirCell;
import com.re.ng.uu.comic.util.LogUtil;
import com.re.ng.uu.comic.util.diffUtil.ComicDiffUtil;
import com.re.ng.uu.comic.view.dialog.PayDialog;
import com.re.ng.uu.comic.view.widget.PanelManage;
import com.re.ng.uu.comic.view.widget.WrapContentLinearLayoutManager;

import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ComicActivity extends BaseActivity {

    RecyclerView mRvComicContainer;
    ImageView mIvBack;
    TextView mTvTitleTop;
    TextView mTvPageTop;
    TextView mTvTotalPageTop;
    TextView mTvTitleBottom;
    TextView mTvPageBottom;
    TextView mTvTotalPageBottom;
    ConstraintLayout mClTopLayout;
    LinearLayout mLlBottomLayout;
    LinearLayout mLlSetting;
    LinearLayout mLlAuto;
    LinearLayout mLlQuality;
    LinearLayout mLlBrightness;
    LinearLayout mLlDir;
    LinearLayout mLlRightLayout;
    LinearLayout mLlOrder;
    ImageView mIvOrder;
    TextView mTvOrder;
    RecyclerView mRvDir;
    LinearLayout mLlQualitySetting;
    ImageView mIvQualityLow;
    ImageView mIvQualityMiddle;
    ImageView mIvQualityHigh;
    ImageView mIvQualityBottom;
    LeavesLoading mLeavesLoading;
    View mLoadingLayout;

    private RvSimpleAdapter mComicAdapter;
    private RvSimpleAdapter mDirAdapter;
    private String mStartChapterId = "";
    private String mBookId;
    private int mFirstVisibleItemIndex = -1;
    private int mLastVisibleItemIndex = -1;
    private WrapContentLinearLayoutManager mComicLayoutManager;
    private WrapContentLinearLayoutManager mDirLayoutManager;
    private PanelManage mPanelManage;
    private int mDirCheckedIndex = -1;
    private ObjectAnimator mLoadingAnimator;
    private Handler mHandler;
    private ChapterBean mChapterBean;
    private LongSparseArray<ChapterBean> mChapterMap;//存储key-value=chapterId-ChapterBean
    //Adapter中每次只会放总共3章的漫画
    private List<ComicBean> mPreChapterList;//前一章
    private List<ComicBean> mCurChapterList;//当前浏览的章节
    private List<ComicBean> mNextChapterList;//后一张
    private List<ComicBean> mOldComicList;
    private List<ChapterBean> mDirList;
    private int mRvComicFirstVisibleItemIndex = 0;
    private int mImageQuality;

    public static final int QUALITY_LOW = 0;
    public static final int QUALITY_MIDDLE = 1;
    public static final int QUALITY_HIGH = 2;

    public static final int SCROLL_STATE_IDLE = 0;//闲置
    public static final int SCROLL_STATE_RUNNING = 1;//滑动
    public static final int NEED_UPDATE = 2;//需要更新数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic);
        findView();
        initData(savedInstanceState);
        initView();
        initListener();
    }

    private void findView() {
        mRvComicContainer = findViewById(R.id.rv_container_act_comic);
        mIvBack = findViewById(R.id.iv_back_act_comic);
        mTvTitleTop = findViewById(R.id.tv_title_top_act_comic);
        mTvPageTop = findViewById(R.id.tv_page_count_top_act_comic);
        mTvTotalPageTop = findViewById(R.id.tv_page_total_top_act_comic);
        mTvTitleBottom = findViewById(R.id.tv_title_bottom_act_comic);
        mTvPageBottom = findViewById(R.id.tv_page_count_bottom_act_comic);
        mTvTotalPageBottom = findViewById(R.id.tv_page_total_bottom_act_comic);
        mClTopLayout = findViewById(R.id.cl_top_layout_act_comic);
        mLlBottomLayout = findViewById(R.id.ll_bottom_layout_act_comic);
        mLlSetting = findViewById(R.id.ll_setting_act_comic);
        mLlAuto = findViewById(R.id.ll_auto_act_comic);
        mLlQuality = findViewById(R.id.ll_quality_act_comic);
        mLlBrightness = findViewById(R.id.ll_brightness_act_comic);
        mLlDir = findViewById(R.id.ll_dir_act_comic);
        mLlRightLayout = findViewById(R.id.ll_right_layout_act_comic);
        mLlOrder = findViewById(R.id.ll_order_act_comic);
        mIvOrder = findViewById(R.id.iv_order_act_comic);
        mTvOrder = findViewById(R.id.tv_order_act_comic);
        mRvDir = findViewById(R.id.rv_dir_act_comic);
        mLlQualitySetting = findViewById(R.id.ll_quality_setting_layout_act_comic);
        mIvQualityLow = findViewById(R.id.iv_quality_low_ac_comic);
        mIvQualityMiddle = findViewById(R.id.iv_quality_middle_ac_comic);
        mIvQualityHigh = findViewById(R.id.iv_quality_high_ac_comic);
        mIvQualityBottom = findViewById(R.id.iv_quality_bottom_act_comic);
        mLeavesLoading = findViewById(R.id.leavesLoading_loading_layout);
        mLoadingLayout = findViewById(R.id.loadingLayout_act_comic);
    }

    protected void initData(Bundle savedInstanceState) {
        mDirList = new ArrayList<>();
        mOldComicList = new ArrayList<>();
        mChapterMap = new LongSparseArray<>();
        Intent intent = getIntent();
        if (intent != null) {
            mStartChapterId = intent.getStringExtra("chapterId");
            getChapterDetail(mStartChapterId, false, null);
        }
        mPanelManage = new PanelManage(
                mClTopLayout,
                mLlBottomLayout,
                mLlRightLayout,
                mLlQualitySetting);
        mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            boolean isIDLE = true;
            boolean needUpdate = false;
            ChapterBean data = null;

            @Override
            public boolean handleMessage(Message msg) {
                // 使用Handler刷新数据，因为RecyclerView在滑动时或onComputingLayout()计算布局时
                // notifyDataChanged()可能会造成出错
                LogUtil.d(UUClient.TAG, "msg: "+msg.what+" , "+isIDLE+" , "+needUpdate);
                switch (msg.what) {
                    case SCROLL_STATE_IDLE:
                        isIDLE = true;
                        if (needUpdate) {
                            updateComicData(data);
                            needUpdate = false;
                        }
                        break;
                    case SCROLL_STATE_RUNNING:
                        isIDLE = false;
                        break;
                    case NEED_UPDATE:
                        if (!isIDLE) {
                            needUpdate = true;
                            data = (ChapterBean) msg.obj;
                        } else {
                            needUpdate = false;
                            data = null;
                            updateComicData((ChapterBean) msg.obj);
                        }
                        break;
                }
                return false;
            }
        });
    }

    protected void initView() {
        //show loading view
        startLoading();
        mComicLayoutManager = new WrapContentLinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        mRvComicContainer.setLayoutManager(mComicLayoutManager);
        mComicAdapter = new RvSimpleAdapter();
        mRvComicContainer.setAdapter(mComicAdapter);
        mDirLayoutManager = (new WrapContentLinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false));
        mRvDir.setLayoutManager(mDirLayoutManager);
        mDirAdapter = new RvSimpleAdapter();
        mRvDir.setAdapter(mDirAdapter);

        mLlBottomLayout.post(new Runnable() {
            @Override
            public void run() {
                mPanelManage.setBottomLayoutHeight(mLlBottomLayout.getHeight());
            }
        });
        mClTopLayout.post(new Runnable() {
            @Override
            public void run() {
                mPanelManage.setTopLayoutHeight(mClTopLayout.getHeight());
            }
        });
        mLlRightLayout.post(new Runnable() {
            @Override
            public void run() {
                mPanelManage.setRightLayoutWidth(mLlRightLayout.getWidth());
                mPanelManage.hideRight();//初始隐藏
            }
        });
    }

    protected void onDestroy() {
        //保存最近阅读的章节
//        ChapterBean curChapter = mPresenter.getReadingChapter();
//        if (curChapter != null){
//            mPresenter.saveLastRecordChapter(curChapter);
//        }
        super.onDestroy();
    }

    private void getChapterDetail(String chapterId, final boolean menu, final String chapterName) {
        String uToken = APP.getInstance().getUToken();
        UUClient.sub(UUClient.getDefault().chapterDetail(chapterId, uToken), new SimpleObserver<ChapterDetail>() {
            @Override
            public void onNext(ChapterDetail result) {
                super.onNext(result);
                if (result.isSuccess()) {
                    finishedGetComicData(result.getChapters());
                    mChapterBean = result.getChapter();
//                    mOldComicList.addAll(mChapterBean.getPhotos());
                    if (!menu) {
                        mDirList.addAll(result.getChapters());
                    } else {
                        saveReadChapter(mChapterBean);
                    }
                    updateComicData(mChapterBean);
                    mChapterMap.put(mChapterBean.getLongValue(mChapterBean.getChapterId()), mChapterBean);
                } else {
                    if (result.getSuccess() != 0) {
                        showPayDialog(chapterId, result.getMoney(), chapterName);
                    } else {
                        showToast("code " + result.getSuccess() + " 加载失败");
                    }
                }
            }

        });
    }

    private void showPayDialog(String chapterId, String money, String chapterName) {
        String title = chapterName;
        if (TextUtils.isEmpty(title)) {
            title = getIntent().getStringExtra("title");
        }
        final String titleStr = title;
        new PayDialog(this, chapterId, title, money, isSuccess -> {
            if (isSuccess) {
                getChapterDetail(chapterId, false, titleStr);
            }
        })
                .show();
    }

    private void loadNextChapter(ChapterBean data) {
        long nextId = data.getNext();
        if (nextId != 0) {
            String uToken = APP.getInstance().getUToken();
            UUClient.sub(UUClient.getDefault().chapterDetail(nextId + "", uToken), new SimpleObserver<ChapterDetail>() {
                @Override
                public void onNext(ChapterDetail result) {
                    super.onNext(result);
                    if (result.isSuccess()) {
                        ChapterBean next = result.getChapter();
                        mChapterMap.put(next.getLongValue(next.getChapterId()), next);
                    } else {
                        if (result.getSuccess() != 0) {
//                            showToast("code " + result.getSuccess() + " " + result.getMoney());
                        }

                    }
                }
            });
        }
    }

    public ChapterBean getChapterById(long chapterId) {
        return mChapterMap.get(chapterId);
    }


    private ChapterBean lastChapterBean = null;

    public void saveReadChapter(ChapterBean chapterBean) {
        if (chapterBean == null) {
            return;
        }
        Log.d(UUClient.TAG, "saveReadChapter: " + chapterBean.getChapterId()
                + " , " + chapterBean.getChapter_name());
        if (lastChapterBean != null &&
                Long.parseLong(chapterBean.getChapterId()) <= Long.parseLong(lastChapterBean.getChapterId())) {
            return;
        }
        BookBean bookBean = LitePal.where("book_id = ?", chapterBean.getBookId() + "")
                .findFirst(BookBean.class);
        if (bookBean != null) {
            bookBean.setRecentChapter(chapterBean.getChapter_name());
            bookBean.setRecentChapterId(Long.parseLong(chapterBean.getChapterId()));
            bookBean.setLastRecordChapter(chapterBean.getChapter_name());
            bookBean.setLastRecordChapterId(Long.parseLong(chapterBean.getChapterId()));
            bookBean.setTime(new Date());
            bookBean.saveAsync().listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                    Log.d(UUClient.TAG, "saveReadChapter: " + success);
                }
            });
        }
        chapterBean.saveOrUpdateAsync(
                "chapterId = ? and bookId = ?",
                chapterBean.getChapterId() + "",
                chapterBean.getBookId() + "").listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {
                Log.d(UUClient.TAG, "onFinish: " + success);
            }
        });
        lastChapterBean = chapterBean;
    }

    protected void initListener() {
        mRvComicContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        sendMessage(SCROLL_STATE_IDLE, null);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        sendMessage(SCROLL_STATE_RUNNING, null);
                        mPanelManage.hideRight();
                        break;
                    default:
                        sendMessage(SCROLL_STATE_RUNNING, null);
                }
            }

            //往下浏览时dy>0
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //设置阅读提示（阅读到第几章第几页）
                ComicBean curComic = getReadingComic();
                if (curComic != null) {
                    ChapterBean curChapter = getChapterById(curComic.getChapter_id());
                    saveReadChapter(curChapter);
                    setChapterTip(
                            curChapter.getChapter_name(),
                            String.valueOf(curComic.getIndex()),
                            String.valueOf(curChapter.getPageCount()));
                }
                if (dy < -5) {
                    //往上浏览，看前面的章节
                    mPanelManage.showTopAndBottom();
                } else if (dy > 5) {
                    mPanelManage.hideTopAndBottom();
                    mPanelManage.setQualityLayoutVisibility(false);
                }
                int f = mComicLayoutManager.findFirstVisibleItemPosition();
                int l = mComicLayoutManager.findLastVisibleItemPosition();
                if (f == RecyclerView.NO_POSITION
                        || (mFirstVisibleItemIndex == f && mLastVisibleItemIndex == l))
                    return;
                mFirstVisibleItemIndex = f;
                mLastVisibleItemIndex = l;
                onRecyclerViewScroll(dy, mFirstVisibleItemIndex, mLastVisibleItemIndex);
            }
        });
        findViewById(R.id.iv_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanelManage.hideTopAndBottom();
                mPanelManage.showRight();
                indexRvDirFirst();
            }
        });
        mLlDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanelManage.hideTopAndBottom();
                mPanelManage.showRight();
                indexRvDirFirst();
            }
        });
        mLlOrder.setOnClickListener(new View.OnClickListener() {
            boolean order = true;//正序

            @Override
            public void onClick(View v) {
                if (order) {
                    mIvOrder.setImageResource(R.drawable.ic_read_catalog_reverse);
                    mTvOrder.setText("逆序");
                } else {
                    mIvOrder.setImageResource(R.drawable.ic_read_catalog_order);
                    mTvOrder.setText("正序");
                }
                Collections.reverse(mDirList);
                mDirAdapter.setData(createDirCellList(mDirList));
                order = !order;
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(Constant.TIP_NOT_COMPLETE);
            }
        });
        mLlAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(Constant.TIP_NOT_COMPLETE);
            }
        });
        mLlBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(Constant.TIP_NOT_COMPLETE);
            }
        });
        mLlQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPanelManage.setQualityLayoutVisibility(true);
            }
        });
        mIvQualityHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageQuality(QUALITY_HIGH);
                mPanelManage.setQualityLayoutVisibility(false);
                mIvQualityBottom.setImageResource(R.drawable.ic_read_definition_high);
                showToast("已切换到高清画质");
            }
        });
        mIvQualityMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageQuality(QUALITY_MIDDLE);
                mPanelManage.setQualityLayoutVisibility(false);
                mIvQualityBottom.setImageResource(R.drawable.ic_read_definition_middle);
                showToast("已切换到标清画质");
            }
        });
        mIvQualityLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageQuality(QUALITY_LOW);
                mPanelManage.setQualityLayoutVisibility(false);
                mIvQualityBottom.setImageResource(R.drawable.ic_read_definition_low);
                showToast("已切换到流畅画质");
            }
        });
    }

    public void setImageQuality(int quality) {
        if (quality == mImageQuality) {
            return;
        }
        mImageQuality = quality;
        //重新加载漫画
        updateComicData(getReadingChapter());
    }

    public void indexRvDirFirst() {
        if (mOldComicList.size() != 0) {
            ChapterBean chapter = getReadingChapter();
            int index = 0;
            for (int i = 0; i < mDirList.size(); i++) {
                if (mDirList.get(i).getChapterId().equals(chapter.getChapterId())) {
                    index = i;
                    break;
                }
            }
            rvDirScrollTo(index);
            setDirItemSelected(index);
        }
    }

    public ChapterBean getReadingChapter() {
        ComicBean curComic = getReadingComic();
        if (curComic != null) {
            return mChapterMap.get(curComic.getChapter_id());
        }
        return null;
    }

    public void onRecyclerViewScroll(int dy, int firstVisibleItemIndex, int lastVisibleItemIndex) {
        mRvComicFirstVisibleItemIndex = firstVisibleItemIndex;
        ComicBean pre_LastComic = mOldComicList.get(firstVisibleItemIndex);
        ComicBean next_FirstComic = mOldComicList.get(lastVisibleItemIndex);
//        Log.d("ComicPresenter", "firstVisibleItemIndex="+firstVisibleItemIndex);
//        Log.d("ComicPresenter", "dy="+dy+",preListSize!=0:"+(mPreChapterList.size() != 0)+
//                ",pre_LastComic.isLastPage()="+pre_LastComic.isLastPage()+",flag="+pre_LastComic.getBookBeanId().equals(mPreChapterList.get(mPreChapterList.size()-1).getBookBeanId()));
        if (dy < 0 && mPreChapterList.size() != 0
                && pre_LastComic.isLastPage()
                && pre_LastComic.getId() == mPreChapterList.get(mPreChapterList.size() - 1).getId()) {
            ChapterBean preChapter = mChapterMap.get(pre_LastComic.getChapter_id());
            mHandler.sendMessage(mHandler.obtainMessage(NEED_UPDATE, preChapter));
            return;
        } else if (dy < 0 && firstVisibleItemIndex == 0) {
            // firstVisibleItemIndex 随着内容向下滚动（即往上浏览看以前的章节），会逐渐减小为0？
            // 这里没太懂，可能是和我数据刷新的有关
            // 当前第一个可见的Bean
            ComicBean firstVisibleComicBean = mOldComicList.get(firstVisibleItemIndex);
            long preChapterId = mChapterMap.get(firstVisibleComicBean.getChapter_id()).getPrev();
            if (preChapterId != ChapterBean.NO_CHAPTER_ID) {
                ChapterBean preChapter = mChapterMap.get(preChapterId);
                mHandler.sendMessage(mHandler.obtainMessage(NEED_UPDATE, preChapter));
            }
            return;
        }
        if (dy > 0 && mNextChapterList.size() != 0
                && next_FirstComic.isFirstPage()
                && next_FirstComic.getId() == mNextChapterList.get(0).getId()) {
            ChapterBean nextChapter = mChapterMap.get(next_FirstComic.getChapter_id());
            mHandler.sendMessage(mHandler.obtainMessage(NEED_UPDATE, nextChapter));
        } else if (dy > 0 && lastVisibleItemIndex == mOldComicList.size() - 1) {
            //同样，这里的 mLastVisibleItemIndex 会逐渐变为 mComicBeanList.size()-1
            ComicBean lastVisibleComicBean = mOldComicList.get(lastVisibleItemIndex);
            long nextChapterId = mChapterMap.get(lastVisibleComicBean.getChapter_id()).getNext();
            if (nextChapterId != ChapterBean.NO_CHAPTER_ID) {
                mHandler.sendMessage(mHandler.obtainMessage(NEED_UPDATE, mChapterMap.get(nextChapterId)));
            }
        }
    }

    public void updateComicData(ChapterBean data) {
        if (data != null) {
            ChapterBean preChapter = mChapterMap.get(data.getPrev());
            mPreChapterList = (preChapter == null ?
                    new ArrayList<ComicBean>() : toChapterComicList(preChapter));
            ChapterBean nextChapter = mChapterMap.get(data.getNext());
            mNextChapterList = (nextChapter == null ?
                    new ArrayList<ComicBean>() : toChapterComicList(nextChapter));
            mCurChapterList = toChapterComicList(data);
            LogUtil.d("ChapterBean " + data.getChapterId()
                    + " preChapter = " + preChapter
                    + " nextChapter = " + nextChapter
                    + " prevId " + data.getPrev()
                    + " nextId " + data.getNext());

            if (nextChapter == null) {
                loadNextChapter(data);
            }
            List<ComicBean> newComicList = new ArrayList<>();
            newComicList.addAll(mPreChapterList);
            newComicList.addAll(mCurChapterList);
            newComicList.addAll(mNextChapterList);
            updateComic(newComicList, mOldComicList);
            mOldComicList.clear();
            mOldComicList.addAll(newComicList);
        }else {
            getChapterDetail(mChapterBean.getNext()+"", false, "下一话");
        }
    }

    private List<ComicBean> toChapterComicList(ChapterBean chapter) {
        if (chapter == null)
            return new ArrayList<>();
        if (chapter.getPhotos() == null) {
            return new ArrayList<>();
        }
        return chapter.getPhotos();
    }

    public void sendMessage(int what, Object obj) {
        mHandler.sendMessage(mHandler.obtainMessage(what, obj));
    }

    public void finishedGetComicData(List<ChapterBean> chapterList) {
        mDirAdapter.addAll(createDirCellList(chapterList));
        // hide loading view
        endLoading();
    }

    public void onError(String msg) {
        showToast(msg);
    }

    public void setChapterTip(String title, String count, String total) {
        mTvTitleTop.setText(title);
        mTvTotalPageTop.setText(total);
        mTvPageTop.setText(count);
        mTvTitleBottom.setText(title);
        mTvTotalPageBottom.setText(total);
        mTvPageBottom.setText(count);
    }

    public void setDirItemSelected(int index) {
        if(index >= mDirList.size()){
            return;
        }
        if (mDirList.get(index).isChecked()) {
            //已经被选中，直接返回
            return;
        }
        mDirList.get(index).setChecked(true);
        mDirAdapter.update(index, createDirCell(mDirList.get(index)));
        if (mDirCheckedIndex != -1) {
            mDirList.get(mDirCheckedIndex).setChecked(false);
            mDirAdapter.update(mDirCheckedIndex, createDirCell(mDirList.get(mDirCheckedIndex)));
        }
        mDirCheckedIndex = index;
    }

    public void updateComic(List<ComicBean> newComicList, List<ComicBean> oldComicList) {
        mComicAdapter.getData().clear();
        mComicAdapter.getData().addAll(createComicCellList(newComicList));
        //使用DiffUtil高效刷新，刷新时没有白闪
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new ComicDiffUtil(oldComicList, newComicList), true);
        diffResult.dispatchUpdatesTo(mComicAdapter);
    }

    public void rvDirScrollTo(int index) {
        if (index > -1 && index < mDirList.size()) {
            mDirLayoutManager.scrollToPositionWithOffset(index, 0);
        }
    }

    public void rvComicScrollTo(int index) {
        if (index > -1 && index < getComicList().size()) {
            mComicLayoutManager.scrollToPositionWithOffset(index, 0);
        }
    }

    /**
     * 获取当前正在阅读的漫画页
     *
     * @return
     */
    public ComicBean getReadingComic() {
        int curPosition = -1;
        View firstView = mRvComicContainer.getChildAt(0);
        if (firstView.getBottom() > firstView.getHeight() / 2) {
            curPosition = mRvComicContainer.getChildAdapterPosition(firstView);
        } else {
            View secondView = mRvComicContainer.getChildAt(1);
            if (secondView != null) {
                curPosition = mRvComicContainer.getChildAdapterPosition(secondView);
            }
        }
        return curPosition == -1 ? null : getComicList().get(curPosition);
    }

    public List<ComicBean> getComicList() {
        return mOldComicList;
    }


    private void startLoading() {
        mLoadingAnimator = ObjectAnimator
                .ofInt(mLeavesLoading, "progress", 0, 30)
                .setDuration(1000);
        mLoadingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mLoadingAnimator.setAutoCancel(true);
        mLoadingAnimator.start();
    }

    private void endLoading() {
        mLeavesLoading.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLoadingAnimator.isRunning()) {
                    endLoading();
                    return;
                }
                ObjectAnimator animator = ObjectAnimator
                        .ofInt(mLeavesLoading, "progress", 100)
                        .setDuration(2000);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLeavesLoading.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mLoadingLayout.setVisibility(View.GONE);
                            }
                        }, 600);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
            }
        }, 100);
    }

    private List<BaseRvCell> createComicCellList(List<ComicBean> list) {
        List<BaseRvCell> cellList = new ArrayList<>();
        for (ComicBean comicBean : list) {
            ComicCell comicCell = new ComicCell(comicBean);
            comicCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    mPanelManage.hideRight();
                }
            });
            cellList.add(comicCell);
        }
        return cellList;
    }

    private List<BaseRvCell> createDirCellList(final List<ChapterBean> list) {
        List<BaseRvCell> cellList = new ArrayList<>();
        for (ChapterBean chapterBean : list) {
            DirCell dirCell = createDirCell(chapterBean);
            cellList.add(dirCell);
        }
        return cellList;
    }

    private DirCell createDirCell(final ChapterBean chapterBean) {
        DirCell dirCell = new DirCell(chapterBean);
        dirCell.setListener(new OnClickViewRvListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public <C> void onClickItem(C data, int position) {
                setDirItemSelected(position);
                ChapterBean mapValue = mChapterMap.get(chapterBean.getLongValue(chapterBean.getChapterId()));
                LogUtil.d("DirCell onClickItem " + chapterBean.getChapterId() + " " + mapValue);

                if (mapValue != null) {
                    updateComicData(mapValue);
                    saveReadChapter(mapValue);
                    rvComicScrollTo(getCurComicListFirstIndex());
                } else {
//                    updateComicData(chapterBean);
                    startLoading();
                    getChapterDetail(chapterBean.getChapterId(), true, chapterBean.getChapter_name());
                }

            }

        });
        return dirCell;
    }

    public int getCurComicListFirstIndex() {
        return mPreChapterList.size();
    }

}
