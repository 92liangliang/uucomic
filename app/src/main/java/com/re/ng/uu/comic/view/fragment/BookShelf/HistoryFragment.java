package com.re.ng.uu.comic.view.fragment.BookShelf;


import android.support.annotation.MainThread;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.re.ng.uu.comic.base.BaseBookShelfTabFragment;
import com.re.ng.uu.comic.http.bean.BookBean;
import com.re.ng.uu.comic.http.bean.HistoryBean;
import com.re.ng.uu.comic.http.bean.HistoryVO;
import com.re.ng.uu.comic.http.bean.PageDTO;
import com.re.ng.uu.comic.http.bean.rv_cell.HistoryCell;
import com.re.ng.uu.comic.util.LogUtil;
import com.re.ng.uu.comic.util.StartActUtil;
import com.re.ng.uu.comic.util.diffUtil.HistoryDiffUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.CountCallback;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class HistoryFragment extends BaseBookShelfTabFragment {

    private OnClickViewRvListener mOnHistoryItemListener;
    private List<HistoryVO> mNewBookList = new ArrayList<>();
    private List<HistoryVO> mOldBookList = new ArrayList<>();
    private PageDTO mPage;
    private int mTotalPage;
    private final int PAGE_SIZE = 100;
    private boolean isStartEdit;

    private List<BaseRvCell> createHistoryCellList(List<HistoryVO> list) {
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final HistoryVO vo : list) {
            BaseRvCell cell = new HistoryCell(vo);
            cell.setListener(mOnHistoryItemListener);
            cellList.add(cell);
        }
        return cellList;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        isLoaded = true;
        getHistoryData();
    }

    @Override
    public void onRecyclerViewInitialized() {
        mOnHistoryItemListener = new OnClickViewRvListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public <C> void onClickItem(C data, int position) {
                StartActUtil.toBookDetail(mActivity, ((HistoryVO) data).getBookBean().getBook_id() + "");
            }
        };
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManger() {
        return new GridLayoutManager(mBaseActivity, 3);
    }

    @Override
    public void onPullRefresh() {
        mRefreshLayout.setEnableLoadMore(true);
//        refresh();
        getHistoryData();
    }

    @Override
    public void onLoadMore() {
        getHistoryData();
    }

    public void setHistoryData(List<HistoryVO> oldList, List<HistoryVO> newList) {
        Log.d("LYT", "setHistoryData: oldSize=" + oldList.size() + ",newSize=" + newList.size());
        mRefreshLayout.finishLoadMore();
        mRefreshLayout.finishRefresh();
        mBaseAdapter.setDataNoNotify(createHistoryCellList(newList));
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new HistoryDiffUtil(oldList, newList));
        diffResult.dispatchUpdatesTo(mBaseAdapter);
    }

    public void onError(String msg) {
        showToast(msg);
    }

    public void noMore() {
        if(mRefreshLayout.isRefreshing()){
            mRefreshLayout.finishRefresh();
        }
        mRefreshLayout.setEnableLoadMore(false);
        showToast("没有更多数据了！");
    }

    @Override
    public void onStartEdit() {
        addBottomCheckLayout();
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadMore(false);
        startEdit();
    }

    @Override
    public void onEndEdit() {
        removerBottomCheckLayout();
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadMore(true);
        endEdit();
    }

    @Override
    public void onSelectAll(boolean isSelect) {
        selectAll(isSelect);
    }

    @Override
    public void onDelete() {
        deleteBooks();
    }

    public void startEdit() {
        isStartEdit = true;
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        ListIterator<HistoryVO> listIterator = mNewBookList.listIterator();
        while (listIterator.hasNext()){
            HistoryVO c = listIterator.next().clone();
            c.setStartSelect(true);
            listIterator.set(c);
        }
        setHistoryData(mOldBookList, mNewBookList);
    }

    public void endEdit() {
        isStartEdit = false;
        refresh();
    }

    public void deleteBooks() {
        List<HistoryVO> temp = new ArrayList<>();
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        for (HistoryVO c : mNewBookList) {
            if (c.isSelect()) {
                temp.add(c);
                c.getBookBean().deleteAsync().listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {

                    }
                });
            }
        }
        mNewBookList.removeAll(temp);
        setHistoryData(mOldBookList, mNewBookList);
    }

    public void selectAll(boolean isSelect) {
        mOldBookList.clear();
        mOldBookList.addAll(mNewBookList);
        ListIterator<HistoryVO> listIterator = mNewBookList.listIterator();
        while (listIterator.hasNext()){
            HistoryVO c = listIterator.next().clone();
            c.setSelect(isSelect);
            listIterator.set(c);
        }
        setHistoryData(mOldBookList, mNewBookList);
    }

    @MainThread
    public void refresh() {
        LitePal.countAsync(HistoryBean.class)
                .listen(new CountCallback() {
                    @Override
                    public void onFinish(int count) {
                        mTotalPage = count / PAGE_SIZE;
                        if (mTotalPage * PAGE_SIZE < count) {
                            mTotalPage++;
                        }
                        mPage = new PageDTO(mTotalPage, 0);
                        getHistoryData();
                    }
                });

        LitePal.findAllAsync(BookBean.class).listen(new FindMultiCallback<BookBean>() {
            @Override
            public void onFinish(List<BookBean> list) {
                Log.d(TAG, "findAll BookBean onFinish: "+list.size());
            }
        });
    }

    public void getHistoryData(){
        LitePal.limit(PAGE_SIZE)
                .order("time desc")
                .findAsync(BookBean.class)
                .listen(new FindMultiCallback<BookBean>() {
                    @Override
                    public void onFinish(List<BookBean> list) {
                        LogUtil.d("getHistoryData: " + list.toString());
                        mOldBookList.clear();
                        mOldBookList.addAll(mNewBookList);
//                        if (mPage.getCurrentPage() == 0) {
//                            //第一页，刷新
//                            mNewBookList = new ArrayList<>();
//                        }
                        for (BookBean bookBean : list) {
                            if(!mNewBookList.contains(new HistoryVO(bookBean))){
                                mNewBookList.add(new HistoryVO(bookBean));
                            }
                        }
//                        mPage.setCurrentPage(mPage.getCurrentPage() + 1);
                        setHistoryData(mOldBookList, mNewBookList);
                        if (list.size() < PAGE_SIZE) {
                            noMore();
                        }
                        if (isStartEdit) {
                            //此时正在编辑
                            startEdit();
                        }
                    }
                });
    }

