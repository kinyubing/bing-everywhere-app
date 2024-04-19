package cn.edu.hrbcu.everywhereapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.adapter.BusAdapter;
import cn.edu.hrbcu.everywhereapp.entity.BusLocation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DriverActivity extends AppCompatActivity {
    private final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String GPS_LOCATION_NAME = "android.location.LocationManager.GPS_PROVIDER";
    private boolean isGPSEnabled;
    private String locationType;
    OkHttpClient okHttpClient = null;
    private final String host = "82.156.113.196";
    private String currentBus = "工大线";
    private Button button_login = null;
    LocationManager locationManager = null;
    LocationProvider provider = null;
    private Spinner spinner;
    private Double longitude, latitude;
    private String note;
    private List<BusLocation> buses;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<BusLocation> buses = (List<BusLocation>) msg.obj;
                    BusAdapter spinnerAdapter = new BusAdapter(DriverActivity.this, buses);
                    spinner.setAdapter(spinnerAdapter);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        //获取车辆spinner列表框
        spinner = (Spinner) findViewById(R.id.spinner_buses);
        //为spinner选择子项添加监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取司机选择的路线位置
                BusLocation busLocation = buses.get(position);
                //将选择的路线名称赋值给currentBus
                currentBus = busLocation.getName();
                Log.i("driverSelectBusname",currentBus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 如果没有项被选中，这个方法会被调用(m默认选择第一个工大线）
            }
        });
        //获取登录按钮
        button_login = findViewById(R.id.button_login);
        //获取权限
        // initLocation();
        /////////////////////////////////////////////////
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + host + "/Bus/queryAllBus")
                .build();
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
                            Log.i("SNOW", responseBody);
                            buses = JSON.parseArray(responseBody.toString(), BusLocation.class);
                            Message msg = new Message();
                            msg.obj = buses;
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        }).start();

        ////////////////////////////////////////////////////
        //司机单击登录按钮之后进行获取设备最新一次的位置
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }
    //获取权限进行页面跳转
//    private void initLocation(){
//       this.requestPermissions(permissions,0x123);
//    }

    //定位设置
//    private void enableLocationSettings() {
//        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivity(settingsIntent);
//    }

    //获取设备的当前位置
    private void getLocation() {
        //进行页面跳转传递参数
        Intent intent = new Intent(DriverActivity.this,GPSActivity.class);
        intent.putExtra("currentBus",currentBus);
        startActivity(intent);
    }
}