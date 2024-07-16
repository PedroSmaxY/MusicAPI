package org.mfnm.musicapi.infra.rest;

import org.mfnm.musicapi.core.entity.Song;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/song")
public class SongController {
    @GetMapping
    public List<Song> Listar(){
        List<Song> songslist = new ArrayList<Song>(1);

        Calendar calendar = Calendar.getInstance();

        Date currentDate = calendar.getTime();
        Song cancao = new Song(0, "Carry On WayWard Son", "Kansas", "CarryOn", currentDate);
        songslist.add(cancao);

        return  songslist;
    }
}
