package com.example.tao.firstapp;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
