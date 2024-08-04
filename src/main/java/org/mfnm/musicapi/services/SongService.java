package org.mfnm.musicapi.services;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.song.SongRequestDTO;
import org.mfnm.musicapi.domain.song.SongUpdateDTO;
import org.mfnm.musicapi.repositories.PlaylistRepository;
import org.mfnm.musicapi.repositories.SongRepository;
import org.mfnm.musicapi.services.exceptions.SongAlreadyExistsException;
import org.mfnm.musicapi.services.exceptions.SongNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    private final PlaylistRepository playlistRepository;

    public Song findById(Long id) {
        return this.songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException(
                        "Song with ID " + id + " not found in the system."
                ));
    }

    public List<Song> findByTitle(String title) {
        return this.songRepository.findByTitle(title);
    }

    public List<Song> findByArtist(String artist) {
        return this.songRepository.findByArtist(artist);
    }

    public List<Song> findByAlbum(String album) {
        return this.songRepository.findByAlbum(album);
    }

    @Transactional
    public Song create(SongRequestDTO songRequestDTO) {

        Song song = new Song();
        song.setTitle(songRequestDTO.title());
        song.setArtist(songRequestDTO.artist());
        song.setAlbumTitle(songRequestDTO.album());
        song.setImageData(songRequestDTO.image_data());
        song.setAudioData(songRequestDTO.audio_data());

        List<Song> existingSongs = this.songRepository.findByTitle(song.getTitle());
        boolean songExists = existingSongs.stream()
                .anyMatch(existingSong -> existingSong.getArtist().equals(song.getArtist()));

        if (!songExists) {
            return songRepository.save(song);
        }

        throw new SongAlreadyExistsException(
                "Song with title '" + song.getTitle() + "' by artist '" + song.getArtist() + "' already exists!"
        );
    }

    @Transactional
    public Song updateSong(SongUpdateDTO songUpdateDTO) {

        Song existingSong = songRepository.findById(songUpdateDTO.id())
                .orElseThrow(() -> new SongNotFoundException("Song with ID " + songUpdateDTO.id() + " not found in the system."));

        existingSong.setTitle(songUpdateDTO.title());
        existingSong.setArtist(songUpdateDTO.artist());
        existingSong.setAlbumTitle(songUpdateDTO.album());

        if (songUpdateDTO.image_data() != null) {
            existingSong.setImageData(songUpdateDTO.image_data());
        }

        return songRepository.save(existingSong);
    }


    public void delete(Long id) {

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song with ID " + id + " not found in the system."));

        for (Playlist playlist : song.getPlaylists()) {
            playlist.getSongs().remove(song);
            playlistRepository.save(playlist);
        }

        songRepository.deleteById(id);
    }

    public ResponseEntity<Resource> getSongResourceResponse(Long id, boolean isDownload) {
        Song song = findById(id);
        Resource resource = createResource(song);
        HttpHeaders headers = createHttpHeaders(song, isDownload);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private Resource createResource(Song song) {
        byte[] audioData = song.getAudioData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
        return new InputStreamResource(byteArrayInputStream);
    }

    private HttpHeaders createHttpHeaders(Song song, boolean isDownload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        if (isDownload) {
            headers.setContentDispositionFormData("attachment", song.getTitle() + ".mp3");
        } else {
            headers.setContentDispositionFormData("inline", song.getTitle() + ".mp3");
        }
        return headers;
    }
}
