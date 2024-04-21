package cn.edu.hrbcu.everywhereapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.entity.BusLocation;
import cn.edu.hrbcu.everywhereapp.utils.Scheduler;
import okhttp3.OkHttpClient;

public class MapBusActivity extends AppCompatActivity {
    private TextView textView = null;
    private OkHttpClient okHttpClient;
    private final String host = "82.156.113.196";
    BusLocation busLocation=null;
    private Double longtitude;
    private Double latitude;
    private String busname;
    OkHttpClient mClient = new OkHttpClient();
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bus);
        busname= getIntent().getStringExtra("busname");
        //根据busname查询该路线下的所有站点信息
      //  http://82.156.113.196/Router/queryRouterByBusname?busname=工大线

        //绘制地图
        MapView mapView = (MapView) findViewById(R.id.mapView002);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();
        //////////////////////////////////////////////
        textView = findViewById(R.id.textview_busname);
        String text = textView.getText().toString();
        busname = getIntent().getStringExtra("busname");
        textView.setText(text + busname);
        button = findViewById(R.id.button_showBusstop);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MapBusActivity.this,BusStopActivity.class);
                intent.putExtra("busname",busname);
                startActivity(intent);
                Log.i("linyubing","onclick");
            }
        });
        //启动定时器
        Scheduler scheduler = new Scheduler();
        scheduler.startScheduler(busname,aMap);


    }
    //销毁定时任务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Scheduler scheduler = new Scheduler();
        scheduler.stopScheduler();
    }
}