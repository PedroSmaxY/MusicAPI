package org.mfnm.musicapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    @Column(name = "songtitle", length = 100, nullable = false)
    @NotEmpty
    private String title;

    @Column(name = "artistname", length = 100, nullable = false)
    @NotEmpty
    private String artist;

    @Column(name = "albumtitle", length = 100, nullable = true)
    private String albumTitle;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "audio_data", columnDefinition = "LONGBLOB", nullable = true)
    private byte[] audioData;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = true, updatable = true)
    private Playlist playlist;
}