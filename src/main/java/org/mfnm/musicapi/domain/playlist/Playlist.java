package org.mfnm.musicapi.domain.playlist;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.user.User;


@Data
@Entity
@Table(name = Playlist.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Playlist {
    public static final String TABLE_NAME = "playlist";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "playlist_title", length = 100)
    @NotNull
    @NotEmpty
    private String title;

    @ManyToMany
    @JoinTable(
            name = "playlist_song",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private List<Song> songs = new ArrayList<>();

}
