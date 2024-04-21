package cn.edu.hrbcu.everywhereapp.utils;

import android.util.Log;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.edu.hrbcu.everywhereapp.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//定时器任务

public class Scheduler {
    private ScheduledExecutorService scheduler;
    private final String host = "82.156.113.196";
    private String busname;
    private Double longtitude;
    private Double latitude;
    private AMap aMap=null;
    public void startScheduler(String busname,AMap aMap) {
        this.busname=busname;
        this.aMap = aMap;
        //使用线程池来执行定时任务
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
               //运行定时任务
                sendOkHttpRequest();
            }
        }, 0, 10, TimeUnit.SECONDS); // 每10秒钟执行一次
    }

    public void stopScheduler() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
    public void sendOkHttpRequest(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + host + "/Bus/queryBus?busname="+busname)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // 请求失败处理
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 请求成功的处理
                    String responseBody= response.body().string();
                    Log.i("SNOW",responseBody);
                    responseBody = responseBody.replace("[", ",").replace("]", "")
                            .replace(":","");
                    String[] split = responseBody.split(",");
                    //通过设置地图的中心点和缩放级别来显示特定的经纬度位置
                    longtitude=Double.parseDouble(split[1]);
                    latitude=Double.parseDouble(split[2]);
                    //通过设置地图的中心点和缩放级别来显示特定的经纬度位置
                    //设置经纬度
                    LatLng latLng = new LatLng(latitude,longtitude);
                    //通过移动中心点的位置以及缩放级别设置地图
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
                    // 在地图上添加标记
                    // 创建一个MarkerOptions对象，并设置位置和图标
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(latitude, longtitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus))
                            .title(busname)
                            .snippet("我在这里")
                            .anchor(0.5f, 1.0f) // 设置Marker的锚点
                            .zIndex(100) // 设置Marker的层级
                            .draggable(true) // 设置Marker是否可拖动
                            .visible(true);// 设置Marker是否可见

                    // 将Marker添加到地图上
                    Marker marker = aMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                } else {
                    // 服务器返回错误码的处理
                    int code = response.code();
                    // 根据code值处理不同的错误情况
                    Log.i("linyubing", String.valueOf(code));
                }
            }
        });
    }
}