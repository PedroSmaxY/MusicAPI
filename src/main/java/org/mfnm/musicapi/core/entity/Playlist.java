package org.mfnm.musicapi.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private long id;
    private String name;
    private User user;
    private List<Song> songs;

    public Playlist() {
        this.songs = new ArrayList<>();
    }

    public Playlist(long id, String name, User user, List<Song> songs){
        this.id = id;
        this.name = name;
        this.user = user;
        this.songs = songs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
