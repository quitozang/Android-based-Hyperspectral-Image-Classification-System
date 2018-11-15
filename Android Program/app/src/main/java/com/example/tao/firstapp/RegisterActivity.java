package com.example.tao.firstapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    public String addressRegister = "http://10.10.10.58:8088/MyWeb/register";
    boolean flag = false;

    public Button btConfirm;
    public Button btCancel;
    public EditText etAccount;
    public EditText etPasswd;
    public EditText etPasswd2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btConfirm = findViewById(R.id.button);
        btCancel = findViewById(R.id.button4);
        etAccount = findViewById(R.id.account);
        etPasswd = findViewById(R.id.passwd);
        etPasswd2 = findViewById(R.id.passwd2);

        /**
         * 确认注册
         * 连接数据库
         * 在数据库中添加账号、密码信息
         */
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = etAccount.getText().toString().trim();
                final String passwd = etPasswd.getText().toString().trim();
                final String passwd2 = etPasswd2.getText().toString().trim();

                if (!(passwd.equals(passwd2))) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不同，请重新输入！！！", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String StringURL =  addressRegister + "?username=" + username + "&password=" + passwd;
                            try {
//                                URL url = new URL(StringURL);
                                HttpUtil.sendHttpRequestForLogin(StringURL, new HttpCallbackListener() {
                                    @Override
                                    public void onFinish(String response) {
                                        Message message = new Message();
                                        message.obj = response;
                                        mHandler.sendMessage(message);
                                        if (response.equals("no")) {
                                            flag = false;
                                        } else {
                                            flag = true;
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Message message = new Message();
                                        message.obj = e.toString();
                                        message.what=Integer.valueOf(404);
                                        mHandler.sendMessage(message);
                                        flag = false;
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String returnMsg = msg.obj.toString();
            if (msg.what == 404) {       //无网络连接
                Toast.makeText(RegisterActivity.this, "No Network", Toast.LENGTH_SHORT).show();
//            } else if (returnMsg.equals("no")){
//                Toast.makeText(RegisterActivity.this, "username or password error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
