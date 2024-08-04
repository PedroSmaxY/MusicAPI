package org.mfnm.musicapi.services;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.mfnm.musicapi.domain.playlist.PlaylistRequestDTO;
import org.mfnm.musicapi.domain.playlist.PlaylistUpdateDTO;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.user.User;
import org.mfnm.musicapi.repositories.PlaylistRepository;
import org.mfnm.musicapi.repositories.SongRepository;
import org.mfnm.musicapi.repositories.UserRepository;
import org.mfnm.musicapi.services.exceptions.PlaylistNotFoundException;
import org.mfnm.musicapi.services.exceptions.SongNotFoundException;
import org.mfnm.musicapi.services.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final UserRepository userRepository;

    public Playlist findById(Long id) {
        return this.playlistRepository.findById(id).orElseThrow(
                () -> new PlaylistNotFoundException("Playlist with ID " + id + " not found in the system.")
        );
    }

    @Transactional
    public Playlist createPlaylist(PlaylistRequestDTO playlistRequestDTO) {
        User user = this.userRepository.findById(playlistRequestDTO.userId())
                .orElseThrow(() -> new UserNotFoundException("User with ID " + playlistRequestDTO.userId() + " not found in the system."));

        Playlist playlist = new Playlist();
        playlist.setTitle(playlistRequestDTO.title());
        playlist.setUser(user);

        if (playlistRequestDTO.imageData() != null) {
            playlist.setImageData(playlistRequestDTO.imageData());
        }

        List<Song> songs = this.songRepository.findAllById(playlistRequestDTO.songIds());
        if (songs.size() != playlistRequestDTO.songIds().size()) {

            Set<Long> foundSongIds = songs.stream().map(Song::getId).collect(Collectors.toSet());
            List<Long> missingSongIds = playlistRequestDTO.songIds().stream()
                    .filter(id -> !foundSongIds.contains(id))
                    .toList();

            if (!missingSongIds.isEmpty()) {
                throw new SongNotFoundException("Songs with IDs " + missingSongIds + " not found in the system.");
            }
        }

        playlist.setSongs(songs);
        return this.playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist addSongsToPlaylist(Long playlistId, List<Long> songIds) {
        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with ID " + playlistId + " not found in the system."));

        playlist.getSongs().addAll(this.songRepository.findAllById(songIds));
        return this.playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist removeSongsFromPlaylist(Long playlistId, List<Long> songIds) {
        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with ID " + playlistId + " not found in the system."));

        playlist.getSongs().removeIf(song -> songIds.contains(song.getId()));
        return this.playlistRepository.save(playlist);
    }

    @Transactional
    public void updatePlaylist(Long playlistId, PlaylistUpdateDTO playlistUpdateDTO) {

        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with ID " + playlistId + " not found in the system."));

        playlist.setTitle(playlistUpdateDTO.title());

        if (playlistUpdateDTO.image_data() != null) {
            playlist.setImageData(playlistUpdateDTO.image_data());
        }

        this.playlistRepository.save(playlist);
    }

    @Transactional
    public void deletePlaylist(Long playlistId) {

        Playlist playlist = this.playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist with ID " + playlistId + " not found in the system."));

        playlist.getSongs().forEach(song -> song.getPlaylists().remove(playlist));
        this.songRepository.saveAll(playlist.getSongs());

        User user = playlist.getUser();
        user.getPlaylists().remove(playlist);
        this.userRepository.save(user);

        this.playlistRepository.delete(playlist);
    }
}
