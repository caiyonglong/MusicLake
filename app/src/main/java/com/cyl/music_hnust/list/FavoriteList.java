package com.cyl.music_hnust.list;

import com.cyl.music_hnust.utils.MusicInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 */
public class FavoriteList {

	public static final List<MusicInfo> list = new ArrayList<MusicInfo>();

	public static void sort() {
		Collections.sort(list, new MusicInfo());
	}

}
