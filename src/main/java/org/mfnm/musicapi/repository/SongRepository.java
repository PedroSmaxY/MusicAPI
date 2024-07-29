package org.mfnm.musicapi.repository;

import org.mfnm.musicapi.domain.song.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    public List<Song> findByTitle(String title);

    public List<Song> findByArtist(String artist);

    @Query("SELECT s FROM Song s WHERE s.albumTitle = :albumTitle")
    public List<Song> findByAlbum(@Param("albumTitle") String albumTitle);
}
