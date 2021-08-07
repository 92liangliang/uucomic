package com.re.ng.uu.comic.http.bean;

public class EventMessage<T> {
    public static final String TAG = "EventMessage";

    public final static int USER_INFO = 0x100;

    int MessageType;
    T data;

    public EventMessage(int messageType, T data) {
        MessageType = messageType;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public EventMessage(int messageType) {
        MessageType = messageType;
    }

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

}
