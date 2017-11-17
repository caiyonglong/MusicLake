package com.cyl.music_hnust.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.bean.music.Playlist;
import com.cyl.music_hnust.ui.activity.PlaylistDetailActivity;
import com.cyl.music_hnust.ui.fragment.base.BaseFragment;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.SystemUtils;

import butterknife.BindView;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

/**
 * 作者：yonglong on 2016/11/6 17:09
 */
public class PlaylistPagerFragment extends BaseFragment {
    @BindView(R.id.name)
    TextView playlistame;
    @BindView(R.id.songcount)
    TextView songcount;
    @BindView(R.id.number)
    TextView playlistnumber;
    @BindView(R.id.playlisttype)
    TextView playlisttype;
    @BindView(R.id.playlist_image)
    ImageView playlistImage;
    @BindView(R.id.foreground)
    View foreground;

    private static final String TAG_PAGE_NUMBER = "pageNumber";
    private static final String TAG_PLAYLIST = "pid";
    int[] backgroundResourse = {
            R.drawable.music_one,
            R.drawable.music_two,
            R.drawable.music_three,
            R.drawable.music_seven,
            R.drawable.music_eight,
            R.drawable.music_nine,
            R.drawable.music_ten,
            R.drawable.music_eleven};
    private int pageNumber;
    private int foregroundColor;
    private int backgroundImage;
    private long firstAlbumID = -1;
    private Playlist playlist;


    public static PlaylistPagerFragment newInstance(int pageNumber, Playlist playlist) {
        PlaylistPagerFragment fragment = new PlaylistPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_PAGE_NUMBER, pageNumber);
        bundle.putSerializable(TAG_PLAYLIST, playlist);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_playlist_pager;
    }

    @Override
    public void initViews() {
        playlist = (Playlist) getArguments().getSerializable(TAG_PLAYLIST);
        pageNumber = getArguments().getInt(TAG_PAGE_NUMBER);
    }

    @Override
    protected void listener() {
        playlistImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PlaylistDetailFragment detailFragment =
//                        PlaylistDetailFragment.newInstance(playlist.getId(), playlist.getName());
//
//                getActivity().getSupportFragmentManager()
//                        .beginTransaction()
//                        .addSharedElement(playlistame, "transition_playlist_name")
//                        .addSharedElement(playlistImage, "transition_album_art")
//                        .addSharedElement(foreground, "transition_foreground")
//                        .add(R.id.fragment_container, detailFragment)
//                        .addToBackStack(null)
//                        .commit();
//
                final Intent intent = new Intent(getActivity(), PlaylistDetailActivity.class);
                //传递参数
                intent.putExtra(Extras.PLAYLIST_ID, playlist.getId());
                intent.putExtra(Extras.PLAYLIST_FOREGROUND_COLOR, foregroundColor);
                intent.putExtra(Extras.PLAYLIST_BACKGROUND_IMAGE, backgroundImage);
                intent.putExtra(Extras.ALBUM_ID, firstAlbumID);
                intent.putExtra(Extras.PLAYLIST_NAME, String.valueOf(playlistame.getText()));

                if (SystemUtils.isLollipop()) {
                    ActivityOptions options = makeSceneTransitionAnimation
                            (getActivity(),
                                    Pair.create((View) playlistame, "transition_playlist_name"),
                                    Pair.create((View) playlistImage, "transition_album_art"),
                                    Pair.create(foreground, "transition_foreground"));
                    getActivity().startActivity(intent, options.toBundle());
                } else {
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initDatas() {

        playlistame.setText(playlist.getName());
        playlistnumber.setText(pageNumber + 1 + "");
        songcount.setText(" " + playlist.getCount() + " 首歌曲");

//
//        foregroundColor = foregroundColors[rndInt];
//        foreground.setBackgroundColor(foregroundColor);
        backgroundImage = backgroundResourse[pageNumber % backgroundResourse.length];
        playlistImage.setImageResource(backgroundImage);

    }


}
