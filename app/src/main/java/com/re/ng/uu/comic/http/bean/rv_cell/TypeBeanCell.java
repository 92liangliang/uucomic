package com.re.ng.uu.comic.http.bean.rv_cell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.config.ItemType;
import com.re.ng.uu.comic.http.bean.TypeBean;

public class TypeBeanCell extends BaseRvCell<TypeBean> {
    public TypeBeanCell(TypeBean typeBean) {
        super(typeBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_BOOK;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_type,parent,false));
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        holder.getTextView(R.id.tv_name_cell_type).setText(mData.getTag_name());
        Glide.with(holder.getContext())
                .load(mData.getCover_url())
                .into(holder.getImageView(R.id.iv_cover_cell_type));
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
