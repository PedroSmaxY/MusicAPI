package org.mfnm.musicapi.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = Song.TABLE_NAME)
public class Song implements Serializable {

    private static final String TABLE_NAME = "song";
    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "songname", length = 100, nullable = false)
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "artistname", length = 100, nullable = false)
    @NotNull
    @NotEmpty
    private String artist;

    @Column(name = "albumname", length = 100, nullable = true)
    @NotNull
    @NotEmpty
    private String album;

    @Column(name = "releasedate", nullable = true)
    @NotEmpty
    private Date releaseDate;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false, updatable = true)
    private Playlist playlist;

    public Song() {
    }

    public Song(Long id, @NotNull @NotEmpty String name, @NotNull @NotEmpty String artist,
            @NotNull @NotEmpty String album, @NotEmpty Date releaseDate) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.releaseDate = releaseDate;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return String return the album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * @param album the album to set
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * @return Date return the releaseDate
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * @param releaseDate the releaseDate to set
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((artist == null) ? 0 : artist.hashCode());
        result = prime * result + ((album == null) ? 0 : album.hashCode());
        result = prime * result + ((releaseDate == null) ? 0 : releaseDate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Song other = (Song) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (artist == null) {
            if (other.artist != null)
                return false;
        } else if (!artist.equals(other.artist))
            return false;
        if (album == null) {
            if (other.album != null)
                return false;
        } else if (!album.equals(other.album))
            return false;
        if (releaseDate == null) {
            if (other.releaseDate != null)
                return false;
        } else if (!releaseDate.equals(other.releaseDate))
            return false;
        return true;
    }
}