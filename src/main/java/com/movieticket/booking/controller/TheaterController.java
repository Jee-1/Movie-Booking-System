package com.movieticket.booking.controller;

import com.movieticket.booking.dto.TheaterRequest;
import com.movieticket.booking.entity.Theater;
import com.movieticket.booking.service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Theater create(@RequestBody TheaterRequest theater){
        return theaterService.create(theater);
    }

    @GetMapping
    public List<Theater> getAll(){
        return theaterService.getAll();
    }

    @GetMapping("/{id}")
    public Theater getById(@PathVariable Long id){
        return theaterService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Theater delete(@PathVariable Long id, @RequestBody TheaterRequest theater){
        return theaterService.update(id, theater);
    }

}
