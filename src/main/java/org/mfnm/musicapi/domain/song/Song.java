package org.mfnm.musicapi.domain.song;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mfnm.musicapi.domain.playlist.Playlist;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = Song.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @ManyToMany(mappedBy = "songs")
    private List<Playlist> playlists = new ArrayList<>();
}