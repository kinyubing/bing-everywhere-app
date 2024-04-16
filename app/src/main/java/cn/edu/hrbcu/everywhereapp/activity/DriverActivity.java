package cn.edu.hrbcu.everywhereapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.util.List;

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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<BusLocation> buses = (List<BusLocation>)msg.obj;
                    BusAdapter spinnerAdapter = new BusAdapter(DriverActivity.this, buses);
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
                                    Toast.makeText(DriverActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
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
        spinner = (Spinner) findViewById(R.id.spinner_buses);
        button_login = findViewById(R.id.button_login);

        initLocation();
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
                            List<BusLocation> buses = JSON.parseArray(responseBody.toString(), BusLocation.class);
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
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    private void initLocation(){
        this.requestPermissions(permissions,0x123);
    }

    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
    private void getLocation(){
        int ret = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        ret = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        !=PackageManager.PERMISSION_GRANTED){
            return ;
        }

        Location location = locationManager.getLastKnownLocation(locationType);
        if(location != null){

        }
        Toast.makeText(DriverActivity.this, "注册监听器", Toast.LENGTH_SHORT).show();
        locationManager.requestLocationUpdates(locationType,10000,0,
                locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            //Toast.makeText(DriverActivity.this, "LocationChanged", Toast.LENGTH_SHORT).show();
            // 收到GPS的数据
            String url = "http://" + host + "/Bus/updateBus" + "?busname="
                    + currentBus + "&longtitude=" + location.getLongitude()
                    + "&latitude=" + location.getLatitude();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Message msg = new Message();
            msg.what = 1;
            msg.obj = request;
            handler.sendMessage(msg);

            //Toast.makeText(DriverActivity.this, url, Toast.LENGTH_SHORT).show();


        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0x123 && grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            locationType = LocationManager.GPS_PROVIDER;
            isGPSEnabled = locationManager.isProviderEnabled(locationType);

            if(!isGPSEnabled){
                enableLocationSettings();
            }
        }
    }

}