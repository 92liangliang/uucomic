package com.re.ng.uu.comic.http.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class CollectBean extends LitePalSupport {
    @Column(unique = true)
    private long bookPrimaryKey;
    private Date time;

    public long getBookPrimaryKey() {
        return bookPrimaryKey;
    }

    public void setBookPrimaryKey(long bookPrimaryKey) {
        this.bookPrimaryKey = bookPrimaryKey;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}