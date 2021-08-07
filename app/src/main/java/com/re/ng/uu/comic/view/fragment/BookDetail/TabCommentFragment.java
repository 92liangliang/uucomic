package com.re.ng.uu.comic.view.fragment.BookDetail;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter;
import com.re.ng.uu.comic.APP;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.base.BaseLazyFragment;
import com.re.ng.uu.comic.http.SimpleObserver;
import com.re.ng.uu.comic.http.UUClient;
import com.re.ng.uu.comic.http.bean.BookBean;
import com.re.ng.uu.comic.http.bean.CommentBean;
import com.re.ng.uu.comic.http.bean.CommentData;
import com.re.ng.uu.comic.http.bean.rv_cell.CommentCell;
import com.re.ng.uu.comic.view.dialog.CommentInputDialog;

import java.util.List;

public class TabCommentFragment extends BaseLazyFragment {

    RecyclerView mRvContainer;
    private BookBean mBookBean;
    private RvSimpleAdapter mAdapter;
    private LinearLayout ll_input_comment;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_tab_comment;
    }

    @Override
    protected void initView() {
        mRvContainer = mRootView.findViewById(R.id.rv_container_fragment_tab_comment);
        ll_input_comment = mRootView.findViewById(R.id.ll_input_comment);
        mAdapter = new RvSimpleAdapter();
        mRvContainer.setLayoutManager(new LinearLayoutManager(
                mBaseActivity,
                LinearLayoutManager.VERTICAL,
                false));
        mRvContainer.setAdapter(mAdapter);
        ll_input_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(APP.getInstance().checkLogin(getContext())){
                    new CommentInputDialog(getContext(), mBookBean.getBook_id()+"")
                            .show();
                }
            }
        });
    }

    public void setData(BookBean bookBean) {
        this.mBookBean = bookBean;
        getComment();
    }

    @Override
    protected void lazyLoad() {
    }

    private void getComment() {
        UUClient.sub(UUClient.getDefault().comments(mBookBean.getBook_id() + ""),
                new SimpleObserver<CommentData>() {
                    @Override
                    public void onNext(CommentData result) {
                        super.onNext(result);
                        if (result.isSuccess()) {
                            isLoaded = true;
                            addItem(result.getComments());
                        }
                    }
                });

    }

    private void addItem(List<CommentBean> list) {
        if (list != null && list.size() != 0) {
            for (CommentBean data : list) {
                mAdapter.add(new CommentCell(data));
            }
        }
    }
}
