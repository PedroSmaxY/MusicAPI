package org.mfnm.musicapi.service;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.song.SongRequestDTO;
import org.mfnm.musicapi.domain.song.SongUpdateDTO;
import org.mfnm.musicapi.repository.SongRepository;
import org.mfnm.musicapi.service.exceptions.SongAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    public Song findById(Long id) {
        return this.songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Song not found! Id: " + id
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
                .orElseThrow(() -> new IllegalArgumentException("Song not found"));

        existingSong.setTitle(songUpdateDTO.title());
        existingSong.setArtist(songUpdateDTO.artist());
        existingSong.setAlbumTitle(songUpdateDTO.album());

        if (songUpdateDTO.image_data() != null) {
            existingSong.setImageData(songUpdateDTO.image_data());
        }

        return songRepository.save(existingSong);
    }

    public void delete(Long id) {
        if (!songRepository.existsById(id)) {
            throw new RuntimeException("Song not found! Id: " + id);
        }
        songRepository.deleteById(id);
    }
}
