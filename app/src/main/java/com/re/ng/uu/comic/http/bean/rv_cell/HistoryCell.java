package com.re.ng.uu.comic.http.bean.rv_cell;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.base.BaseBookShelfCell;
import com.re.ng.uu.comic.config.ApiConstant;
import com.re.ng.uu.comic.config.ItemType;
import com.re.ng.uu.comic.http.bean.HistoryVO;

public class HistoryCell extends BaseBookShelfCell<HistoryVO> {

    public HistoryCell(HistoryVO h) {
        super(h);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_HISTORY;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createSimpleHolder(parent, R.layout.cell_book_history);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        TextView tvName = holder.getTextView(R.id.tv_name_cell_book_history);
        TextView tvRecord = holder.getTextView(R.id.tv_record_cell_book_history);
        ImageView ivCover = holder.getImageView(R.id.iv_cover_cell_book_history);
        tvName.setText(mData.getBookBean().getBook_name());
        tvRecord.setText("已读："+mData.getBookBean().getRecentChapter());
        Glide.with(holder.getContext()).load(
                ApiConstant.getFormatUrl(mData.getBookBean().getCover_url())
        ).into(ivCover);
        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(mData,position);
                }
            });
        }
    }
}
