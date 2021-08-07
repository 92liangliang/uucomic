package com.re.ng.uu.comic.view.fragment.BookShelf;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.re.ng.uu.comic.APP;
import com.re.ng.uu.comic.base.BaseBookShelfTabFragment;
import com.re.ng.uu.comic.http.SimpleObserver;
import com.re.ng.uu.comic.http.UUClient;
import com.re.ng.uu.comic.http.bean.BaseBean;
import com.re.ng.uu.comic.http.bean.CollectVO;
import com.re.ng.uu.comic.http.bean.EventMessage;
import com.re.ng.uu.comic.http.bean.FavorBean;
import com.re.ng.uu.comic.http.bean.FavorList;
import com.re.ng.uu.comic.http.bean.UserInfo;
import com.re.ng.uu.comic.http.bean.rv_cell.CollectCell;
import com.re.ng.uu.comic.util.LogUtil;
import com.re.ng.uu.comic.util.StartActUtil;
import com.re.ng.uu.comic.util.diffUtil.CollectDiffUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class CollectFragment extends BaseBookShelfTabFragment {

    private List<CollectVO> mNewCollectBookList = new ArrayList<>();
    private List<CollectVO> mOldCollectBookList = new ArrayList<>();
    private boolean isStartEdit;

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        isLoaded = true;
        getCollectData();
    }

    @Override
    public void onRecyclerViewInitialized() {
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManger() {
        return new GridLayoutManager(mBaseActivity, 3);
    }

    @Override
    public void onPullRefresh() {
        getCollectData();
    }

    @Override
    public void onLoadMore() {
    }

    public void getCollectData() {
        if(!APP.getInstance().checkLogin(mBaseActivity, true)){
            mRefreshLayout.finishRefresh();
            return;
        }
        UUClient.sub(UUClient.getDefault().bookShelf(APP.getInstance().getUToken()),
                new SimpleObserver<FavorList>(mRefreshLayout){
                    @Override
                    public void onNext(FavorList result) {
                        super.onNext(result);
                        if(result.isSuccess()){
                            onLoadBookShelf(result.getFavors());
                        }
                    }
                });

        UUClient.sub(UUClient.getDefault().history(APP.getInstance().getUToken()),
                new SimpleObserver<BaseBean>(){
                    @Override
                    public void onNext(BaseBean result) {
                        super.onNext(result);
                    }
                });
    }

    private void onLoadBookShelf(List<FavorBean> favorList){
        mOldCollectBookList.clear();
        mOldCollectBookList.addAll(mNewCollectBookList);
        mNewCollectBookList.clear();
        for(FavorBean favorBean: favorList){
            if(favorBean.getBook() != null){
                mNewCollectBookList.add(new CollectVO(favorBean.getBook(), favorBean.getId()));
            }
        }
        setCollectData(mOldCollectBookList, mNewCollectBookList);
    }

    public void setCollectData(List<CollectVO> oldList, List<CollectVO> newList) {
        mBaseAdapter.setDataNoNotify(createCollectCellList(newList));
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CollectDiffUtil(oldList, newList));
        diffResult.dispatchUpdatesTo(mBaseAdapter);
    }

    private List<BaseRvCell> createCollectCellList(List<CollectVO> list) {
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final CollectVO collectVO : list) {
            CollectCell cell = new CollectCell(collectVO);
            cell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toBookDetail(mActivity, collectVO.getBookBean().getBook_id() + "");
                }
            });
            cellList.add(cell);
        }
        return cellList;
    }

    @Override
    public void onStartEdit() {
        addBottomCheckLayout();
        mRefreshLayout.setEnableRefresh(false);
        isStartEdit = true;
        mOldCollectBookList.clear();
        mOldCollectBookList.addAll(mNewCollectBookList);
        ListIterator<CollectVO> listIterator = mNewCollectBookList.listIterator();
        while (listIterator.hasNext()) {
            CollectVO c = listIterator.next().clone();
            c.setStartSelect(true);
            listIterator.set(c);
        }

        setCollectData(mOldCollectBookList, mNewCollectBookList);

    }

    @Override
    public void onEndEdit() {
        removerBottomCheckLayout();
        mRefreshLayout.setEnableRefresh(true);
        isStartEdit = false;
        mOldCollectBookList.clear();
        mOldCollectBookList.addAll(mNewCollectBookList);
        ListIterator<CollectVO> listIterator = mNewCollectBookList.listIterator();
        while (listIterator.hasNext()){
            CollectVO c = listIterator.next().clone();
            c.setStartSelect(false);
            c.setSelect(false);
            listIterator.set(c);
        }
        setCollectData(mOldCollectBookList,mNewCollectBookList);
    }

    @Override
    public void onSelectAll(boolean isSelect) {
        mOldCollectBookList.clear();
        mOldCollectBookList.addAll(mNewCollectBookList);
        ListIterator<CollectVO> listIterator = mNewCollectBookList.listIterator();
        while (listIterator.hasNext()){
            CollectVO c = listIterator.next().clone();
            c.setSelect(isSelect);
            listIterator.set(c);
        }
        setCollectData(mOldCollectBookList,mNewCollectBookList);
    }

    @Override
    public void onDelete() {
        List<CollectVO> temp = new ArrayList<>();
        mOldCollectBookList.clear();
        mOldCollectBookList.addAll(mNewCollectBookList);
        for (CollectVO c : mNewCollectBookList) {
            if (c.isSelect()) {
                temp.add(c);
            }
        }
        mNewCollectBookList.removeAll(temp);
        deleteFromServer(temp);
        setCollectData(mOldCollectBookList,mNewCollectBookList);
    }

    private void deleteFromServer(List<CollectVO> temp){
        String ids = "";
        for(CollectVO data: temp){
            ids += data.getBookBean().getBook_id()+",";
        }
        if(TextUtils.isEmpty(ids)){
            return;
        }
        if(ids.endsWith(",")){
            ids = ids.substring(0, ids.length() - 1);
        }
        UUClient.sub(UUClient.getDefault().delFavors(ids, APP.getInstance().getUToken()),
                new SimpleObserver<BaseBean>(){
                    @Override
                    public void onNext(BaseBean result) {
                        super.onNext(result);
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMessage(EventMessage<UserInfo> event) {
        LogUtil.d("CollectFragment onEventMessage: "+event.getMessageType());
        if (event.getMessageType() == EventMessage.USER_INFO) {
            getCollectData();
        }
    }
}
