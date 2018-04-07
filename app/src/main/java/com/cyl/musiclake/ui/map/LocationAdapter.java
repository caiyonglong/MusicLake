package com.cyl.musiclake.ui.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.musiclake.R;
import com.cyl.musiclake.api.GlideApp;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.ui.map.location.Location;
import com.cyl.musiclake.utils.FormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D22434 on 2018/1/4.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationAdapterHolder> {


    private Context mContext;
    private List<Location> mData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private String TAG = "LocationAdapter";

    public LocationAdapter(Context mContext, List<Location> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public LocationAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mLayoutInflater.inflate(R.layout.item_near, parent, false);
        return new LocationAdapterHolder(mView);
    }

    @Override
    public void onBindViewHolder(LocationAdapterHolder holder, int position) {

        User user = mData.get(position).getUser();
        String time = FormatUtil.getTimeDifference(mData.get(position).getLocation_time());
        holder.user_name.setText(user.getName());
        holder.user_signature.setText(mData.get(position).getUser_song());
        holder.location_time.setText(String.format("%s在听：", time));
        holder.user_distance.setVisibility(View.GONE);
        GlideApp.with(mContext)
                .load(user.getAvatar())
                .into(holder.user_img);
        Log.e(TAG, mData.get(position).toString());

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
