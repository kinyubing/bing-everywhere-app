package cn.edu.hrbcu.everywhereapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;

import java.lang.reflect.Array;

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
    private Spinner zoomSpinner;
    public String currentZoom="10";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bus);

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
        busname= getIntent().getStringExtra("busname");
        //获取缩放级别spinner
//        zoomSpinner=findViewById(R.id.spinner_school);
//        // 创建一个 ArrayAdapter 适配器，并将数据与 Spinner 绑定
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.zooms_array, android.R.layout.simple_spinner_item);
//
//        // 设置下拉菜单的样式
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // 将适配器添加到 Spinner 上
//        zoomSpinner.setAdapter(adapter);
//        //获取缩放级别数组
//        String[] zoomsArray = getResources().getStringArray(R.array.zooms_array);
//        //为spinner选择子项创建监听器
//        zoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //获取乘客选择的缩放级别
//                currentZoom = zoomsArray[position];
//                Log.i("driverSelectZoom",currentZoom);
//                scheduler.startScheduler(busname,aMap,currentZoom);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // 如果没有项被选中，这个方法会被调用(默认选择第一个缩放级别为10）
//            }
//        });
        //启动定时器
        Scheduler scheduler = new Scheduler();
        scheduler.startScheduler(busname,aMap);
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



    }
    //销毁定时任务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Scheduler scheduler = new Scheduler();
        scheduler.stopScheduler();
    }
}