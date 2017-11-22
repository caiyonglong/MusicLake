package com.cyl.musiclake.mvp.contract;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.cyl.musiclake.mvp.presenter.BasePresenter;
import com.cyl.musiclake.mvp.view.BaseView;

public interface ArtistDetailContract {

    interface View extends BaseView {

        Context getContext();

        void showArtistArt(Bitmap bitmap);

        void showArtistArt(Drawable drawable);

    }

    interface Presenter extends BasePresenter<View> {

        void subscribe(long artistID);

        void loadArtistArt(long artistID);
    }

}
