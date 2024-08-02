package org.mfnm.musicapi.domain.playlist;


public record PlaylistUpdateDTO(Long id, String title, byte[] image_data) {
}
