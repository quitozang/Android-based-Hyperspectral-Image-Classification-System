package com.example.tao.firstapp;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class HttpUtil {
    private static final int SO_TIMEOUT = 10 * 60 * 1000; // 设置请求超时时间10秒钟

    /**
     * 建立http连接
     * 传输登录页面的账号和密码信息
     * @param address
     * @param listener
     * @throws InterruptedException
     */
    public static void sendHttpRequestForLogin(final String address, final HttpCallbackListener listener) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    //使用HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //设置方法和参数
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(15000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //获取返回结果
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    //成功则回调onFinish
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //出现异常则回调onError
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        });

        thread.start();
        thread.join();

    }

    /**
     * 上传数据
     * @param address
     * @param listener
     * @throws InterruptedException
     */
    public static void sendHttpRequestForUpLoad(final String address, final HttpCallbackListener listener) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    //使用HttpURLConnection
                    connection = (HttpURLConnection) url.openConnection();
                    //设置方法和参数
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(15000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //获取返回结果
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    //成功则回调onFinish
                    if (listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //出现异常则回调onError
                    if (listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        });

        thread.start();
//        thread.join();

    }
    /**
     * 封装的发送请求函数
     * 返回image
     * @param address
     * @param listener
     */
    //
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {

        if (!HttpUtil.isNetworkAvailable()){
            //这里写相应的网络设置处理
//            Toast.makeText(LogInActivity, "can't connect the net", Toast.LENGTH_SHORT).show();
            return;
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                try{
//                    URL url = new URL(address);
//                    //使用HttpURLConnection
//                    connection = (HttpURLConnection) url.openConnection();
//                    //设置方法和参数
//                    connection.setRequestMethod("POST");
//                    connection.setConnectTimeout(15000);
//                    connection.setReadTimeout(15000);
//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
//                    //获取返回结果
//                    InputStream inputStream = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null){
//                        response.append(line);
//                    }
//                    //成功则回调onFinish
//                    if (listener != null){
//                        listener.onFinish(response.toString());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    //出现异常则回调onError
//                    if (listener != null){
//                        listener.onError(e);
//                    }
//                }finally {
//                    if (connection != null){
//                        connection.disconnect();
//                    }
//                }
//            }
//        }).start();

        /**
         * 接收图片
         */
        new Thread(new Runnable() {////在子线程中传递msg，主线程中接收msg;;要是用fun_HttpPost()的返回值会失败
            @Override
            public void run() {
                // TODO 自动生成的方法存根
                HttpPost request = new HttpPost(address);
                try {
                    HttpClient client = getHttpClient();
                    // 执行请求返回相应
                    HttpResponse response = client.execute(request);
                    // 判断是否请求成功
//                    if (response.getStatusLine().getStatusCode() == 200) {
                        // 获得响应信息
                        String responseMessage = EntityUtils.toString(response.getEntity());
//                        responseMessage = response.getStatusLine().getStatusCode() + responseMessage;
                        if (listener != null){
                            listener.onFinish(responseMessage);
                        }
//                        Bitmap bitmap = GenerateImage(responseMessage);////生成Bitmap
//                        Message msg = new Message();
//                        msg.what=0;
//                        msg.obj=bitmap;////传递msg
//                        handler.sendMessage(msg);

//                    }
                } catch (ConnectTimeoutException e) {
                    listener.onError(e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }// //run()
        }).start();

    }

    // 初始化HttpClient，并设置超时
    public static HttpClient getHttpClient() {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 15000);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpClient client = new DefaultHttpClient(httpParams);
        return client;
    }// /public HttpClient getHttpClient()

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


    //组装出带参数的完整URL
    public static String getURLWithParams(String address,HashMap<String,String> params) throws UnsupportedEncodingException {
        //设置编码
        final String encode = "UTF-8";
        StringBuilder url = new StringBuilder(address);
        url.append("?");
        //将map中的key，value构造进入URL中
        for(Map.Entry<String, String> entry:params.entrySet())
        {
            url.append(entry.getKey()).append("=");
            url.append(URLEncoder.encode(entry.getValue(), encode));
            url.append("&");
        }
        //删掉最后一个&
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    //判断当前网络是否可用
    public static boolean isNetworkAvailable(){
        //这里检查网络，后续再添加
        return true;
    }

    public static String submitPostData(HashMap<String,String> params, String encode) {
        StringBuffer tmpAddress = getRequestData(params, encode);
        byte[] data = tmpAddress.toString().getBytes();
        try {
            URL url = new URL("http://10.10.10.58:8080/MyWeb/A?"+tmpAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);              //设置连接超时时间
            httpURLConnection.setDoInput(true);                     //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                    //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");             //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);                  //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
}
