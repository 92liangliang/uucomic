package com.re.ng.uu.comic.view.fragment.BookShelf;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.re.ng.uu.comic.R;
import com.re.ng.uu.comic.base.BaseBookShelfTabFragment;
import com.re.ng.uu.comic.base.BaseLazyFragment;

import java.util.ArrayList;
import java.util.List;

public class BookShelfFragment extends BaseLazyFragment {

    TabLayout mTabLayout;
    ViewPager mVpContainer;
    TextView mTvEdit;

    private List<BaseBookShelfTabFragment> mFList;
    private List<EditStatus> mEditStatusList;
    private int mCurrentPage;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.fragment_bookshelf;
    }

    protected void initData() {
        mFList = new ArrayList<>(2);
        mFList.add(new CollectFragment());
        mFList.add(new HistoryFragment());
        mEditStatusList = new ArrayList<>(2);
        mEditStatusList.add(new EditStatus(false));
        mEditStatusList.add(new EditStatus(false));
    }

    @Override
    protected void initView() {
        mTabLayout = mRootView.findViewById(R.id.tl_top_fragment_bookshelf);
        mVpContainer = mRootView.findViewById(R.id.vp_container_fragment_bookshelf);
        mTvEdit = mRootView.findViewById(R.id.tv_edit_fragment_bookshelf);
        initData();
        setListener();
    }

    @Override
    protected void lazyLoad () {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        mVpContainer.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            String[] titles = new String[]{"收藏", "历史"};

            @Override
            public Fragment getItem(int position) {
                return mFList.get(position);
            }

            @Override
            public int getCount() {
                return mFList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        mVpContainer.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mVpContainer);
        isLoaded = true;
    }

    protected void setListener() {
        mTvEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditStatus editStatus = mEditStatusList.get(mCurrentPage);
                if (!editStatus.isEdit()){
                    mFList.get(mCurrentPage).onStartEdit();
                    mTvEdit.setText("完成");
                }else {
                    mFList.get(mCurrentPage).onEndEdit();
                    mTvEdit.setText("编辑");
                }
                editStatus.setEdit(!editStatus.isEdit());
            }
        });
        mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                mTvEdit.setText(mEditStatusList.get(position).getText());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 编辑管理
     */
    class EditStatus {
        private boolean isEdit;

        public EditStatus(boolean isEdit) {
            this.isEdit = isEdit;
        }

        public String getText() {
            return isEdit?"完成":"编辑";
        }

        public boolean isEdit() {
            return isEdit;
        }

        public void setEdit(boolean edit) {
            isEdit = edit;
        }
    }
}
