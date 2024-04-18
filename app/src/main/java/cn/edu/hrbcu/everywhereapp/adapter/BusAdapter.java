package cn.edu.hrbcu.everywhereapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.hrbcu.everywhereapp.R;
import cn.edu.hrbcu.everywhereapp.entity.BusLocation;

public class BusAdapter extends BaseAdapter {
    private LayoutInflater mInflater = null;
    Context context;
    List<BusLocation> mDatas;

    public BusAdapter(Context context, List<BusLocation> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        //根据context上下文加载布局，这里的是Demo17Activity本身，即this
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.spinner_bus_item,null);
            holder.busname = (TextView)convertView.findViewById(R.id.spinner_busname);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.busname.setText((String) mDatas.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        TextView busname;
    }
}
