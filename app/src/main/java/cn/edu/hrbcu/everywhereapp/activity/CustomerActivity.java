package cn.edu.hrbcu.everywhereapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private final String host = "82.156.113.196";
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
        /*//3、准备数据
        String[] data={"菠萝","芒果","石榴","葡萄", "苹果", "橙子", "西瓜","菠萝","芒果","石榴","葡萄", "苹果", "橙子", "西瓜","菠萝","芒果","石榴","葡萄", "苹果", "橙子", "西瓜"};
        //4、创建适配器 连接数据源和控件的桥梁
        //参数 1：当前的上下文环境
        //参数 2：当前列表项所加载的布局文件
        //(android.R.layout.simple_list_item_1)这里的布局文件是Android内置的，里面只有一个textview控件用来显示简单的文本内容
        //参数 3：数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<>(CustomerActivity.this,android.R.layout.simple_list_item_1,data);
        //5、将适配器加载到控件中
        listView.setAdapter(adapter);*/
        //6、为列表中选中的项添加单击响应事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                BusLocation busLocation= buses.get(i) ;
                Intent intent = new Intent(CustomerActivity.this,MapBusActivity.class);
                intent.putExtra("busname",busLocation.getName());
                startActivity(intent);
                Toast.makeText(CustomerActivity.this,"您选择的路线是：" + busLocation.getName(),Toast.LENGTH_LONG).show();
            }
        });
        Request request = new Request.Builder()
                .url("http://" + host + "/Bus/queryAllBus")
                .build();

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