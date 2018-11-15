package com.example.tao.firstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText accountTextView;
    EditText passwdTextView;
    Button button1;
    String loginAddress = "http://10.10.10.58:8088/MyWeb/login";
    boolean flag = true;                //判断是否登陆成功

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountTextView = findViewById(R.id.account);
        passwdTextView = findViewById(R.id.passwd);

        button1 = findViewById(R.id.logIn);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<String, String>();
                String name = accountTextView.getText().toString();
                String passwd = passwdTextView.getText().toString();
                params.put("username",name);
                params.put("password",passwd);

                login(params);          //执行登录操作

                if (flag) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LogInActivity.class);
                    startActivity(intent);
                } else {
                    System.out.println(" ");
                }
            }
        });

        TextView register = findViewById(R.id.newAccount);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//        TextView changePasswd = findViewById(R.id.modifyPasswd);
//        changePasswd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, SetPasswordDialogFragment.class);
//                startActivity(intent);
//            }
//        });
    }

    public void login(HashMap params) {
        try {
            String compeletedURL = HttpUtil.getURLWithParams(loginAddress, params);//构造完整URL
            HttpUtil.sendHttpRequestForLogin(compeletedURL, new HttpCallbackListener() {
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

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String returnMsg = msg.obj.toString();
            if (msg.what == 404) {       //无网络连接
                Toast.makeText(MainActivity.this, "No Network", Toast.LENGTH_SHORT).show();
            } else if (returnMsg.equals("no")){
                Toast.makeText(MainActivity.this, "username or password error", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(MainActivity.this, "yes", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
