package cn.edu.hrbcu.everywhereapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.entity.BusLocation;
import cn.edu.hrbcu.everywhereapp.entity.BusStop;

//
public class BusStopsApapter extends ArrayAdapter<BusStop> {
    private List<BusStop> busStops = null;

    public BusStopsApapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<BusStop> objects) {
        super(context, resource, textViewResourceId,objects);
        busStops = objects;
    }

    @Override
    public int getCount() {
        return busStops.size();
    }

    @Nullable
    @Override
    public BusStop getItem(int position) {
        return busStops.get(position);
    }

    @Override
    public int getPosition(@Nullable BusStop item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return busStops.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        获取当前子项内容
        BusStop busStop = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bustop_item,parent,false);
        TextView sitename = view.findViewById(R.id.sitename);
//        TextView longtitude = view.findViewById(R.id.longtitude);
//        TextView latitude = view.findViewById(R.id.latitude);

        sitename.setText(busStop.getSiteName());
//        if(busLocation.getLongtitude() == null){
//            longtitude.setText("--");
//        }else{
//            longtitude.setText(dblToLocation(busLocation.getLongtitude()));
//        }
//
//        if(busLocation.getLatitude() == null){
//            latitude.setText("--");
//        }else{
//            latitude.setText(dblToLocation(busLocation.getLatitude()));
//        }

        return view;
    }

    public static String dblToLocation(double data) {
        StringBuffer result = new StringBuffer();
        //得到度
        int du = (int) data;
        result = result.append(String.valueOf(du) + "°");
        //得到分
        double du1 = data - du;
        int min = (int) (du1 * 60);
        result = result.append(String.valueOf(min) + "′");
        //得到秒
        double min1 = du1 * 60 - min;
        int sec = (int) (min1 * 60);
        result = result.append(String.valueOf(sec) + "″");
        return result.toString();
    }
}
