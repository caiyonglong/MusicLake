package com.cyl.musiclake.event;

/**
 * Created by D22434 on 2018/1/10.
 */

public class StatusChangedEvent {
    private String name;

    public StatusChangedEvent() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusChangedEvent that = (StatusChangedEvent) o;

        return name != null ? !name.equals(that.name) : that.name != null;
    }

}
