package cn.edu.hrbcu.everywhereapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSON;

import java.io.IOException;
import java.util.List;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.adapter.BusStopsApapter;
import cn.edu.hrbcu.everywhereapp.entity.BusStop;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BusStopActivity extends AppCompatActivity {
    private ListView listView = null;

    private TextView textView = null;

   // OkHttpClient mClient = new OkHttpClient();
    private final String host = "82.156.113.196";
    private String busname;
    private List<BusStop> busStops;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            busStops = (List<BusStop>) msg.obj;
            BusStopsApapter adapter=new BusStopsApapter(BusStopActivity.this,R.layout.bustop_item,0,busStops);
            listView.setAdapter(adapter);

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busstop);
        listView = findViewById(R.id.busstop_listview);

        textView = findViewById(R.id.textview_busname);
        String text = textView.getText().toString();
        busname = getIntent().getStringExtra("busname");
        textView.setText(text + busname);

        busname=getIntent().getStringExtra("busname");
        Log.i("busname",busname);

       // http://82.156.113.196/Router/queryRouterByBusname?busname=工大线
        Request request = new Request.Builder()
                .url("http://"+host+"/Router/queryRouterByBusname?busname="+busname)
                .build();//在显示所有的路线,例如工大线、工程线等。
        Log.i("after","afterRequest");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i("linyubing","onFail");
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String responseBody = response.body().string();
                        Log.i("linyubing",responseBody);
                        busStops = JSON.parseArray(responseBody.toString(), BusStop.class);
                        Message msg = new Message();
                        msg.obj = busStops;
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();

    }
}
