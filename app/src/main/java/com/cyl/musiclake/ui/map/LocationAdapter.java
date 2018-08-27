package com.cyl.musiclake.ui.map;

import android.support.annotation.Nullable;
import com.cyl.musiclake.utils.LogUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.ui.map.location.Location;
import com.cyl.musiclake.ui.my.user.User;
import com.cyl.musiclake.utils.CoverLoader;
import com.cyl.musiclake.utils.FormatUtil;

import java.util.List;

/**
 * Created by D22434 on 2018/1/4.
 */

public class LocationAdapter extends BaseQuickAdapter<Location, BaseViewHolder> {

    private static String TAG = "LocationAdapter";

    public LocationAdapter(@Nullable List<Location> data) {
        super(R.layout.item_near, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Location location) {
        User user = location.getUser();
        String time = FormatUtil.INSTANCE.getTimeDifference(location.getLocation_time());
        holder.setText(R.id.user_name, user.getName());
        holder.setText(R.id.user_signature, location.getUser_song());
        holder.setText(R.id.location_time, String.format("%s在听：", time));
//        holder.setText(R.id.user_distance,location.getLocation_time());
        CoverLoader.loadImageView(mContext, user.getAvatar(), holder.getView(R.id.user_img));
        LogUtil.e(TAG, location.toString());
    }


}
