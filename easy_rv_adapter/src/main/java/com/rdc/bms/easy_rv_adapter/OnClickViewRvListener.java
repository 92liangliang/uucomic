package com.rdc.bms.easy_rv_adapter;

import android.view.View;

public interface OnClickViewRvListener {
    /**
     * RecyclerView中点击了Item中的View
     * @param view
     * @param view
     */
    void onClick(View view,int position);

     <C> void onClickItem(C data,int position);

}
