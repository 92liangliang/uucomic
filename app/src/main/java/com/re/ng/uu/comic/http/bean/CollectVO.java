package com.re.ng.uu.comic.http.bean;

import com.re.ng.uu.comic.base.BaseBookShelfVO;

import java.util.Objects;

public class CollectVO extends BaseBookShelfVO implements Cloneable {
    private BookBean bookBean;
    private int favorId;

    public CollectVO(BookBean bookBean, int favorId) {
        this.bookBean = bookBean;
        this.favorId = favorId;
    }


    @Override
    public CollectVO clone(){
        CollectVO vo = null;
        try {
            vo = (CollectVO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return vo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollectVO collectVO = (CollectVO) o;
        return Objects.equals(bookBean.getBook_id(), collectVO.bookBean.getBook_id());
    }


    @Override
    public int hashCode() {
        return Objects.hash(bookBean.getBook_id());
    }

    public BookBean getBookBean() {
        return bookBean;
    }

    public void setBookBean(BookBean bookBean) {
        this.bookBean = bookBean;
    }

    public int getFavorId() {
        return favorId;
    }

    public void setFavorId(int favorId) {
        this.favorId = favorId;
    }
}
