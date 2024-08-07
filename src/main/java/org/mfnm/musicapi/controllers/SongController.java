package org.mfnm.musicapi.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mfnm.musicapi.domain.song.Song;
import org.mfnm.musicapi.domain.song.SongRequestDTO;
import org.mfnm.musicapi.domain.song.SongResponseDTO;
import org.mfnm.musicapi.domain.song.SongUpdateDTO;
import org.mfnm.musicapi.services.SongService;
import org.mfnm.musicapi.services.exceptions.BusinessLogicException;
import org.mfnm.musicapi.services.exceptions.FileProcessingException;
import org.mfnm.musicapi.services.exceptions.SongNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/songs")
@AllArgsConstructor
public class SongController {

    private final SongService songService;

    @GetMapping("/{title}")
    public ResponseEntity<List<SongResponseDTO>> getSongsByTitle(@PathVariable String title) {
        List<Song> songs = songService.findByTitle(title);
        List<SongResponseDTO> responseDTOs = songs.stream().map(song -> new SongResponseDTO(
                song.getId(),
                song.getTitle(),
                song.getArtist(),
                song.getAlbumTitle(),
                song.getImageData() != null ? Base64.getEncoder().encodeToString(song.getImageData()) : null
        )).collect(Collectors.toList());

        return ResponseEntity.ok().body(responseDTOs);
    }

    @GetMapping
    public ResponseEntity<Page<SongResponseDTO>> getAllSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Song> songsPage = this.songService.findAllSongs(pageable);

        Page<SongResponseDTO> responsePage = songsPage.map(song -> new SongResponseDTO(
                song.getId(),
                song.getTitle(),
                song.getArtist(),
                song.getAlbumTitle(),
                song.getImageData() != null ? Base64.getEncoder().encodeToString(song.getImageData()) : null
        ));

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Song>> search(@NonNull @RequestParam String query) {
        List<Song> songs = this.songService.search(query);
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SongResponseDTO> getSongById(@PathVariable Long id) {
        Song song = this.songService.findById(id);
        SongResponseDTO songResponseDTO = new SongResponseDTO(
                song.getId(),
                song.getTitle(),
                song.getArtist(),
                song.getAlbumTitle(),
                song.getImageData() != null ? Base64.getEncoder().encodeToString(song.getImageData()) : null
        );
        return ResponseEntity.ok().body(songResponseDTO);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadSong(@PathVariable Long id) {
        try {
            return songService.getSongResourceResponse(id, true);
        } catch (SongNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new FileProcessingException("Error processing the requested song");
        }
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<Resource> streamSong(@PathVariable Long id) {
        try {
            return songService.getSongResourceResponse(id, false);
        } catch (SongNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new FileProcessingException("Error processing the requested song");
        }
    }

    @PostMapping("/upload")
    @Validated
    public ResponseEntity<String> createSong(@RequestParam @NotNull MultipartFile audioFile,
                                             @RequestParam(required = false) MultipartFile imageFile,
                                             @RequestParam @NotBlank String title,
                                             @RequestParam @NotBlank String artist,
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
            throw new FileProcessingException("Error processing the file upload");
        }
    }

    @PutMapping("/update/{id}")
    @Validated
    public ResponseEntity<String> updateSong(@PathVariable Long id,
                                             @RequestParam String title,
                                             @RequestParam String artist,
                                             @RequestParam(required = false) String albumTitle,
                                             @RequestParam(required = false) MultipartFile imageFile) {

        try {
            byte[] imageData = (imageFile != null) ? imageFile.getBytes() : null;
            SongUpdateDTO songUpdateDTO = new SongUpdateDTO(
                    id,
                    title,
                    artist,
                    albumTitle,
                    imageData
            );

            if (!id.equals(songUpdateDTO.id())) {
                throw new BusinessLogicException("ID in path and request body do not match.");
            }

            Song updatedSong = songService.updateSong(songUpdateDTO);
            return ResponseEntity.noContent().build();

        } catch (IOException e) {
            throw new FileProcessingException("Error processing the file upload");
        } catch (Exception e) {
            throw new FileProcessingException("Error updating the song");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        this.songService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

