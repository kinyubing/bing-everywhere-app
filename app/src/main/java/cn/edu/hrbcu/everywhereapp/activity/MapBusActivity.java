package cn.edu.hrbcu.everywhereapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.adapter.BusAdapter;
import cn.edu.hrbcu.everywhereapp.entity.BusLocation;
import cn.edu.hrbcu.everywhereapp.utils.Scheduler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapBusActivity extends AppCompatActivity {
    private TextView textView = null;
    private OkHttpClient okHttpClient;
    private final String host = "82.156.113.196";
    BusLocation busLocation=null;
    private Double longtitude;
    private Double latitude;
    private String busname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bus);

        //绘制地图
        MapView mapView = (MapView) findViewById(R.id.mapView002);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();
        //////////////////////////////////////////////
        textView = findViewById(R.id.textview_busname);
        String text = textView.getText().toString();
        busname = getIntent().getStringExtra("busname");
        textView.setText(text + busname);
        //启动定时器
        Scheduler scheduler = new Scheduler();
        scheduler.startScheduler(busname,aMap);
        /////////////////////////////////////////////////
//        okHttpClient = new OkHttpClient();
//        // 显示高德地图
//        //1.根据busname查询（通过okhttp后台）司机的经纬度（定时器，每隔10秒发送请求）
//        Request request = new Request.Builder()
//                .url("http://" + host + "/Bus/queryBus?busname="+busname)
//                .build();
//        //采用异步的方式发送请求
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // 请求失败的处理
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    // 请求成功的处理
//                    String responseBody= response.body().string();
//                    Log.i("SNOW",responseBody);
//                    responseBody = responseBody.replace("[", ",").replace("]", "")
//                            .replace(":","");
//                    String[] split = responseBody.split(",");
//                    //通过设置地图的中心点和缩放级别来显示特定的经纬度位置
//                    longtitude=Double.parseDouble(split[1]);
//                    latitude=Double.parseDouble(split[2]);
//                    //通过设置地图的中心点和缩放级别来显示特定的经纬度位置
//                    //设置经纬度
//                    LatLng latLng = new LatLng(latitude,longtitude);
//                    //通过移动中心点的位置以及缩放级别设置地图
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
//                   // 在地图上添加标记
//                    // 创建一个MarkerOptions对象，并设置位置和图标
//                    MarkerOptions markerOptions = new MarkerOptions()
//                            .position(new LatLng(latitude, longtitude))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
//                            .title(busname)
//                            .snippet("我在这里")
//                            .anchor(0.5f, 1.0f) // 设置Marker的锚点
//                            .zIndex(100) // 设置Marker的层级
//                            .draggable(true) // 设置Marker是否可拖动
//                            .visible(true);// 设置Marker是否可见
//
//                    // 将Marker添加到地图上
//                    Marker marker = aMap.addMarker(markerOptions);
//                    marker.showInfoWindow();
//                } else {
//                    // 服务器返回错误码的处理
//                    int code = response.code();
//                    // 根据code值处理不同的错误情况
//                    Log.i("linyubing", String.valueOf(code));
//                }
//            }
//        });
//        Log.i("linyubing","我来了");
        //根据busname查询该线路下的所有站点
//        Request requestSites = new Request.Builder()
//                .url("http://" + host + "/Router/queryRouterByBusname?busname="+busname)
//                .build();
//        //采用异步的方式发送请求
//        okHttpClient.newCall(requestSites).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // 请求失败的处理
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    // 请求成功的处理
//                    String responseBody= response.body().string();
//                    Log.i("getAllSites",responseBody);
//                    //获取到所有的站点后进行下一步操作
//                } else {
//                    // 服务器返回错误码的处理
//                    int code = response.code();
//                    // 根据code值处理不同的错误情况
//                    Log.i("linyubing", String.valueOf(code));
//                }
//            }
//        });


    }
    //销毁定时任务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Scheduler scheduler = new Scheduler();
        scheduler.stopScheduler();
    }
}