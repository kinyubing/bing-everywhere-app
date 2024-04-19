package cn.edu.hrbcu.everywhereapp.activity;

import android.Manifest;
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

public class DriverTestActivity extends AppCompatActivity {
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
    private Double longitude,latitude;
    private String note;
    private List<BusLocation> buses;



    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<BusLocation> buses = (List<BusLocation>)msg.obj;
                    BusAdapter spinnerAdapter = new BusAdapter(DriverTestActivity.this, buses);
                    spinner.setAdapter(spinnerAdapter);
                    break;
                case 1:
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
                                    Toast.makeText(DriverTestActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_driver);
        //获取车辆spinner列表框
        spinner = (Spinner) findViewById(R.id.spinner_buses);
        //为spinner选择子项添加监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("linyubing",spinner.getSelectedItem().toString());
                BusLocation busLocation = buses.get(position);
                Log.i("linyubingInfo",busLocation.getName());
                // 在这里处理selectedItem，它包含了选中的项的内容
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 如果没有项被选中，这个方法会被调用
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
                        Log.e("SNOW","onFailure");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        //ResponseBody responseBody = response.body();
                        Log.e("SNOW","onResponse");
                        if(!response.isSuccessful()){

                        }

                        Headers responseHeaders = response.headers();
                        //if(responseBody != null)
                        {
                            //String result = responseBody.toString();
                            String responseBody = response.body().string();
                            Log.i("SNOW",responseBody);
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
//        button_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLocation();
//            }
//        });
    }
    //获取权限
//    private void initLocation(){
//        this.requestPermissions(permissions,0x123);
//    }
//
//    //定位设置
//    private void enableLocationSettings() {
//        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        startActivity(settingsIntent);
//    }
    //获取设备的当前位置
  //  private void getLocation(){
//        int ret = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        ret = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//
//        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        !=PackageManager.PERMISSION_GRANTED){
//            return ;
//        }
        // 判断当前是否拥有使用GPS的权限
//        if (ActivityCompat.checkSelfPermission(DriverTestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            // 没有的话就申请权限
//            ActivityCompat.requestPermissions(DriverTestActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 100);
//        }
//        // 获取当前位置管理器
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 启动位置请求
        // LocationManager.GPS_PROVIDER GPS定位
        // LocationManager.NETWORK_PROVIDER 网络定位
        // LocationManager.PASSIVE_PROVIDER 被动接受定位信息
//        locationManager.requestLocationUpdates(locationType,10000,0,
//                locationListener);
//        //从locationManager对象中获取最后一次已知的位置信息
//        Location location = locationManager.getLastKnownLocation(locationType);
//        if(location != null){
//
//        }
//        Toast.makeText(DriverActivity.this, "注册监听器", Toast.LENGTH_SHORT).show();
        //通过locationManager对象请求位置更新，以便实时获取新的位置信息
        //locationManager是一个LocationManager对象，用于管理位置相关的服务和回调
//        locationManager.requestLocationUpdates(locationType,10000,0,
//                locationListener);
   // }

    //locationListener则是一个实现了LocationListener接口的对象，用于接收位置更新的通知
//    private LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            //Toast.makeText(DriverActivity.this, "LocationChanged", Toast.LENGTH_SHORT).show();
//            // 获取当前纬度
//            latitude = location.getLatitude();
//            // 获取当前经度
//            longitude = location.getLongitude();
//            // 定义位置解析
//            Geocoder geocoder = new Geocoder(DriverTestActivity.this, Locale.getDefault());
//            try {
//                // 获取经纬度对于的位置
//                // getFromLocation(纬度, 经度, 最多获取的位置数量)
//                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                // 得到第一个经纬度位置解析信息
//                Address address = addresses.get(0);
//                // 获取到详细的当前位置
//                // Address里面还有很多方法你们可以自行实现去尝试。比如具体省的名称、市的名称...
//                String info = address.getAddressLine(0) + // 获取国家名称
//                        address.getAddressLine(1) + // 获取省市县(区)
//                        address.getAddressLine(2);  // 获取镇号(地址名称)
//                //详细的当前位置
//                note=info;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            // 移除位置管理器
//            // 需要一直获取位置信息可以去掉这个
//            //locationManager.removeUpdates(this);
//            // 收到GPS的数据
//            String url = "http://" + host + "/Bus/updateBus" + "?busname="
//                    + currentBus + "&longtitude=" + location.getLongitude()
//                    + "&latitude=" + location.getLatitude();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//            Message msg = new Message();
//            msg.what = 1;
//            msg.obj = request;
//            handler.sendMessage(msg);
//
//            //Toast.makeText(DriverActivity.this, url, Toast.LENGTH_SHORT).show();
//
//
//        }
//    };
     //获取权限的返回结果
 //   @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 0x123 && grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
//            locationType = LocationManager.GPS_PROVIDER;
//            isGPSEnabled = locationManager.isProviderEnabled(locationType);
//
//            if(!isGPSEnabled){
//                enableLocationSettings();
//            }
//        }
//    }

}