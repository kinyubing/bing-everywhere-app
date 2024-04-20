package cn.edu.hrbcu.everywhereapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import cn.edu.hrbcu.everywhereapp.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DriverBetweenGps extends AppCompatActivity {
    private String username;
    private String password;
    private String currentBus;
    OkHttpClient okHttpClient = null;
    private final String okInfo="用户名和密码校验成功！";
    private final String failInfo="用户名或密码错误！";
    private final String registerInfo="用户注册成功！";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_between);
        //获取司机输入的用户名和密码以及选择的线路
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        currentBus = getIntent().getStringExtra("currentBus");
        //进行用户名和密码验证
        okHttpClient = new OkHttpClient();
        //先进行本地测试
        Request request = new Request.Builder()
                .url("http://192.168.243.167:8888/Driver/validateDriver?username=" + username + "&password="
                        + password)
                .build();
        //服务器测试
//        Request request = new Request.Builder()
//                .url("http://"+host+"/Driver/validateDriver?username=" + username + "&password="
//                        + password)
//                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("SNOW", "onFailure");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        //ResponseBody responseBody = response.body();
                        Log.e("SNOW", "onResponse");
                        if (!response.isSuccessful()) {

                        }

                        Headers responseHeaders = response.headers();
                        //if(responseBody != null)
                        {
                            //String result = responseBody.toString();
                            String responseBody = response.body().string();
                            Log.i("linyubing", responseBody);
                            //判断校验是否通过
                            if (responseBody.equals(okInfo)||responseBody.equals(registerInfo)) {
                                //显示登录成功的信息
                               // Toast.makeText(DriverBetweenGps.this, responseBody, Toast.LENGTH_SHORT).show();
                                //页面进行跳转到定位界面
                                Intent intent = new Intent(DriverBetweenGps.this, GPSActivity.class);
                                intent.putExtra("currentBus",currentBus);
                                startActivity(intent);
//
                            } else if (responseBody.equals(failInfo)) {
                                //校验失败需要进行页面跳转到登录页面并返回失败的信息
                              //  Toast.makeText(DriverBetweenGps.this, responseBody, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DriverBetweenGps.this, DriverActivity.class);
                                intent.putExtra("failInfo",failInfo);
                                startActivity(intent);
                            }

                        }


                    }
                });
            }
        }).start();
    }
}