//    public void getHistoryData() {
//        if (mPage.hasMore()) {
//            // 删除数据时只会删除HistoryBean表中的数据而不会删除BookBean表中的数据
//            // 不然在删除历史记录时把可能会把收藏的漫画删除导致收藏丢失
//
//            // 1.先向HistoryBean表查询历史漫画的ID
//            LitePal.limit(PAGE_SIZE)
//                    .offset(PAGE_SIZE * mPage.getCurrentPage())
//                    .order("time desc")
//                    .findAsync(HistoryBean.class)
//                    .listen(new FindMultiCallback<HistoryBean>() {
//                        @Override
//                        public void onFinish(List<HistoryBean> list) {
//                            long[] bookPrimaryKeys = new long[list.size()];
//                            for (int i = 0; i < list.size(); i++) {
//                                bookPrimaryKeys[i] = list.get(i).getBookPrimaryKey();
//                                Log.d(TAG, "3. onFinish: bookPrimaryKeys" + bookPrimaryKeys[i]);
//                            }
//                            Log.d(TAG, "1. onFinish: " + Arrays.toString(bookPrimaryKeys));
//                            if (bookPrimaryKeys.length > 0) {
//                                //2.再向BookBean表查询详细数据
//                                LitePal.findAllAsync(BookBean.class, bookPrimaryKeys)
//                                        .listen(new FindMultiCallback<BookBean>() {
//                                            @Override
//                                            public void onFinish(List<BookBean> list) {
//                                                Log.d(TAG, "2. onFinish: " + list.toString());
//                                                mOldBookList.clear();
//                                                mOldBookList.addAll(mNewBookList);
//                                                if (mPage.getCurrentPage() == 0) {
//                                                    //第一页，刷新
//                                                    mNewBookList = new ArrayList<>();
//                                                }
//                                                for (BookBean bookBean : list) {
//                                                    mNewBookList.add(new HistoryVO(bookBean));
//                                                }
//                                                mPage.setCurrentPage(mPage.getCurrentPage() + 1);
//                                                setHistoryData(mOldBookList, mNewBookList);
//                                                if (list.size() < PAGE_SIZE) {
//                                                    noMore();
//                                                }
//                                                if (isStartEdit) {
//                                                    //此时正在编辑
//                                                    startEdit();
//                                                }
//                                            }
//                                        });
//                            }
//                            if(mRefreshLayout.isRefreshing()){
//                                mRefreshLayout.finishRefresh();
//                            }
//                        }
//                    });
//        } else {
//            noMore();
//        }
//
//    }
}
