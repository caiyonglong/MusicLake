package com.cyl.musiclake.data.model;

/**
 * Created by D22434 on 2018/1/10.
 */

public class HistoryChangedEvent {
    private String id;
    private String name;
    private String artist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HistoryChangedEvent that = (HistoryChangedEvent) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return artist != null ? artist.equals(that.artist) : that.artist == null;
    }

}
