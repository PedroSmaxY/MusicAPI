package org.mfnm.musicapi.repositories;

import lombok.NonNull;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @NonNull
    Page<Playlist> findAll(@NonNull Pageable pageable);

    List<Playlist> findByTitleContainingIgnoreCase(String query);
}
