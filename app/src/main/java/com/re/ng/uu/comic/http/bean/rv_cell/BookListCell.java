package com.re.ng.uu.comic.http.bean.rv_cell;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.OnClickViewRvListener;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.rdc.bms.easy_rv_adapter.base.RvSimpleAdapter;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.http.bean.BookBean;
import com.re.ng.uu.comic.util.StartActUtil;

import java.util.ArrayList;
import java.util.List;

public class BookListCell extends BaseRvCell<List<BookBean>> {

    private int mType;
    private String mName;

    public static final int TYPE_TAB_COMIC_LIST_RECOMMEND = 6;
    public static final int TYPE_TAB_COMIC_LIST_OTHER_WORKS = 7;

    public BookListCell(List<BookBean> list, int type, String name) {
        super(list);
        mType = type;
        mName = name;
    }


    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return mType;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_book_list,parent,false));
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, int position) {
        TextView mTvTitle = holder.getTextView(R.id.tv_title_cell_book_list);
        mTvTitle.setText(mName);
        RecyclerView rv = (RecyclerView) holder.getView(R.id.rv_comic_cell_book_list);
        rv.setLayoutManager(new LinearLayoutManager(
                holder.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));
        List<BaseRvCell> cellList = new ArrayList<>();
        for (final BookBean bean : mData) {
            BookCell bookCell = new BookCell(bean);
            bookCell.setListener(new OnClickViewRvListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public <C> void onClickItem(C data, int position) {
                    StartActUtil.toBookDetail(holder.getContext(),bean.getBook_id()+"");
                }

            });
            cellList.add(bookCell);
        }
        RvSimpleAdapter adapter = new RvSimpleAdapter();
        rv.setAdapter(adapter);
        adapter.addAll(cellList);

    }
}
