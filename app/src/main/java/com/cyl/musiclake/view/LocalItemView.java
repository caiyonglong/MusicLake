package com.cyl.musiclake.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyl.musiclake.R;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;


/**
 * Created by D22434 on 2018/1/4.
 */

public class LocalItemView extends LinearLayout {
    private MaterialIconView mIcon, mPlay;
    private TextView mName;
    private TextView mDesc;
    private View mView;
    private int icon_color;
    private Context mContext;

    public LocalItemView(Context context) {
        super(context, null);
    }

    public LocalItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LocalItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LocalItemView);
        String name = ta.getString(R.styleable.LocalItemView_tv_name);
        String desc = ta.getString(R.styleable.LocalItemView_tv_desc);
        int icon = ta.getInt(R.styleable.LocalItemView_iv_icon, 0);
        icon_color = ta.getColor(R.styleable.LocalItemView_iv_icon_color, Color.BLACK);
        ta.recycle();

        mContext = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        mView = inflater.inflate(R.layout.item_layout_view, this, true);
        mIcon = mView.findViewById(R.id.iv_icon);
        mPlay = mView.findViewById(R.id.iv_play);
        mName = mView.findViewById(R.id.tv_name);
        mDesc = mView.findViewById(R.id.tv_desc);

        mName.setText(name);
        mDesc.setText(desc);
        mIcon.setColor(icon_color);
        if (icon == 0) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.LIBRARY_MUSIC);
        } else if (icon == 1) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.HEART_OUTLINE);
        } else if (icon == 2) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.TIMETABLE);
        } else if (icon == 3) {
            mIcon.setIcon(MaterialDrawableBuilder.IconValue.ARROW_COLLAPSE_DOWN);
        }

        mPlay.setOnClickListener(view -> {
            if (listener != null) {
                listener.click(view);
            }
        });
        mView.setOnClickListener(view -> {
            if (listener != null) {
                listener.click(view);
            }
        });
    }

    public void setSongsNum(int num) {
        mDesc.setText(num + " 首");
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void click(View view);
    }

}
