package cn.edu.hrbcu.everywhereapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.util.List;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.adapter.BusAdapter;
import cn.edu.hrbcu.everywhereapp.adapter.BusLocationApapter;
import cn.edu.hrbcu.everywhereapp.entity.BusLocation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CustomerActivity extends AppCompatActivity {
    private final String host = "www.newkownledge.com";
    private ListView listView = null;

    OkHttpClient mClient = new OkHttpClient();

    List<BusLocation> buses ;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            buses = (List<BusLocation>)msg.obj;
            BusLocationApapter busLocationAdapter = new BusLocationApapter(CustomerActivity.this, R.layout.buslocation_item,0, buses);
            listView.setAdapter(busLocationAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        //////////////////////////////////////////////
        listView = findViewById(R.id.bus_listview);
        requestAllBus();
        //1、为列表中选中的项添加单击响应事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                //获得乘客选择的路线名
                BusLocation busLocation= buses.get(i) ;
                Intent intent = new Intent(CustomerActivity.this,MapBusActivity.class);
                intent.putExtra("busname",busLocation.getName());
                startActivity(intent);
                Toast.makeText(CustomerActivity.this,"您选择的路线是：" + busLocation.getName(),Toast.LENGTH_LONG).show();
            }
        });


        listView.setOnTouchListener(new View.OnTouchListener() {
            int yDown,xDown;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 当手指按下时，获取当前的Y坐标
                         yDown = (int) event.getRawY();
                         xDown = (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 当手指离开时，获取当前的Y坐标
                        int yUp = (int) event.getRawY();
                        int xUp = (int) event.getRawX();
                        // 计算手指移动的距离
                        int dy = yUp - yDown;
                        int dx = xUp-xDown;
                        // 如果手指向上滑动（dy>0），则模拟下拉效果
                        int dist=dy*dy+dx*dx;
                        if (dist>10000) {
                            //查询最新的线路信息数据
                            requestAllBus();
                            // 启动刷新动画
//                            Animation animation = AnimationUtils.loadAnimation(CustomerActivity.this, R.animator.refresh_animation);
//                            listView.startAnimation(animation);

                            Toast.makeText(CustomerActivity.this,"onTouch" ,Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return false; // 返回false以允许其他事件继续处理
            }
        });
   //////////////////////////////////////////////////////////////////////////////////////////
    }

    private void requestAllBus(){
        Request request = new Request.Builder()
                .url("http://" + host + "/Bus/queryAllBus")
                .build();//在显示所有的路线,例如工大线、工程线等。

        new Thread(new Runnable() {
            @Override
            public void run() {
                mClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.i("SNOW",responseBody);
                        List<BusLocation> buses = JSON.parseArray(responseBody.toString(), BusLocation.class);
                        Message msg = new Message();
                        msg.obj = buses;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }

}