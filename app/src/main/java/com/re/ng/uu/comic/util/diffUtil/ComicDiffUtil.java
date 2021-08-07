package com.re.ng.uu.comic.util.diffUtil;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.re.ng.uu.comic.http.bean.ComicBean;

import java.util.List;

public class ComicDiffUtil extends DiffUtil.Callback{

    private List<ComicBean> mOldDatas,mNewDatas;

    public ComicDiffUtil(List<ComicBean> mOldDatas, List<ComicBean> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    @Override
    public int getOldListSize() {
        return mOldDatas == null?0:mOldDatas.size();
    }

    @Override
    public int getNewListSize() {
        return mNewDatas==null?0:mNewDatas.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewDatas.get(newItemPosition).getId()
                == mOldDatas.get(oldItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewDatas.get(newItemPosition).getImg_url()
                .equals(mOldDatas.get(oldItemPosition).getImg_url());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
