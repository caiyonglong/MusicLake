package com.music.lake.musiclib.player;

import android.os.Binder;

import com.music.lake.musiclib.bean.BaseMusicInfo;
import com.music.lake.musiclib.listener.MusicPlayEventListener;
import com.music.lake.musiclib.listener.MusicPlayerController;
import com.music.lake.musiclib.listener.MusicRequest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * version  : 2020/2/29
 * function :
 */
public class MusicServiceBinder extends Binder {

    private MusicPlayerController controller;

    public MusicServiceBinder(MusicPlayerController controller) {
        this.controller = controller;
    }

    public void playMusicById(int index) {
        if (controller != null) {
            controller.playMusicById(index);
        }
    }

    public void playMusic(BaseMusicInfo song) {
        if (controller != null) {
            controller.playMusic(song);
        }
    }

    public void playMusic(List<BaseMusicInfo> songs, int index) {
        if (controller != null) {
            controller.playMusic(songs, index);
        }
    }

    public void playNextMusic() {
        if (controller != null) {
            controller.playNextMusic();
        }
    }


    public void playPrevMusic() {
        if (controller != null) {
            controller.playPrevMusic();
        }
    }


    public void restorePlay() {
        if (controller != null) {
            controller.restorePlay();
        }
    }

    public void pausePlay() {
        if (controller != null) {
            controller.pausePlay();
        }
    }


    public void stopPlay() {
        if (controller != null) {
            controller.stopPlay();
        }
    }


    public void setLoopMode(int mode) {
        if (controller != null) {
            controller.setLoopMode(mode);
        }
    }

    public int getLoopMode() {
        if (controller != null) {
            return controller.getLoopMode();
        }
        return 0;
    }


    public void seekTo(long ms) {
        if (controller != null) {
            controller.seekTo(ms);
        }
    }

    public BaseMusicInfo getNowPlayingMusic() {
        if (controller != null) {
            return controller.getNowPlayingMusic();
        }
        return null;
    }


    public int getNowPlayingIndex() {
        if (controller != null) {
            return controller.getNowPlayingIndex();
        }
        return 0;
    }

    @NotNull
    public List<BaseMusicInfo> getPlayList() {
        if (controller != null) {
            return controller.getPlayList();
        }
        return null;
    }


    public void removeFromPlaylist(int position) {
        if (controller != null) {
            controller.removeFromPlaylist(position);
        }
    }


    public void clearPlaylist() {
        if (controller != null) {
            controller.clearPlaylist();
        }
    }


    public boolean isPlaying() {
        if (controller != null) {
            return controller.isPlaying();
        }
        return false;
    }


    public long getDuration() {
        if (controller != null) {
            return controller.getDuration();
        }
        return 0;
    }


    public long getPlayingPosition() {
        if (controller != null) {
            return controller.getPlayingPosition();
        }
        return 0;
    }


    public void addMusicPlayerEventListener(@NotNull MusicPlayEventListener listener) {
        if (controller != null) {
            controller.addMusicPlayerEventListener(listener);
        }
    }


    public void removeMusicPlayerEventListener(@NotNull MusicPlayEventListener listener) {
        if (controller != null) {
            controller.removeMusicPlayerEventListener(listener);
        }
    }

    public void showDesktopLyric(boolean show) {

    }

    public void setMusicRequestListener(MusicRequest request) {
        if (controller != null) {
            controller.setMusicRequestListener(request);
        }
    }

    public int AudioSessionId() {
        if (controller != null) {
            return controller.AudioSessionId();
        }
        return 0;
    }
}