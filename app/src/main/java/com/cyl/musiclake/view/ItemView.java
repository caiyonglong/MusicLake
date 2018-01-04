package com.cyl.musiclake.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;

/**
 * Created by D22434 on 2018/1/4.
 */

public class ItemView extends LinearLayout {
    private ImageView mIcon;
    private TextView mName;
    private TextView mDesc;
    private ImageView mPlay;
    private View mView;

    public ItemView(Context context) {
        super(context, null);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        String name = ta.getString(R.styleable.ItemView_tv_name);
        String desc = ta.getString(R.styleable.ItemView_tv_desc);
        Drawable icon = ta.getDrawable(R.styleable.ItemView_iv_icon);
        ta.recycle();

        LayoutInflater inflater = LayoutInflater.from(context);
        mView = inflater.inflate(R.layout.item_layout_view, this, true);
        mIcon = mView.findViewById(R.id.iv_icon);
        mPlay = mView.findViewById(R.id.iv_play);
        mName = mView.findViewById(R.id.tv_name);
        mDesc = mView.findViewById(R.id.tv_desc);

        mName.setText(name);
        mDesc.setText(desc);
        mIcon.setImageDrawable(icon);

    }


}
