package cn.edu.hrbcu.everywhereapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Random;

import cn.edu.hrbcu.everywhereapp.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestOKHTTPActivity extends AppCompatActivity {
    private OkHttpClient okHttpClient = null;
    private final String host = "82.156.113.196";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_okhttpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        okHttpClient = new OkHttpClient();

        Button button = findViewById(R.id.send_button_URL);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                Double lng = random.nextDouble();
                Double lat = random.nextDouble();

                String url = "http://" + host + "/Bus/updateBus" + "?busname="
                        + "工大线" + "&longtitude=" + lng
                        + "&latitude=" + lat;

                Request request = new Request.Builder()
                        .url(url)
                        .build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.i("fail","fail");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                //若是数据更新成功之后需要重新回到定位界面
                                String responseBody= response.body().string();
                                Log.i("GPSSendToEnd",responseBody);
                                if(responseBody.equals("OK")){
//                            Intent intent = new Intent(GPSActivity.this, GPSActivity.class);
//                            intent.putExtra("updateGpsOK",updateGpsOK);
//                            intent.putExtra("longitude",longitude);
//                            intent.putExtra("latitude",latitude);
//                            intent.putExtra("info",info);
//                            startActivity(intent);
                                    //Toast.makeText(TestOKHTTPActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                }else if(responseBody.equals("NoBus Or Fail")){
//                            Intent intent = new Intent(GPSActivity.this, GPSActivity.class);
//                            intent.putExtra("updateGpsFail",updateGpsFail);
//                            intent.putExtra("longitude",longitude);
//                            intent.putExtra("latitude",latitude);
//                            intent.putExtra("info",info);
//                            startActivity(intent);
                                    //Toast.makeText(TestOKHTTPActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                }).start();
            }
        });
    }
}