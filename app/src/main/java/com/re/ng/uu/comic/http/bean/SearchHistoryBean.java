package com.re.ng.uu.comic.http.bean;

import org.litepal.crud.LitePalSupport;

public class SearchHistoryBean extends LitePalSupport {

    private String key;

    public SearchHistoryBean(String key) {
        this.key = key;
    }

    public SearchHistoryBean() {
    }

    public String getKey() {
        return key == null ? "" : key;
    }

    public void setKey(String key) {
        this.key = key == null ? "" : key;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof  SearchHistoryBean){
            if(((SearchHistoryBean) obj).key == null){
                return false;
            }else {
                if(((SearchHistoryBean) obj).key.equals(key)){
                    return true;
                }
            }
        }
        return false;
    }
}
