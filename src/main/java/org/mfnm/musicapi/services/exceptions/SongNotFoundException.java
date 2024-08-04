package org.mfnm.musicapi.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SongNotFoundException extends ObjectNotFoundException {
    public SongNotFoundException(String message) {
        super(message);
    }
}
