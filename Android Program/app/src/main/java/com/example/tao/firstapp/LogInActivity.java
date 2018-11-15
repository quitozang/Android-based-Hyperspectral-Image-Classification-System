package com.example.tao.firstapp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{
    public String runAlgorithmAddress = "http://10.10.10.58:8088/MyWeb/runAlgorithm";       //10.10.10.58改成自己的虚拟机的地址    地址查看指令：ifconfig
    public String uploadDataAddress = "http://10.10.10.58:8088/MyWeb/uploadData";
    public TextView t1;
    public TextView t2;
    public Button b1;
    public Button b2;
    public ImageView imageView;
    public ListView listview1;
    public Spinner spinner;
    public ProgressBar progressBar;

    //listView显示数据
    private List<ImageFile> fruitList = new ArrayList<ImageFile>();
    private List<Map<String, Object>> data;

    private MyAdapter myAdapter;

    //spinner填充数据
    private static final String[] m={"2","4","8","16","32"};
    private ArrayAdapter<String> adapter;
    public String splitNum;
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
            if (msg.what == 404) {       //无网络连接
                Toast.makeText(LogInActivity.this, "Can't connect to the server", Toast.LENGTH_SHORT).show();
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.loadfail));
            } else {
                t1.setText(msg.obj.toString());
                imageView.setImageBitmap((Bitmap)msg.obj);///回收msg
            }
        }
    };

    /**
     * 处理上传数据之后的响应信息
     * @param savedInstanceState
     */
    Handler uploadMsgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = "";

            if ("OK".equals(msg.obj.toString())){
                result = "success";
            }else if ("Wrong".equals(msg.obj.toString())){
                result = "fail";
            }else {
                result = msg.obj.toString();
            }
            String returnMsg = msg.obj.toString();
            String tmp = "";
            progressBar.setVisibility(View.INVISIBLE);

            if (returnMsg.length() > 8)
                tmp = returnMsg.substring(0,8);
            if (tmp.equals("java.net")) {
                Toast.makeText(LogInActivity.this, "No Network", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LogInActivity.this, "数据上传成功", Toast.LENGTH_SHORT).show();
//                imageView.setVisibility(View.VISIBLE);
            }
        }
    };
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("img", R.drawable.file2_1);
        map1.put("title", "PaviaU1");
        map1.put("info", "原始Pavia高光谱图像数据,图像文件大小220M");
        list.add(map1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("img", R.drawable.file2_1);
        map2.put("title", "PaviaU2");
        map2.put("info", "将两幅Pavia高光谱图像数据进行水平拼接，拼接之后的文件大小440M");
        list.add(map2);

        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("img", R.drawable.file2_1);
        map3.put("title", "PaviaU3");
        map3.put("info", "将三幅Pavia高光谱图像数据进行水平拼接，拼接之后的文件大小660M");
        list.add(map3);

        return list;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initImageFile();
        initView();

        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
//        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        initEvent();
    }

    private void initImageFile() {
        ImageFile file1 = new ImageFile("PaviaU1", R.drawable.file2_1, "原始Pavia高光谱图像数据,图像文件大小220M");
        fruitList.add(file1);
        ImageFile file2 = new ImageFile("PaviaU2", R.drawable.file2_1, "将两幅Pavia高光谱图像数据进行水平拼接，拼接之后的文件大小440M");
        fruitList.add(file2);
        ImageFile file3 = new ImageFile("PaviaU3", R.drawable.file2_1, "将三幅Pavia高光谱图像数据进行水平拼接，拼接之后的文件大小660M");
        fruitList.add(file3);
//        ImageFile file4 = new ImageFile("PaviaU4", R.drawable.file2, "四十倍Pavia高光谱图像数据，文件大小8800M");
//        fruitList.add(file4);
    }

    private void initView() {
        t1 = findViewById(R.id.textView1);
        b1 = findViewById(R.id.button2);        //执行算法
        b2 = findViewById(R.id.button3);        //上传图像
        listview1 = findViewById(R.id.listView1);
        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.loading);
    }
    private void initEvent() {
        /**
         * listview显示设置
         */
//        ImageFileAdapter imageFileAdapter = new ImageFileAdapter(LogInActivity.this, R.layout.image_item, fruitList);
//        listview1.setAdapter(imageFileAdapter);

        data = getData();
        myAdapter = new MyAdapter(this);
        listview1.setAdapter(myAdapter);
        listview1.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listview1.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                String title = myAdapter.getTitle();
                /** it's wrong!!!!!!!!!!!!!!!!!!**/
                Toast.makeText(getApplicationContext(), "PaviaU" + (arg2+1)+" selected", Toast.LENGTH_SHORT).show();
                myAdapter.setSelectedItem(arg2);
            }
        });
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                //拿到被选择项的值
//                str = (String) spinner.getSelectedItem();
//                //把该值传给 TextView
//                tv.setText(str);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//
//            }
//        });


        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }
    public void login() {
        //构造HashMap
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", "hello");
        params.put("password", "world");
        try {
            //构造完整URL
            String compeletedURL = HttpUtil.getURLWithParams(runAlgorithmAddress, params);

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
    /**
     * App与服务端交互
     * 执行上传数据命令
     */
    public void uploadData() {
        //构造HashMap
        splitNum = (String) spinner.getSelectedItem();
//        String splitNum = "4";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("split", splitNum);
        params.put("password", "world");
        try {
            //构造完整URL
            String compeletedURL = HttpUtil.getURLWithParams(uploadDataAddress, params);

            //发送请求
            HttpUtil.sendHttpRequestForUpLoad(compeletedURL, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Message message = new Message();
                    message.obj = response;
                    uploadMsgHandler.sendMessage(message);
                }

                @Override
                public void onError(Exception e) {
                    Message message = new Message();
                    message.obj = e.toString();
                    uploadMsgHandler.sendMessage(message);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
//                login();
                Intent intent = new Intent();
                intent.setClass(LogInActivity.this, ExecutorActivity.class);
                intent.putExtra("splitData",splitNum);
                startActivity(intent);
                break;
            case R.id.button3:
                progressBar.setVisibility(View.VISIBLE);
                uploadData();
//                progressBar.setVisibility(View.INVISIBLE);
//                t2.setText("data loading");
                break;
        }
    }



    /**
     * 判断目标网络是否连通
     */
    private boolean ping(String ip) {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);
        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet get = new HttpGet(ip);
        HttpResponse response = null;
        try {
            response = client.execute(get);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return false;
        }
        if(response.getStatusLine().getStatusCode()==200){
            return true;
        }
        return false;
    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 从服务器取图片
     *http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 自定义apapter
     */
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater = null;
        public ViewHolder holder;
        private int selectedItem = -1;
        private MyAdapter(Context context) {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }

        public void setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
        }

        public String getTitle() {
            return holder.title.getText().toString();
        }

        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null) {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.image_item, null);
                holder.img = convertView.findViewById(R.id.fruit_img);
                holder.title = convertView.findViewById(R.id.fruit_name);
                holder.info = convertView.findViewById(R.id.fruit_introduction);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.img.setBackgroundResource((Integer)data.get(position).get("img"));
            holder.title.setText((String)data.get(position).get("title"));
            holder.info.setText((String)data.get(position).get("info"));
            if(position == selectedItem) {
//                convertView.setBackgroundColor(Color.BLUE);
                //convertView.setSelected(true);
//                convertView.setBackgroundResource(R.drawable.all_listentry_left_selected);
            } else {
//                convertView.setBackgroundColor(Color.GRAY);
                //convertView.setSelected(false);
//                convertView.setBackgroundResource(R.drawable.lstview);
            }
            return convertView;
        }
    }

    //ViewHolder静态类
    static class ViewHolder {
        public ImageView img;
        public TextView title;
        public TextView info;
    }

}
