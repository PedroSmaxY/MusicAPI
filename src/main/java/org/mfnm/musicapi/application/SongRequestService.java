package org.mfnm.musicapi.application;

import org.mfnm.musicapi.adapters.SongRequestGateway;
import org.mfnm.musicapi.core.usecase.SongRequestUseCase;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongRequestService implements SongRequestUseCase {

    private final SongRequestGateway songRequestGateway;

    //@Autowired
    public SongRequestService(SongRequestGateway songGateway) {
        this.songRequestGateway = songGateway;
    }

    @Override
    public void requestSong(String songName) {
        this.songRequestGateway.requestSong(songName);
    }
}
