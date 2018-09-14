/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyl.musiclake.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.design.internal.ScrimInsetsFrameLayout;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.cyl.musiclake.R;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Represents a standard navigation menu for application. The menu contents can be populated
 * by a menu resource file.
 * <p>NavigationView is typically placed inside a {@link android.support.v4.widget.DrawerLayout}.
 * </p>
 * <pre>
 * &lt;android.support.v4.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:app="http://schemas.android.com/apk/res-auto"
 *     android:id="@+id/drawer_layout"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:fitsSystemWindows="true"&gt;
 *
 *     &lt;!-- Your contents --&gt;
 *
 *     &lt;android.support.design.widget.NavigationView
 *         android:id="@+id/navigation"
 *         android:layout_width="wrap_content"
 *         android:layout_height="match_parent"
 *         android:layout_gravity="start"
 *         app:menu="@menu/my_navigation_items" /&gt;
 * &lt;/android.support.v4.widget.DrawerLayout&gt;
 * </pre>
 */
public class MyNavigationView extends ScrimInsetsFrameLayout {

    private int mMaxWidth;

    public MyNavigationView(Context context) {
        this(context, null);
    }

    public MyNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public MyNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.header_nav, this);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        switch (MeasureSpec.getMode(widthSpec)) {
            case MeasureSpec.EXACTLY:
                // Nothing to do
                break;
            case MeasureSpec.AT_MOST:
                widthSpec = MeasureSpec.makeMeasureSpec(
                        Math.min(MeasureSpec.getSize(widthSpec), mMaxWidth), MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                widthSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY);
                break;
        }
        // Let super sort out the height
        super.onMeasure(widthSpec, heightSpec);
    }

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @Override
    protected void onInsetsChanged(WindowInsetsCompat insets) {
    }



}
