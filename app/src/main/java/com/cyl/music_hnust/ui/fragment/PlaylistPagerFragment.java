package com.cyl.music_hnust.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyl.music_hnust.R;
import com.cyl.music_hnust.dataloaders.PlaylistLoader;
import com.cyl.music_hnust.model.music.Playlist;
import com.cyl.music_hnust.ui.activity.PlaylistDetailActivity;
import com.cyl.music_hnust.utils.Extras;
import com.cyl.music_hnust.utils.SystemUtils;
import com.cyl.music_hnust.view.CreatePlaylistDialog;

import java.util.List;
import java.util.Random;

import butterknife.Bind;

import static android.app.ActivityOptions.makeSceneTransitionAnimation;

/**
 * 作者：yonglong on 2016/11/6 17:09
 */
public class PlaylistPagerFragment extends Fragment {
    @Bind(R.id.name)
    TextView playlistame;
    @Bind(R.id.songcount)
    TextView songcount;
    @Bind(R.id.number)
    TextView playlistnumber;
    @Bind(R.id.playlisttype)
    TextView playlisttype;
    @Bind(R.id.playlist_image)
    ImageView playlistImage;
    @Bind(R.id.foreground)
    View foreground;

    private static final String ARG_PAGE_NUMBER = "pageNumber";
    int[] foregroundColors = {R.color.pink_transparent, R.color.green_transparent, R.color.blue_transparent, R.color.red_transparent, R.color.purple_transparent};
    int[] backgroundResours = {
            R.drawable.music_one,
            R.drawable.music_two,
            R.drawable.music_three,
            R.drawable.music_four,
            R.drawable.music_five,
            R.drawable.music_six,
            R.drawable.music_seven,
            R.drawable.music_eight,
            R.drawable.music_nine,
            R.drawable.music_ten,
            R.drawable.music_eleven,
            R.drawable.music_twelve};
    private int pageNumber, songCountInt;
    private int foregroundColor;
    private int backgroundImage;
    private long firstAlbumID = -1;
    private Playlist playlist;

    public static PlaylistPagerFragment newInstance(int pageNumber) {
        PlaylistPagerFragment fragment = new PlaylistPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist_pager, container, false);
        final List<Playlist> playlists = PlaylistLoader.getPlaylists(getActivity(), true);


        int position = (int) getArguments().get(ARG_PAGE_NUMBER);
        if (position == playlists.size()) {
            playlistame.setText("新建歌单");
            songcount.setVisibility(View.INVISIBLE);
            playlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreatePlaylistDialog.newInstance().show(getChildFragmentManager(), "CREATE_PLAYLIST");
                }
            });
        } else {
            pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
            playlist = playlists.get(pageNumber);
            playlistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                        getActivity().startActivityForResult(intent, Extras.TO_PLAYLISTDETAIL, options.toBundle());
                    } else {
                        getActivity().startActivityForResult(intent, Extras.TO_PLAYLISTDETAIL);
                    }
                }
            });

            setUpPlaylistDetails();
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedinstancestate) {
        new loadPlaylistImage().execute("");
    }

    private void setUpPlaylistDetails() {
        playlistame.setText(playlist.getName());

        int number = getArguments().getInt(ARG_PAGE_NUMBER) + 1;
        String playlistnumberstring;

        if (number > 9) {
            playlistnumberstring = String.valueOf(number);
        } else {
            playlistnumberstring = "0" + String.valueOf(number);
        }
        playlistnumber.setText(playlistnumberstring);

        Random random = new Random();
        int rndInt = random.nextInt(foregroundColors.length);

        foregroundColor = foregroundColors[rndInt];
        foreground.setBackgroundColor(foregroundColor);

//        if (pageNumber > 2) {
        playlisttype.setVisibility(View.GONE);
//        }

    }

    private class loadPlaylistImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null) {
//                List<Music> playlistsong = PlaylistLoader.getMusicForPlaylist(getActivity(), playlist.getId());
//                songCountInt = playlistsong.size();
//                if (songCountInt != 0) {
//
//                    Log.e("-----", songCountInt + "====");
//                    Log.e("-----", playlistsong.get(0).toString() + "====");
//
//                    Log.e("-----", playlistsong.get(0).getCoverUri() + "====");
//                    return playlistsong.get(0).getCoverUri();
//                } else
                return "nosongs";

            } else return "context is null";

        }

        @Override
        protected void onPostExecute(String uri) {
            Random random = new Random();
            int rndInt = random.nextInt(backgroundResours.length);

            backgroundImage = backgroundResours[rndInt];
            playlistImage.setImageResource(backgroundImage);

            songcount.setText(" " + String.valueOf(songCountInt) + " 首歌曲");
        }

        @Override
        protected void onPreExecute() {
        }
    }

}
