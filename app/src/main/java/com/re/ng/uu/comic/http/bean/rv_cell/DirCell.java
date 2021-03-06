package com.re.ng.uu.comic.http.bean.rv_cell;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.config.ItemType;
import com.re.ng.uu.comic.http.bean.ChapterBean;

public class DirCell extends BaseRvCell<ChapterBean> {

    public DirCell(ChapterBean chapterBean) {
        super(chapterBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_DIR;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createSimpleHolder(parent, R.layout.cell_dir);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        ImageView iv = holder.getImageView(R.id.iv_cover_cell_dir);
        TextView tvNum = holder.getTextView(R.id.tv_chapter_num_cell_dir);
        TextView tvName = holder.getTextView(R.id.tv_chapter_name_cell_dir);

//        Glide.with(holder.getContext())
//                .load(mData.getCoverUrl())
//                .into(iv);
//        tvNum.setText(mData.get());
        tvName.setText(mData.getChapter_name());
        holder.itemView.setBackgroundResource(mData.isChecked()?
                R.color.colorRed:android.R.color.transparent);
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
