package org.mfnm.musicapi.controllers;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.song.SongRequestDTO;
import org.mfnm.musicapi.domain.song.SongUpdateDTO;
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
    public ResponseEntity<String> createSong(@RequestParam MultipartFile audioFile,
                                             @RequestParam(required = false) MultipartFile imageFile,
                                             @RequestParam String title,
                                             @RequestParam String artist,
                                             @RequestParam(required = false) String albumTitle) {

        try {
            if (audioFile.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Audio file is required.");
            }

            byte[] audioData = audioFile.getBytes();
            byte[] imageData = (imageFile != null) ? imageFile.getBytes() : null;

            SongRequestDTO songRequestDTO = new SongRequestDTO(
                    title,
                    artist,
                    albumTitle,
                    audioData,
                    imageData
            );

            Song createdSong = songService.create(songRequestDTO);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdSong.getId())
                    .toUri();

            return ResponseEntity.created(uri).body("File uploaded successfully");

        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed.");
        }
    }

    @PutMapping("/update/{id}")
    @Validated
    public ResponseEntity<String> updateSong(@PathVariable Long id,
                                             @RequestParam String title,
                                             @RequestParam String artist,
                                             @RequestParam(required = false) String albumTitle,
                                             @RequestParam(required = false) MultipartFile image) {

        try {
            byte[] imageData = (image != null) ? image.getBytes() : null;
            SongUpdateDTO songUpdateDTO = new SongUpdateDTO(
                    id,
                    title,
                    artist,
                    albumTitle,
                    imageData
            );

            if (!id.equals(songUpdateDTO.id())) {
                throw new IllegalArgumentException("ID in path and request body do not match.");
            }

            Song updatedSong = songService.updateSong(songUpdateDTO);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        this.songService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

