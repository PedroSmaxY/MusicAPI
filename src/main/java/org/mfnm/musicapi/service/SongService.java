package org.mfnm.musicapi.service;

import lombok.AllArgsConstructor;
import org.mfnm.musicapi.repository.SongRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SongService {

    private final SongRepository songRepository;
    /*
        must be implemented
    */
}
