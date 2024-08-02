package org.mfnm.musicapi.domain.song;

public record SongRequestDTO(String title, String artist, String album, byte[] audio_data, byte[] image_data) {
}
