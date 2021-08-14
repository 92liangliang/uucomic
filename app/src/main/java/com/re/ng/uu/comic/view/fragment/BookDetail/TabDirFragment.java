package com.re.ng.uu.comic.view.fragment.BookDetail;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.base.BaseLazyFragment;
import com.re.ng.uu.comic.http.SimpleObserver;
import com.re.ng.uu.comic.http.UUClient;
import com.re.ng.uu.comic.http.bean.BookBean;
import com.re.ng.uu.comic.http.bean.ChapterBean;
import com.re.ng.uu.comic.http.bean.ChapterList;
import com.re.ng.uu.comic.http.bean.rv_cell.ChapterNumCell;
import com.re.ng.uu.comic.util.StartActUtil;
import com.re.ng.uu.comic.util.diffUtil.ChapterDiffUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabDirFragment extends BaseLazyFragment {

    TextView mTvUpdateTime;
    ImageView mIvOrder;
    RecyclerView mRvChapter;
    TextView mTvOrder;

    private RvSimpleAdapter mChapterRvAdapter;
    private BookBean mBookBean;
    private boolean isDesc = true;//是否逆序
    private List<ChapterBean> mNewList;
    private List<ChapterBean> mOldList;

    public void setData(BookBean bookBean) {
        mBookBean = bookBean;
        mTvUpdateTime.setText(mBookBean.getUpdate_time());
        getChapterList();
    }

    /**
     * @param newList 默认降序
     */
    public void setChapterList(List<ChapterBean> newList) {
        if (!isDesc) {
            Collections.reverse(newList);
        }
        mOldList = mNewList;
        mNewList = newList;
        updateChapterList(mNewList, mOldList);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_tab_dir;
    }

    @Override
    protected void initView() {
        mTvUpdateTime = mRootView.findViewById(R.id.tv_update_time_fragment_tab_dir);
        mIvOrder = mRootView.findViewById(R.id.iv_order_fragment_tab_dir);
        mRvChapter = mRootView.findViewById(R.id.rv_chapter_fragment_tab_dir);
        mTvOrder = mRootView.findViewById(R.id.tv_order_fragment_tab_dir);
        mChapterRvAdapter = new RvSimpleAdapter();
        mRvChapter.setLayoutManager(new GridLayoutManager(mBaseActivity, 3));
        mRvChapter.setAdapter(mChapterRvAdapter);
        mNewList = new ArrayList<>();
        mOldList = new ArrayList<>();
        setListener();
    }

    protected void setListener() {
        mIvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvOrder.performClick();
            }
        });
        mTvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChapterBean> newList = new ArrayList<>(mNewList);
                Collections.reverse(newList);
                mOldList = mNewList;
                mNewList = newList;
                updateChapterList(mNewList, mOldList);
                mRvChapter.getLayoutManager().scrollToPosition(0);
                if (isDesc) {
                    mTvOrder.setText("升序");
                    mIvOrder.setImageResource(R.drawable.svg_arrow_top);
                } else {
                    mTvOrder.setText("降序");
                    mIvOrder.setImageResource(R.drawable.svg_arrow_bottom);
                }
                isDesc = !isDesc;
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    private void getChapterList() {
        UUClient.sub(UUClient.getDefault().chapterList(mBookBean.getBook_id() + ""), new SimpleObserver<ChapterList>() {
            @Override
            public void onNext(ChapterList result) {
                super.onNext(result);
                if (result.isSuccess()) {
                    int startPay = mBookBean.getStart_pay();
                    if (startPay > 0) {
                        for (int i = startPay - 1; i < result.getChapters().size(); i++) {
                            result.getChapters().get(i).setNeedMoney(true);
                        }
                    }
                    setChapterList(result.getChapters());
                }
            }
        });
    }

    private void updateChapterList(List<ChapterBean> newList, List<ChapterBean> oldList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ChapterDiffUtil(oldList, newList));
        diffResult.dispatchUpdatesTo(mChapterRvAdapter);
        mChapterRvAdapter.getData().clear();
        mChapterRvAdapter.getData().addAll(createChapterCellList(newList));
    }

    private List<BaseRvCell> createChapterCellList(List<ChapterBean> list) {
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final ChapterBean chapter : list) {
            ChapterNumCell chapterCell1 = new ChapterNumCell(chapter);
            chapterCell1.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toComicAct(mBaseActivity, chapter,chapter.getChapterId(),
                            chapter.getChapter_name());
                }


            });
            cellList.add(chapterCell1);
        }
        return cellList;
    }
}
