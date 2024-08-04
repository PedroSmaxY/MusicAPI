package org.mfnm.musicapi.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PlaylistNotFoundException extends ObjectNotFoundException {
    public PlaylistNotFoundException(String message) {
        super(message);
    }
}
