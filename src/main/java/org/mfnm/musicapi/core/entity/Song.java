package org.mfnm.musicapi.core.entity;

import java.util.Date;

public class Song {

    private long id;
    private String name;
    private String artist;
    private String album;
    private Date releaseDate;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public Date getReleaseDate(){return  releaseDate;}
}