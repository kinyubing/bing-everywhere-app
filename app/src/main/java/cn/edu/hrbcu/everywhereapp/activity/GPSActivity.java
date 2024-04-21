package cn.edu.hrbcu.everywhereapp.activity;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

public class GPSActivity extends AppCompatActivity implements LocationListener {


    // 定义TextView
    private TextView nowAddress;
    private TextView lat;
    private TextView lon;
    // 定义双精度类型的经纬度
    private Double longitude,latitude;
    // 定义位置管理器
    private LocationManager locationManager;
    private final String host = "82.156.113.196";
    //private final String localhost = "192.168.243.167";
    private String currentBus = "工大线";
    OkHttpClient okHttpClient = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Request request = (Request)msg.obj;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    String responseBody= response.body().string();
                                    Log.i("linyubingGPS",responseBody);
                                    Toast.makeText(GPSActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsactivity);
        // 得到对应视图ID
        nowAddress = findViewById(R.id.tv_nowAddress);
        lat = findViewById(R.id.tv_latitude);
        lon = findViewById(R.id.tv_longitude);
        //在获取设备权限之前已经进行用户名和密码校验
            //校验通过才进行授权获取位置信息
            if (ActivityCompat.checkSelfPermission(GPSActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(GPSActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
            //获取当前设备的位置信息
            getLocation();
        }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        // 获取当前位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 启动位置请求
        // LocationManager.GPS_PROVIDER GPS定位
        // LocationManager.NETWORK_PROVIDER 网络定位
        // LocationManager.PASSIVE_PROVIDER 被动接受定位信息
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 0, GPSActivity.this);
    }


    // 当位置改变时执行，除了移动设置距离为 0时
    @Override
    public void onLocationChanged(@NonNull Location location) {

        // 获取当前纬度
        latitude = location.getLatitude();
        // 获取当前经度
        longitude = location.getLongitude();
        latitude=Double.parseDouble(String.valueOf(latitude).replace("-",""));
        longitude=Double.parseDouble(String.valueOf(longitude).replace("-",""));
        lat.setText("纬度：" + latitude);
        lon.setText("经度：" + longitude);
        // 定义位置解析
        Geocoder geocoder = new Geocoder(GPSActivity.this, Locale.getDefault());
        try {
            // 获取经纬度对于的位置
            // getFromLocation(纬度, 经度, 最多获取的位置数量)
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // 得到第一个经纬度位置解析信息
            Address address = addresses.get(0);
            // 获取到详细的当前位置
            // Address里面还有很多方法你们可以自行实现去尝试。比如具体省的名称、市的名称...
            String info = address.getAddressLine(0) + // 获取国家名称
                    address.getAddressLine(1) + // 获取省市县(区)
                    address.getAddressLine(2);  // 获取镇号(地址名称)
            // 赋值
            nowAddress.setText(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 移除位置管理器
        // 需要一直获取位置信息可以去掉这个
       // locationManager.removeUpdates(this);
        // 收到GPS的数据(传递currentBus)
        //获取传递过来的busname
        currentBus= getIntent().getStringExtra("currentBus");

        String url = "http://" + host + "/Bus/updateBus" + "?busname="
                + currentBus + "&longtitude=" + longitude
                + "&latitude=" + latitude;

        Request request = new Request.Builder()
                .url(url)
                .build();
        Message msg = new Message();
        msg.what = 1;
        msg.obj = request;
        handler.sendMessage(msg);
    }
    //以下为回调函数
    // 当前定位提供者状态
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("onStatusChanged", provider);
    }

    // 任意定位提供者启动执行
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.e("onProviderEnabled", provider);
    }

    // 任意定位提供者关闭执行
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.e("onProviderDisabled", provider);
    }


}