package com.music.lake.musiclib;

import com.music.lake.musiclib.listener.MusicUrlRequest;

public class MusicPlayerConfig {
    /**
     * 是否使用MediaPlayer
     */
    private boolean useExoPlayer;
    private boolean useCache;
    public MusicUrlRequest request;

    private MusicPlayerConfig() {
    }

    /**
     * 配置类的Builder
     */
    public static class Builder {
        boolean useExoPlayer;
        boolean useCache;
        MusicUrlRequest request;

        public Builder setUseExoPlayer(boolean useExoPlayer) {
            this.useExoPlayer = useExoPlayer;
            return this;
        }

        public Builder setUseCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public Builder setUrlRequest(MusicUrlRequest request) {
            this.request = request;
            return this;
        }

        void applyConfig(MusicPlayerConfig config) {
            config.useExoPlayer = this.useExoPlayer;
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
