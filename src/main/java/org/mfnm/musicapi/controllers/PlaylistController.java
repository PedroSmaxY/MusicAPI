package org.mfnm.musicapi.controllers;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.mfnm.musicapi.domain.playlist.PlaylistRequestDTO;
import org.mfnm.musicapi.domain.playlist.PlaylistResponseDTO;
import org.mfnm.musicapi.domain.playlist.PlaylistUpdateDTO;
import org.mfnm.musicapi.services.PlaylistService;
import org.mfnm.musicapi.services.exceptions.BusinessLogicException;
import org.mfnm.musicapi.services.exceptions.FileProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping
    public ResponseEntity<Page<PlaylistResponseDTO>> getAllPlaylists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Playlist> playlistsPage = this.playlistService.findAllPlaylists(pageable);

        Page<PlaylistResponseDTO> responsePage = playlistsPage.map(playlist -> new PlaylistResponseDTO(
                playlist.getId(),
                playlist.getTitle(),
                playlist.getImageData() != null ? Base64.getEncoder().encodeToString(playlist.getImageData()) : null
        ));

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Playlist>> search(@NonNull @RequestParam String query) {
        List<Playlist> playlists = this.playlistService.search(query);
        return ResponseEntity.ok(playlists);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPlaylist(@RequestParam String title,
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
            throw new FileProcessingException("Error processing the file upload");
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

    @PutMapping("/update/{playlistId}")
    public ResponseEntity<String> updatePlaylist(@PathVariable Long playlistId,
                                                 @RequestParam String title,
                                                 @RequestParam(required = false) MultipartFile imageFile) {

        try {
            byte[] imageData = (imageFile != null) ? imageFile.getBytes() : null;

            PlaylistUpdateDTO playlistUpdateDTO = new PlaylistUpdateDTO(
                    playlistId,
                    title,
                    imageData
            );

            if (!playlistId.equals(playlistUpdateDTO.id())) {
                throw new BusinessLogicException("ID in path and request body do not match.");
            }

            this.playlistService.updatePlaylist(playlistId, playlistUpdateDTO);

            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            throw new FileProcessingException("Error processing the file upload");
        } catch (Exception e) {
            throw new FileProcessingException("Error updating the playlist");
        }
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        this.playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}
