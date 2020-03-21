package com.music.lake.musiclib;

import com.music.lake.musiclib.listener.MusicRequest;

public class MusicPlayerConfig {
    /**
     * 是否使用MediaPlayer
     */
    private boolean useMediaPlayer;
    private boolean useCache;
    public MusicRequest request;

    private MusicPlayerConfig() {
    }

    /**
     * 配置类的Builder
     */
    public static class Builder {
        boolean useMediaPlayer;
        boolean useCache;
        MusicRequest request;

        public Builder setUseMediaPlayer(boolean useMediaPlayer) {
            this.useMediaPlayer = useMediaPlayer;
            return this;
        }

        public Builder setUseCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public Builder setUrlRequest(MusicRequest request) {
            this.request = request;
            return this;
        }

        void applyConfig(MusicPlayerConfig config) {
            config.useMediaPlayer = this.useMediaPlayer;
            config.useCache = this.useCache;
            config.request = this.request;
        }

        public MusicPlayerConfig create() {
            MusicPlayerConfig config = new MusicPlayerConfig();
            applyConfig(config);
            return config;
        }
    }
}
