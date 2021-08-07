package com.re.ng.uu.comic.http.bean.rv_cell;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.config.ApiConstant;
import com.re.ng.uu.comic.config.ItemType;
import com.re.ng.uu.comic.http.bean.BookBean;
import com.re.ng.uu.comic.util.LogUtil;
import com.re.ng.uu.comic.util.ScreenUtil;

public class BookCell extends BaseRvCell<BookBean> {

    private boolean isGrid = false;

    public BookCell(BookBean bookBean) {
        super(bookBean);
    }

    @Override
    public void releaseResource() {

    }

    public void setGrid(boolean isGrid) {
        this.isGrid = isGrid;
    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_BOOK;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_book_home, parent, false);
        if (isGrid) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.bottomMargin = ScreenUtil.dip2px(parent.getContext(), 10);
            view.setLayoutParams(layoutParams);
        }
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        TextView tvName = holder.getTextView(R.id.tv_name_cell_book_home);
        TextView tvScore = holder.getTextView(R.id.tv_score_cell_book_home);
        TextView tvRecentWords = holder.getTextView(R.id.tv_recentWords_cell_book_home);
        TextView tvSummary = holder.getTextView(R.id.tv_summary_cell_book_home);
        ImageView ivCover = holder.getImageView(R.id.iv_cover_cell_book_home);

        tvName.setText(mData.getBook_name());
        tvScore.setText("");
//        int index = mData.getRecentChapter().indexOf("话");
//        String chapter = "";
//        if (index != -1){
//            chapter = mData.getRecentChapter().substring(0,index+1);
//        }
        tvRecentWords.setText("");
        tvSummary.setText(mData.getSummary());
        String coverUrl = ApiConstant.getFormatUrl(mData.getCover_url());
        LogUtil.d("image coverUrl=",coverUrl);
        LogUtil.d("image data=",mData.getCover_url());
        LogUtil.d("image name=",mData.getBook_name());
        LogUtil.d("image--------------------------------");
        Glide.with(holder.getContext()).load(coverUrl).into(ivCover);

        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(mData, position);
                }
            });
        }
    }
}
