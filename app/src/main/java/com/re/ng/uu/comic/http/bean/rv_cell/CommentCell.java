package com.re.ng.uu.comic.http.bean.rv_cell;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.http.bean.CommentBean;

public class CommentCell extends BaseRvCell<CommentBean> {

    public CommentCell(CommentBean list) {
        super(list);
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
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, int position) {
        TextView tv_username = holder.getTextView(R.id.tv_username);
        TextView tv_date = holder.getTextView(R.id.tv_date);
        TextView tv_content = holder.getTextView(R.id.tv_content);

        if (TextUtils.isEmpty(mData.getUser().getNick_name())) {
            tv_username.setText(mData.getUser().getUsername() + "");
        } else {
            tv_username.setText(mData.getUser().getNick_name() + "");
        }
        tv_date.setText(mData.getCreate_time());
        tv_content.setText(mData.getContent());

    }
}
