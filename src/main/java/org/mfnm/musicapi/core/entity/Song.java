package org.mfnm.musicapi.core.entity;

import java.util.Date;

public class Song {

    private Long id;
    private String name;
    private String artist;
    private String album;
    private Date releaseDate;

    public Long getId() {
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