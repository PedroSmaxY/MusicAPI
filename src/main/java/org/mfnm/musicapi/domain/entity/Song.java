package org.mfnm.musicapi.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = Song.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Song {

    public static final String TABLE_NAME = "song";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "songname", length = 100, nullable = false)
    @NotEmpty
    private String name;

    @Column(name = "artistname", length = 100, nullable = false)
    @NotEmpty
    private String artist;

    @Column(name = "albumname", length = 100, nullable = true)
    private String album;

    /*
     @Column(name = "audio_file_path", length = 255, nullable = true)
     private Byte[] audioData;
    */

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false, updatable = true)
    private Playlist playlist;
}