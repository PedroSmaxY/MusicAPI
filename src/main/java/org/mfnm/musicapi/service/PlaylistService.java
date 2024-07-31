package org.mfnm.musicapi.service;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.mfnm.musicapi.domain.playlist.PlaylistRequestDTO;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.user.User;
import org.mfnm.musicapi.repository.PlaylistRepository;
import org.mfnm.musicapi.repository.SongRepository;
import org.mfnm.musicapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    public Playlist findById(Long id) {
        return this.playlistRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Playlist not found")
        );
    }


    public Playlist createPlaylistWithSongs(String title, Long userId, List<Long> songIds) {
        Playlist playlist = new Playlist();
        List<Song> songs = this.songRepository.findAllById(songIds);
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        playlist.setId(null);
        playlist.setUser(user);
        playlist.setTitle(title);
        playlist.setSongs(songs);

        return this.playlistRepository.save(playlist);
    }

    public Playlist createPlaylistEmpty(String title, Long userId) {
        Playlist playlist = new Playlist();
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found!")
        );

        playlist.setId(null);
        playlist.setTitle(title);
        playlist.setUser(user);
        playlist.setSongs(List.of());

        return this.playlistRepository.save(playlist);
    }

    public Playlist addSongsToPlaylist(Long playlistId, List<Long> songIds) {
        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException(
                        "Playlist not found!"
                ));

        List<Song> songs = this.songRepository.findAllById(songIds);
        playlist.getSongs().addAll(songs);
        return this.playlistRepository.save(playlist);
    }

    public Playlist removeSongsFromPlaylist(Long playlistId, List<Long> songIds) {
        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found!"));
        List<Song> songsToRemove = this.songRepository.findAllById(songIds);

        playlist.getSongs().removeAll(songsToRemove);
        return this.playlistRepository.save(playlist);
    }

    public Playlist updatePlaylist(Long playlistId, PlaylistRequestDTO playlistRequestDTO) {

        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found!"));

        playlist.setTitle(playlistRequestDTO.title());
        playlist.setSongs(this.songRepository.findAllById(playlistRequestDTO.songIds()));

        return this.playlistRepository.save(playlist);
    }
}
