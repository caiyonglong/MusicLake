// IMusicService.aidl
package com.cyl.music_hnust;

// Declare any non-default types here with import statements
import com.cyl.music_hnust.bean.music.Music;

interface IMusicService {
    void playOnline(in Music music);
    void play(int id);
    void playPause();
    void prev();
    void next();
    void refresh();
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
    void setPlayList(in List<Music> playlist);
    List<Music> getPlayList();
    void removeFromQueue(int position);
    void clearQueue();
}
