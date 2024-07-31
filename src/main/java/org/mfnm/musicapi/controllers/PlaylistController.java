package org.mfnm.musicapi.controllers;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.playlist.Playlist;
import org.mfnm.musicapi.domain.playlist.PlaylistRequestDTO;
import org.mfnm.musicapi.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/playlists")
@AllArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;

    @GetMapping("/{playlistId}")
    public ResponseEntity<Playlist> getPlaylist(@PathVariable Long playlistId) {
        Playlist playlist = this.playlistService.findById(playlistId);
        return ResponseEntity.ok().body(playlist);
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylistWithSongs(@RequestBody PlaylistRequestDTO playlistRequestDTO) {
        Playlist newPlaylist = this.playlistService.createPlaylistWithSongs(
                playlistRequestDTO.title(),
                playlistRequestDTO.userId(),
                playlistRequestDTO.songIds()
        );

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPlaylist.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/empty")
    public ResponseEntity<Playlist> emptyPlaylist(@RequestBody PlaylistRequestDTO playlistRequestDTO) {
        Playlist newPlaylist = this.playlistService.createPlaylistEmpty(
                playlistRequestDTO.title(),
                playlistRequestDTO.userId()
        );

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPlaylist.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{playlistId}/add-songs")
    public ResponseEntity<Playlist> addSongsToPlaylist(@PathVariable Long playlistId, @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        Playlist newPlaylist = this.playlistService
                .addSongsToPlaylist(playlistId, playlistRequestDTO.songIds());

        return ResponseEntity.ok().body(newPlaylist);
    }

    @PostMapping("/{playlistId}/remove-songs")
    public ResponseEntity<Playlist> removeSongsFromPlaylist(@PathVariable Long playlistId, @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        Playlist newPlaylist = this.playlistService
                .removeSongsFromPlaylist(playlistId, playlistRequestDTO.songIds());

        return ResponseEntity.ok().body(newPlaylist);
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<Void> updatePlaylist(@PathVariable Long playlistId, @RequestBody PlaylistRequestDTO playlistRequestDTO) {
        Playlist playlist = this.playlistService
                .updatePlaylist(playlistId, playlistRequestDTO);

        return ResponseEntity.noContent().build();
    }
}
