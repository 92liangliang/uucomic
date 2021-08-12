package com.re.ng.uu.comic.http.bean.rv_cell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.config.ItemType;
import com.re.ng.uu.comic.http.bean.DownLineBean;

public class DownLineCell extends BaseRvCell<DownLineBean> {

    private boolean isGrid = false;

    public DownLineCell(DownLineBean downLineBean) {
        super(downLineBean);
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
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_down_line, parent, false);
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        TextView tvDate = holder.getTextView(R.id.tv_date);
        TextView tvAmount = holder.getTextView(R.id.tv_amount);
        String reuslt = mData.getCreate_time();
        String d;
        if (reuslt != null) {
            d = reuslt.substring(0, 10);
        } else {
            d = reuslt;
        }
        tvDate.setText(d);
        tvAmount.setText(mData.getMoney() + "元");

    }
}
