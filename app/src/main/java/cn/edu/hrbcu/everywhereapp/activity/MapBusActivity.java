package cn.edu.hrbcu.everywhereapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import cn.edu.hrbcu.everywhereapp.R;

public class MapBusActivity extends AppCompatActivity {
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_bus);
        //////////////////////////////////////////////
        textView = findViewById(R.id.textview_busname);
        String text = textView.getText().toString();
        String busname = getIntent().getStringExtra("busname");
        textView.setText(text + busname);
        /////////////////////////////////////////////////
        // 显示高德地图
    }
}