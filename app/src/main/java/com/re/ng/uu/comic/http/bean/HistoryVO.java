package com.re.ng.uu.comic.http.bean;

import com.re.ng.uu.comic.base.BaseBookShelfVO;

import java.util.Objects;

public class HistoryVO extends BaseBookShelfVO implements Cloneable {
    private BookBean bookBean;
    public HistoryVO(BookBean bookBean) {
        this.bookBean = bookBean;
    }


    @Override
    public HistoryVO clone(){
        HistoryVO vo = null;
        try {
            vo = (HistoryVO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return vo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryVO historyVO = (HistoryVO) o;
        return bookBean.getBook_id() == historyVO.bookBean.getBook_id();
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
}