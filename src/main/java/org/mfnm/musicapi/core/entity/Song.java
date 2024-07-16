package org.mfnm.musicapi.core.entity;

//import javax.persistence.*;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
@Entity
@Table(schema = "music_db", name = "songs")
public class Song  implements Serializable {

    private static final long serialVersionUID = 1l;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String artist;
    private String album;
    private Date releaseDate;

    public long getId() {
        return id;
    }

    public Song(int id, String name, String artist, String album, Date releaseDate){
        this.id = id;
        this.name = artist;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
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