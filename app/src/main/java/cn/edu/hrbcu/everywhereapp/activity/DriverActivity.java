package cn.edu.hrbcu.everywhereapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Spinner;

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
    OkHttpClient okHttpClient = null;

    private final String host = "82.156.113.196";

    private Spinner spinner;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<BusLocation> buses = (List<BusLocation>)msg.obj;
            BusAdapter spinnerAdapter = new BusAdapter(DriverActivity.this, buses);
            spinner.setAdapter(spinnerAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        spinner = (Spinner) findViewById(R.id.spinner_buses);
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
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        }).start();
    }
}