package com.re.ng.uu.comic.http.bean.rv_cell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.http.bean.OrderBean;

public class OrderHistoryCell extends BaseRvCell<OrderBean> {

    public OrderHistoryCell(OrderBean data) {
        super(data);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_order_history,parent,false);
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        TextView tv_title = holder.getTextView(R.id.tv_title);
        TextView tv_time = holder.getTextView(R.id.tv_time);
        TextView tv_money = holder.getTextView(R.id.tv_money);

        tv_title.setText(mData.getSummary());
        tv_time.setText(mData.getCreate_time());
        tv_money.setText(mData.getMoney()+"元");
    }
}
