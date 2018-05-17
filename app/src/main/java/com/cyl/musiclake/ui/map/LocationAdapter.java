package com.cyl.musiclake.ui.map;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.map.location.Location;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D22434 on 2018/1/4.
 */

public class LocationAdapter extends BaseQuickAdapter<Location,BaseViewHolder> {


    private Context mContext;
    private List<Location> mData = new ArrayList<>();
    private String TAG = "LocationAdapter";

    public LocationAdapter( @Nullable List<Location> data) {
        super(R.layout.item_near, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Location location) {
        User user = location.getUser();
        String time = FormatUtil.getTimeDifference(location.getLocation_time());
        holder.setText(R.id.user_name,user.getName());
        holder.setText(R.id.user_signature,location.getUser_song());
        holder.setText(R.id.location_time,String.format("%s在听：", time));
//        holder.setText(R.id.user_distance,location.getLocation_time());
        CoverLoader.loadImageView(mContext, user.getAvatar(), holder.getView(R.id.user_img));
        Log.e(TAG, location.toString());
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class LocationAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView user_img;
        private TextView user_name;
        private TextView user_signature;
        private TextView user_distance;
        private TextView location_time;

        private LocationAdapterHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_img = (ImageView) itemView.findViewById(R.id.user_img);
            user_signature = (TextView) itemView.findViewById(R.id.user_signature);
            user_distance = (TextView) itemView.findViewById(R.id.user_distance);
            location_time = (TextView) itemView.findViewById(R.id.location_time);
        }
    }
}
