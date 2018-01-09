package com.cyl.musiclake.ui.localmusic.presenter;

/**
 * Created by yonglong on 2017/11/22.
 */

public interface MusicStateListener {

    /**
     * Called when REFRESH is invoked
     */
    void restartLoader();

    /**
     * Called when PLAYLIST_CHANGED is invoked
     */
    void onPlaylistChanged();

    /**
     * Called when META_CHANGED is invoked
     */
    void onMetaChanged();

}
