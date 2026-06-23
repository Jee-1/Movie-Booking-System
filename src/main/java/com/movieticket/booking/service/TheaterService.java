package com.movieticket.booking.service;

import com.movieticket.booking.dto.TheaterRequest;
import com.movieticket.booking.entity.Theater;
import com.movieticket.booking.exception.ResourceNotFoundException;
import com.movieticket.booking.repository.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public Theater create(TheaterRequest req) {
        Theater theater = new Theater();
        theater.setName(req.getName());
        theater.setLocation(req.getLocation());
        return theaterRepository.save(theater);
    }

    public List<Theater> getAll(){
        return theaterRepository.findAll();
    }

    public Theater getById(Long id){
        return theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found! "+id));
    }

    public Theater update(Long id, TheaterRequest req) {
        Theater theater = getById(id);
        theater.setName(req.getName());
        theater.setLocation(req.getLocation());
        return theaterRepository.save(theater);
    }

    public void delete(Long id){
        theaterRepository.deleteById(id);
    }

}
