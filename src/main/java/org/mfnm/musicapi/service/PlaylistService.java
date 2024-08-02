package org.mfnm.musicapi.service;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.mfnm.musicapi.domain.playlist.PlaylistRequestDTO;
import org.mfnm.musicapi.domain.playlist.PlaylistUpdateDTO;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.user.User;
import org.mfnm.musicapi.repository.PlaylistRepository;
import org.mfnm.musicapi.repository.SongRepository;
import org.mfnm.musicapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Playlist createPlaylist(PlaylistRequestDTO playlistRequestDTO) {

        User user = this.userRepository.findById(playlistRequestDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Playlist playlist = new Playlist();
        playlist.setTitle(playlistRequestDTO.title());
        playlist.setUser(user);

        if (playlistRequestDTO.imageData() != null) {
            playlist.setImageData(playlistRequestDTO.imageData());
        }

        List<Song> songs = this.songRepository.findAllById(playlistRequestDTO.songIds());
        playlist.setSongs(songs);

        return this.playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist addSongsToPlaylist(Long playlistId, List<Long> songIds) {
        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        playlist.getSongs().addAll(this.songRepository.findAllById(songIds));
        return this.playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist removeSongsFromPlaylist(Long playlistId, List<Long> songIds) {
        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        playlist.getSongs().removeIf(song -> songIds.contains(song.getId()));
        return this.playlistRepository.save(playlist);
    }

    @Transactional
    public void updatePlaylist(Long playlistId, PlaylistUpdateDTO playlistUpdateDTO) {

        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found!"));

        playlist.setTitle(playlistUpdateDTO.title());

        if (playlistUpdateDTO.image_data() != null) {
            playlist.setImageData(playlistUpdateDTO.image_data());
        }

        this.playlistRepository.save(playlist);
    }

    @Transactional
    public void deletePlaylist(Long playlistId) {

        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        playlist.getSongs().forEach(song -> song.getPlaylists().remove(playlist));
        this.songRepository.saveAll(playlist.getSongs());

        User user = playlist.getUser();
        user.getPlaylists().remove(playlist);
        this.userRepository.save(user);

        this.playlistRepository.delete(playlist);
    }
}
