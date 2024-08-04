package org.mfnm.musicapi.domain.song;

public record SongUpdateDTO(Long id, String title, String artist, String album, byte[] image_data) {
}
