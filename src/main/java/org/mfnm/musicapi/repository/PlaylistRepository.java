package org.mfnm.musicapi.repository;

import org.mfnm.musicapi.domain.playlist.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
