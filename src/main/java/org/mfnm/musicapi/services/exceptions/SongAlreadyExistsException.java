package org.mfnm.musicapi.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class SongAlreadyExistsException extends ConflictException {
    public SongAlreadyExistsException(String message) {
        super(message);
    }
}
