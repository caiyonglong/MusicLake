package com.cyl.test.control.fragments;

import com.cyl.test.R;

/**
 * 描述
 *
 * @author yonglong
 * @date 2016/8/4
 */
public class MusicFragment extends BaseFragment {

    public static MusicFragment newInstance()
    {

        return new MusicFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    public void initViews() {

    }
}
