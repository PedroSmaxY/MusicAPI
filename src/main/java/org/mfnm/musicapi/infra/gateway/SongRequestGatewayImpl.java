package org.mfnm.musicapi.infra.gateway;

import org.mfnm.musicapi.adapters.SongRequestGateway;
import org.springframework.stereotype.Component;

@Component
public class SongRequestGatewayImpl implements SongRequestGateway {

    @Override
    public void requestSong(String song) {
        // Implementação do método para requisitar uma música
        System.out.println("Requisitando a música: " + song);
    }
}
