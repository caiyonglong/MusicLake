package com.cyl.musiclake.ui.youtube;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cyl.musiclake.R;
import com.cyl.musiclake.utils.LogUtil;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YoutubeActivity extends AppCompatActivity {

    String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        videoId = getIntent().getStringExtra("videoId");
        LogUtil.d("YoutubeActivity", "|videoId " + videoId);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        View customPlayerUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.custom_player_ui);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                CustomPlayerUiController customPlayerUiController = new CustomPlayerUiController(YoutubeActivity.this, customPlayerUi, youTubePlayer, youTubePlayerView);
                youTubePlayer.addListener(customPlayerUiController);
                youTubePlayerView.addFullScreenListener(customPlayerUiController);
                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        videoId, 0f
                );
            }
        });
    }
}
