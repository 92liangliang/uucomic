package com.re.ng.uu.comic.http.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Date    : 2020-11-02
 */
public class ComicBean {

    @SerializedName("img_url")
    private String img_url;
    @SerializedName("pic_order")
    private String pic_order;
    @SerializedName("update_time")
    private String update_time;
    @SerializedName("create_time")
    private String create_time;
    @SerializedName("chapter_id")
    private int chapter_id;
    @SerializedName("id")
    private int id;

    public boolean isLastPage(){
        return false;
    }

    public boolean isFirstPage(){
        return getIndex() == 1;
    }

    public int getIndex() {
        int index = 0;
        try {
            index = (int)Double.parseDouble(pic_order) + 1;
        }catch (Exception e){}
        return index;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPic_order() {
        return pic_order;
    }

    public void setPic_order(String pic_order) {
        this.pic_order = pic_order;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
