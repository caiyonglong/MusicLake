// IMusicService.aidl
package com.cyl.musiclake;

// Declare any non-default types here with import statements
import com.cyl.musiclake.bean.Music;

interface IMusicService {
    void nextPlay(in Music music);
    void playMusic(in Music music);
    void playPlaylist(in List<Music> playlist,int id,String pid);
    void play(int id);
    void playPause();
    void pause();
    void stop();
    void prev();
    void next();
    void setLoopMode(int loopmode);
    void seekTo(int ms);
    int position();
    int getDuration();
    int getCurrentPosition();
    boolean isPlaying();
    boolean isPause();
    String getSongName();
    String getSongArtist();
    Music getPlayingMusic();
    List<Music> getPlayList();
    void removeFromQueue(int position);
    void clearQueue();
    void showDesktopLyric(boolean show);
    int AudioSessionId();
}
