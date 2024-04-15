package cn.edu.hrbcu.everywhereapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.edu.hrbcu.everywhereapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import cn.edu.hrbcu.everywhereapp.entity.BusLocation;

public class BusLocationApapter extends ArrayAdapter<BusLocation> {
    private List<BusLocation> busLocations = null;

    public BusLocationApapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<BusLocation> objects) {
        super(context, resource, textViewResourceId, objects);
        busLocations = objects;
    }

    @Override
    public int getCount() {
        return busLocations.size();
    }

    @Nullable
    @Override
    public BusLocation getItem(int position) {
        return busLocations.get(position);
    }

    @Override
    public int getPosition(@Nullable BusLocation item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return busLocations.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BusLocation busLocation = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.buslocation_item,parent,false);
        TextView busname = view.findViewById(R.id.bus_name);
        TextView longtitude = view.findViewById(R.id.longtitude);
        TextView latitude = view.findViewById(R.id.latitude);

        busname.setText(busLocation.getName());
        longtitude.setText(dblToLocation(busLocation.getLongtitude()));
        latitude.setText(dblToLocation(busLocation.getLatitude()));

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
