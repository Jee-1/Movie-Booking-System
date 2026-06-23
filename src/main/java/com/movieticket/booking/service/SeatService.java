package com.movieticket.booking.service;

import com.movieticket.booking.dto.ScreenRequest;
import com.movieticket.booking.dto.SeatRequest;
import com.movieticket.booking.entity.Screen;
import com.movieticket.booking.entity.Seat;
import com.movieticket.booking.entity.Theater;
import com.movieticket.booking.exception.ResourceNotFoundException;
import com.movieticket.booking.repository.ScreenRepository;
import com.movieticket.booking.repository.SeatRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SeatService {

    private SeatRepository seatRepository;
    private ScreenRepository screenRepository;

    public Seat create(SeatRequest req){
        Screen screen = screenRepository.findById(req.getScreenId())
                .orElseThrow(()->new ResourceNotFoundException("Screen not found"));
        Seat seat = new Seat();
        seat.setSeatNumber(req.getSeatNumber());
        seat.setSeatType(req.getSeatType());
        seat.setScreen(screen);
        return seatRepository.save(seat);
    }

    public List<Seat> getAll(){
        return seatRepository.findAll();
    }

    public Seat getById(Long id){
        return seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
    }

    public void delete(Long id){
        seatRepository.deleteById(id);
    }

    public Seat update(Long id, SeatRequest req){
        Seat seat = getById(id);
        Screen screen = screenRepository.findById(req.getScreenId())
                .orElseThrow(()->new ResourceNotFoundException("Screen not found"));
        seat.setSeatNumber(req.getSeatNumber());
        seat.setSeatType(req.getSeatType());
        seat.setScreen(screen);
        return seatRepository.save(seat);
    }




}
