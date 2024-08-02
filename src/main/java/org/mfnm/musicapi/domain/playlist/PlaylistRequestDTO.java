package org.mfnm.musicapi.domain.playlist;

import java.util.List;

public record PlaylistRequestDTO(String title, Long userId, byte[] imageData, List<Long> songIds) {
}
