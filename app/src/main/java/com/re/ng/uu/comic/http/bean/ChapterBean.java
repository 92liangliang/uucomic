package com.re.ng.uu.comic.http.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Date    : 2020-11-02
 */
public class ChapterBean extends LitePalSupport implements Cloneable {

    @Column(ignore = true)
    public final static int NO_CHAPTER_ID = 0;

    @SerializedName("next")
    private String next;
    @SerializedName("prev")
    private String prev;
    @SerializedName("chapter_order")
    private String chapter_order;
    @SerializedName("book_id")
    @Column(nullable = false)
    private int bookId;
    @SerializedName("chapter_name")
    private String chapter_name;
    @SerializedName("id")
    @Column(nullable = false,unique = true)
    private String chapterId;
    private boolean isRead;//是否阅读过

    @Column(ignore = true)
    @SerializedName("photos")
    private List<ComicBean> photos;

    @Column(ignore = true)
    private boolean isChecked = false;//是否被选中
    @Column(ignore = true)
    private boolean hasRedPoint;

    private boolean isNeedMoney;

    public boolean isNeedMoney() {
        return isNeedMoney;
    }

    public void setNeedMoney(boolean needMoney) {
        isNeedMoney = needMoney;
    }

    public boolean isHasRedPoint() {
        return hasRedPoint;
    }

    public void setHasRedPoint(boolean hasRedPoint) {
        this.hasRedPoint = hasRedPoint;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPageCount() {
        return photos.size();
    }

    public List<ComicBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ComicBean> photos) {
        this.photos = photos;
    }

    public String getChapter_order() {
        return chapter_order;
    }

    public void setChapter_order(String chapter_order) {
        this.chapter_order = chapter_order;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public long getNext() {
        return getLongValue(next);
    }

    public void setNext(String next) {
        this.next = next;
    }

    public long getPrev() {
        return getLongValue(prev);
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public long getLongValue(String index){
        long num = 0;
        try {
            num = Long.parseLong(index);
        }catch (Exception e){}
        return num;
    }
}
