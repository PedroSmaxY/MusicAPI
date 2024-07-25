package org.mfnm.musicapi.controllers;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.entity.Song;
import org.mfnm.musicapi.service.SongService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/song")
@AllArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/{title}")
    public ResponseEntity<List<Song>> getSong(@PathVariable String title) {
        List<Song> songs = this.songService.findByTitle(title);
        return ResponseEntity.ok().body(songs);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Song song = this.songService.findById(id);
        return ResponseEntity.ok().body(song);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadSong(@PathVariable Long id) {
        try {
            Song song = this.songService.findById(id);
            byte[] audioData = song.getAudioData();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("audio/mpeg"));
            headers.setContentDispositionFormData("attachment", song.getTitle() + ".mp3");

            InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/upload")
    @Validated
    public ResponseEntity<String> createSong(@RequestParam MultipartFile file,
                                             @RequestParam String title,
                                             @RequestParam String artist,
                                             @RequestParam(required = false) String albumTitle,
                                             @RequestParam(required = false) Long playlistId) {

        try {
            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setAlbumTitle(albumTitle);
            song.setAudioData(file.getBytes());

            Song createdSong = this.songService.create(song);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdSong.getId())
                    .toUri();

            return ResponseEntity.created(uri).body("File uploaded succefully");

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed.");
        }
    }

    @PutMapping("/update/{id}")
    @Validated
    public ResponseEntity<Song> updateSong(@PathVariable Long id, @Validated @RequestBody Song song) {
        if (!id.equals(song.getId())) {
            throw new IllegalArgumentException("ID in path and request body do not match.");
        }
        Song updatedSong = this.songService.update(song);
        return ResponseEntity.ok(updatedSong);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        this.songService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

