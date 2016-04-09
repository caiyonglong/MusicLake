package com.cyl.music_hnust.utils;

import android.graphics.Bitmap;

public class Album {
	public String name;
	public String singerName;
	public Bitmap picture;

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getName() {
		return name;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

}
