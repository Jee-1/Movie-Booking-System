package com.movieticket.booking.service;

import com.movieticket.booking.dto.ScreenRequest;
import com.movieticket.booking.entity.Screen;
import com.movieticket.booking.entity.Theater;
import com.movieticket.booking.repository.ScreenRepository;
import com.movieticket.booking.repository.TheaterRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private TheaterRepository theaterRepository;

    public Screen create(ScreenRequest req){
        Theater theater = theaterRepository.findById(req.getTheaterId())
                .orElseThrow(()->new RuntimeException());
        Screen screen = new Screen();
        screen.setTheater(theater);
        screen.setScreenName(req.getScreenName());
        screen.setTotalSeats(req.getTotalSeats());
        return screenRepository.save(screen);
    }

    public List<Screen> getAll(){
        return screenRepository.findAll();
    }

    public Screen getById(Long id){
        return screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
    }

    public void delete(Long id){
        screenRepository.deleteById(id);
    }

    public Screen update(Long id, ScreenRequest req){
        Screen screen = getById(id);
        Theater theater = theaterRepository.findById(req.getTheaterId())
                .orElseThrow(()->new RuntimeException());
        screen.setTheater(theater);
        screen.setScreenName(req.getScreenName());
        screen.setTotalSeats(req.getTotalSeats());
        return screenRepository.save(screen);
    }

}
