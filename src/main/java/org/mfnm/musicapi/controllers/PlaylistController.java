package org.mfnm.musicapi.controllers;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.mfnm.musicapi.domain.playlist.PlaylistRequestDTO;
import org.mfnm.musicapi.domain.playlist.PlaylistResponseDTO;
import org.mfnm.musicapi.domain.playlist.PlaylistUpdateDTO;
import org.mfnm.musicapi.service.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/playlists")
@AllArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponseDTO> getPlaylist(@PathVariable Long playlistId) {
        Playlist playlist = this.playlistService.findById(playlistId);

        PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getImageData() != null ? Base64.getEncoder().encodeToString(playlist.getImageData()) : null
        );

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPlaylist(
            @RequestParam String title,
            @RequestParam Long userId,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) List<Long> songIds) {

        try {
            byte[] imageData = (imageFile != null) ? imageFile.getBytes() : null;

            PlaylistRequestDTO playlistRequestDTO = new PlaylistRequestDTO(
                    title,
                    userId,
                    imageData,
                    songIds != null ? songIds : List.of() // Cria uma lista vazia se null
            );

            Playlist newPlaylist = this.playlistService.createPlaylist(playlistRequestDTO);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newPlaylist.getId())
                    .toUri();

            return ResponseEntity.created(uri).build();

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed.");
        }
    }

    @PostMapping("/{playlistId}/add-songs")
    public ResponseEntity<Playlist> addSongsToPlaylist(@PathVariable Long playlistId, @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        Playlist updatedPlaylist = this.playlistService.addSongsToPlaylist(playlistId, playlistRequestDTO.songIds());
        return ResponseEntity.ok().body(updatedPlaylist);
    }

    @PostMapping("/{playlistId}/remove-songs")
    public ResponseEntity<Playlist> removeSongsFromPlaylist(@PathVariable Long playlistId, @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        Playlist newPlaylist = this.playlistService
                .removeSongsFromPlaylist(playlistId, playlistRequestDTO.songIds());

        return ResponseEntity.ok().body(newPlaylist);
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<String> updatePlaylist(@PathVariable Long playlistId,
                                                 @RequestBody String title,
                                                 @RequestBody(required = false) MultipartFile imageFile) {

        try {
            byte[] imageData = (imageFile != null) ? imageFile.getBytes() : null;

            PlaylistUpdateDTO playlistUpdateDTO = new PlaylistUpdateDTO(
                    playlistId,
                    title,
                    imageData
            );

            this.playlistService.updatePlaylist(playlistId, playlistUpdateDTO);

            return ResponseEntity.noContent().build();

        } catch (IOException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed.");
        }
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        this.playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}
