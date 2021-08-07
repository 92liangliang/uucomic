package com.re.ng.uu.comic.http.bean.rv_cell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.config.ItemType;
import com.re.ng.uu.comic.http.bean.ChapterBean;

/**
 * 单纯只有话数
 */
public class ChapterNumCell extends BaseRvCell<ChapterBean> {

    public ChapterNumCell(ChapterBean chapter) {
        super(chapter);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_CHAPTER;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_chapter_num,parent,false));
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, final int position) {
        TextView tv = holder.getTextView(R.id.tv_num_cell_chapter_num);
        ImageView ivRead = holder.getImageView(R.id.iv_is_read_cell_chapter_num);
        ImageView ivUnRead = holder.getImageView(R.id.iv_un_read_cell_chapter_num);
        ImageView iv_need_money = holder.getImageView(R.id.iv_need_money);
        ivUnRead.setVisibility(mData.isHasRedPoint() && !mData.isRead()?View.VISIBLE:View.GONE);
        ivRead.setVisibility(mData.isRead()?View.VISIBLE:View.GONE);
        iv_need_money.setVisibility(mData.isNeedMoney()?View.VISIBLE:View.GONE);
        tv.setText(mData.getChapter_name());
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
