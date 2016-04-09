package com.cyl.music_hnust.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.utils.ScanInfo;


/**
 * By CWD 2013 Open Source Project
 *
 * <br>
 * <b>扫描列表适配器</b></br>
 *
 * @author CWD
 * @version 2013.05.12 v1.0 实现列表适配
 */
public class ScanAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ScanInfo> data;

    public ScanAdapter(Context context, List<ScanInfo> data) {
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_scan_item, null);
            holder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.adapter_scan_item_cb);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.adapter_scan_item_tv);
            holder.checkBox
                    .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            // TODO Auto-generated method stub
                            data.get(position).setChecked(isChecked);
                        }
                    });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(data.get(position).getFolderPath());
        holder.checkBox.setChecked(data.get(position).isChecked());

        return convertView;
    }

    static class ViewHolder {
        CheckBox checkBox;
        TextView textView;
    }

    /**
     * 返回用户勾选的路径
     *
     * @return 所选择的路径集合
     */
    public List<String> getPath() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked()) {
                list.add(data.get(i).getFolderPath());
            }
        }
        return list;
    }
}
