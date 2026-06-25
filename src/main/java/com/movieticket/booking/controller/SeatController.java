package com.movieticket.booking.controller;

import com.movieticket.booking.dto.SeatRequest;
import com.movieticket.booking.entity.Seat;
import com.movieticket.booking.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Seat create(@RequestBody SeatRequest seat){
        return seatService.create(seat);
    }

    @GetMapping
    public List<Seat> getAll(){
        return seatService.getAll();
    }

    @GetMapping("/{id}")
    public Seat getById(@PathVariable Long id){
        return seatService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Seat delete(@PathVariable Long id, @RequestBody SeatRequest seat){
        return seatService.update(id, seat);
    }

}
