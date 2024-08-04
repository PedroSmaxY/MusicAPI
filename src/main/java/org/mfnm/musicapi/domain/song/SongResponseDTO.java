package org.mfnm.musicapi.domain.song;

public record SongResponseDTO(Long id, String title, String artist, String album, String imageBase64) {
}
