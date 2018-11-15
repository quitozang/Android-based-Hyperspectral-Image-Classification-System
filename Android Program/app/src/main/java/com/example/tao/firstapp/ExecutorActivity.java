package com.example.tao.firstapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import sun.misc.BASE64Decoder;

public class ExecutorActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String[] m={"SPS-FC"};
    private ArrayAdapter<String> adapter;

    public String splitNum; //LoginActivity 传递过来的分片数据

    public String runAlgorithmAddress = "http://10.10.10.58:8088/MyWeb/runAlgorithm";

    public Button exeButton;
    public Spinner spinner;
    public TextView tv;
    public ProgressBar loading;
    public ImageView imageView;
    public TextView tvAlgorithm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);

        exeButton = findViewById(R.id.exebutton);
        tv = findViewById(R.id.textView5);
        tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        loading = findViewById(R.id.loading);
        spinner = findViewById(R.id.spinner2);
        imageView = findViewById(R.id.imageView);
        tvAlgorithm = findViewById(R.id.textView6);

        Intent intent = getIntent();
        splitNum = intent.getStringExtra("splitData");

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_gallery_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
//        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        tvAlgorithm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(ExecutorActivity.this, AlgorithmActivity.class);
                startActivity(intent);
            }
        });

        initEvent();


    }

    /**
     * 事件触发
     */
    private void initEvent() {
        exeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exebutton:
                loading.setVisibility(View.VISIBLE);
                exec();     //算法执行
                break;
            case R.id.textView6:
                Intent intent = new Intent();
                intent.setClass(ExecutorActivity.this, AlgorithmActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void exec() {
        //构造HashMap
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("split", splitNum);
        params.put("password", "world");
        try {
            //构造完整URL
            String compeletedURL = HttpUtil.getURLWithParams(runAlgorithmAddress, params);
            //判断网络是否可用
//            boolean flag = ping(runAlgorithmAddress);
//            if (!flag) {
//                Toast.makeText(LogInActivity.this, "No Network", Toast.LENGTH_SHORT).show();
//            }
            //发送请求
            HttpUtil.sendHttpRequest(compeletedURL, new HttpCallbackListener() {
                @Override
                public void onFinish(String responseMessage) {
//                    String code = responseMessage.substring(0,3);
                    Bitmap bitmap = GenerateImage(responseMessage);////生成Bitmap
                    Message msg = new Message();
                    msg.obj=bitmap;////传递msg
                    mHandler.sendMessage(msg);

                }

                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.obj = e.toString();
                    message.what=Integer.valueOf(404);
                    mHandler.sendMessage(message);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理执行算法之后的响应信息
     * @param savedInstanceState
     */
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String returnMsg = msg.obj.toString();
            String tmp = returnMsg.substring(0,8);
            loading.setVisibility(View.INVISIBLE);
            if (msg.what == 404) {       //无网络连接
                Toast.makeText(ExecutorActivity.this, "Can't connect to the server", Toast.LENGTH_SHORT).show();
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.loadfail));
            } else {
//                t1.setText(msg.obj.toString());
                imageView.setImageBitmap((Bitmap)msg.obj);///回收msg
            }
        }
    };

    // base64字符串转化成图片
    public static Bitmap GenerateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            InputStream ins = new ByteArrayInputStream(b);
            Bitmap bitmap = new BitmapFactory().decodeStream(ins);
            return bitmap;
        } catch (Exception e) {
            Log.i("imgStr == null,catch",
                    "imgStr == null,catch (Exception e)");
            return setNullBitmapImage();
        }
    }///GenerateImag
    private static Bitmap setNullBitmapImage() {

        String filePath = "/sdcard/1spray/11.jpg";
        File file = new File(filePath);
        Bitmap bitmap = null;
        try {
            InputStream is = new FileInputStream(file);
            bitmap = new BitmapFactory().decodeStream(is);
            Log.i("setNullBitmapImage()", "setNullBitmapImage()");
            // /imageView1.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        return bitmap;
    }
}
