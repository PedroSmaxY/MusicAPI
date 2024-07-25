package org.mfnm.musicapi.service;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.entity.Song;
import org.mfnm.musicapi.repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    public Song findById(Long id) {
        Optional<Song> song = songRepository.findById(id);
        return song.orElseThrow(() -> new RuntimeException(
                "Song not found! Id: " + id
        ));
    }

    public List<Song> findByTitle(String title) {
        return songRepository.findByTitle(title);
    }

    public List<Song> findByArtist(String artist) {
        return songRepository.findByArtist(artist);
    }

    public List<Song> findByAlbum(String album) {
        return songRepository.findByAlbum(album);
    }

    @Transactional
    public Song create(Song song) {
        song.setId(null);
        return songRepository.save(song);
    }

    @Transactional
    public Song update(Song song) {
        if (!songRepository.existsById(song.getId())) {
            throw new RuntimeException("Song not found! Id: " + song.getId());
        }
        return songRepository.save(song);
    }

    public void delete(Long id) {
        if (!songRepository.existsById(id)) {
            throw new RuntimeException("Song not found! Id: " + id);
        }
        songRepository.deleteById(id);
    }
}
