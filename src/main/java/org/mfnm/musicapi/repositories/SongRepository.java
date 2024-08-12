package org.mfnm.musicapi.repositories;

import lombok.NonNull;
import org.mfnm.musicapi.domain.song.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByTitle(String title);

    List<Song> findByArtist(String artist);

    @NonNull
    Page<Song> findAll(@NonNull Pageable pageable);

    List<Song> findByTitleContainingIgnoreCase(String query);

    @Query("SELECT s FROM Song s WHERE s.albumTitle = :albumTitle")
    List<Song> findByAlbum(@Param("albumTitle") String albumTitle);
}
