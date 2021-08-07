package com.re.ng.uu.comic.http.bean.rv_cell;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.rdc.bms.easy_rv_adapter.base.BaseRvCell;
import com.rdc.bms.easy_rv_adapter.base.BaseRvViewHolder;
import com.re.ng.uu.comic.APP;
import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.config.ItemType;
import com.re.ng.uu.comic.http.bean.ComicBean;
import com.re.ng.uu.comic.util.ScreenUtil;

public class ComicCell extends BaseRvCell<ComicBean> {

    public ComicCell(ComicBean comicBean) {
        super(comicBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_COMIC;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createSimpleHolder(parent, R.layout.cell_comic);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        final ImageView iv = holder.getImageView(R.id.iv_comic_cell_comic);
        Glide.with(holder.getContext())
                .asBitmap()
                .load(mData.getImg_url())
                .apply(new RequestOptions()
                        .error(R.drawable.pic_load_error)
                        .placeholder(R.drawable.pic_load_error))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        float scale = resource.getHeight()*1f/resource.getWidth();
                        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                        int ivWidth = ScreenUtil.getScreenWidth(APP.getInstance());
                        layoutParams.height = (int) (ivWidth * scale);
                        iv.setLayoutParams(layoutParams);
                        iv.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        iv.setImageDrawable(errorDrawable);
                    }
                });
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
