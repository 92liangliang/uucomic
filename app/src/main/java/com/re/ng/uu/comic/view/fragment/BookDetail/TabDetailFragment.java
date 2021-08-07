package com.re.ng.uu.comic.view.fragment.BookDetail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.base.BaseLazyFragment;
import com.re.ng.uu.comic.http.SimpleObserver;
import com.re.ng.uu.comic.http.UUClient;
import com.re.ng.uu.comic.http.bean.BookBean;
import com.re.ng.uu.comic.http.bean.RecommendBooks;
import com.re.ng.uu.comic.http.bean.rv_cell.BookInfoCell;
import com.re.ng.uu.comic.http.bean.rv_cell.BookListCell;

import java.util.List;

public class TabDetailFragment extends BaseLazyFragment {

    RecyclerView mRvContainer;
    private BookBean mBookBean;
    private RvSimpleAdapter mAdapter;
    private final String TAG = "TabDetailFragment";

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_tab_detail;
    }

    @Override
    protected void initView() {
        mRvContainer = mRootView.findViewById(R.id.rv_container_fragment_tab_detail);
        mAdapter = new RvSimpleAdapter();
        mRvContainer.setLayoutManager(new LinearLayoutManager(
                mBaseActivity,
                LinearLayoutManager.VERTICAL,
                false));
        mRvContainer.setAdapter(mAdapter);
    }

    public void setData(BookBean bookBean){
        this.mBookBean = bookBean;
        getRecommend();
    }

    @Override
    protected void lazyLoad() { }

    private void getRecommend(){
        UUClient.sub(UUClient.getDefault().recommendBooks(mBookBean.getBook_id()+""), new SimpleObserver<RecommendBooks>(){
            @Override
            public void onNext(RecommendBooks result) {
                super.onNext(result);
                if(result.isSuccess()){
                    isLoaded = true;
                    BookInfoCell bookInfoCell = new BookInfoCell(mBookBean);
                    mAdapter.add(bookInfoCell);
                    addItem(BookListCell.TYPE_TAB_COMIC_LIST_RECOMMEND,"相关推荐",
                            result.getRecommends());
                }
            }
        });

    }

    private void addItem(int type,String name,List<BookBean> list){
        if (list != null && list.size() !=0){
            mAdapter.add(new BookListCell(list,type,name));
        }
    }
}
