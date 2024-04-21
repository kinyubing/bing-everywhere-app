package cn.edu.hrbcu.everywhereapp.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.entity.MyPointOverlay;

/**
 * @author adminZPH
 * 自定义地图Overlay
 * 解决经纬度点地图显示适配问题
 * */
public class OverlayTestActivity extends AppCompatActivity implements AMap.OnMapLoadedListener {
    private MapView mapview;
    private AMap aMap;
    public static final LatLng position_ZHENGZHOU = new LatLng(34.7461110548, 113.6589270503);
    //存放所有站点的经纬度
    private List<LatLng> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_test);
        initmap(savedInstanceState);
    }
    /**
     * 随机生成30个数据,经纬度都在中国的经纬度范围内
     * */
    private void initDate() {
        list=new ArrayList<>();
        for (int i=0;i<30;i++) {
            int x = 73 + (int) (Math.random() * 62);
            int y = 15 + (int) (Math.random() * 38);
            int xx = 100000 + (int) (Math.random() * 100000);
            int yy = 100000 + (int) (Math.random() * 100000);
            list.add(new LatLng(Double.valueOf(x+"."+xx),Double.valueOf(y+"."+yy)));
        }
    }
    //初始化地图
    private void initmap(Bundle savedInstanceState) {
        //获取地图
        mapview= (MapView) findViewById(R.id.overlay_map);
        mapview.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapview.getMap();
            mapview.setSelected(true);
            setMap();
        }
        aMap.setOnMapLoadedListener(this);

    }
    //设置地图的默认位置在郑州
    private void setMap() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
        CameraUpdate movecity = CameraUpdateFactory.newLatLngZoom(position_ZHENGZHOU,17);
        aMap.moveCamera(movecity);
        aMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(), R.drawable.bus))).
                position(position_ZHENGZHOU));
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    /**
     * 当地图加载完后
     * */
    @Override
    public void onMapLoaded() {
        //初始化数据
        initDate();
        List<PoiItem> list1=new ArrayList<>();
        for (int i=0;i<30;++i) {
            LatLonPoint point=new LatLonPoint(list.get(i).latitude,list.get(i).longitude);
            PoiItem p=new PoiItem("1",point,null,null);
            list1.add(p);
        }
        MyPointOverlay myPointOverlay=new MyPointOverlay(aMap,list1);
        myPointOverlay.removeFromMap();
        myPointOverlay.addToMap();
        myPointOverlay.zoomToSpan();
        myPointOverlay.AddLineToAmap();
    }
}
