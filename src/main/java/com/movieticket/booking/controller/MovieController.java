package com.movieticket.booking.controller;

import com.movieticket.booking.dto.MovieRequest;
import com.movieticket.booking.entity.Movie;
import com.movieticket.booking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Movie create(@RequestBody MovieRequest movie){
        return movieService.create(movie);
    }

    @GetMapping
    public List<Movie> getAll(){
        return movieService.getAll();
    }

    @GetMapping("/{id}")
    public Movie getById(@PathVariable Long id){
        return movieService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Movie update(@PathVariable Long id, @RequestBody MovieRequest movie){
        return movieService.update(id, movie);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        movieService.delete(id);
    }

}
