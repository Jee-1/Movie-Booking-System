package com.movieticket.booking.service;

import com.movieticket.booking.dto.MovieRequest;
import com.movieticket.booking.entity.Movie;
import com.movieticket.booking.exception.ResourceNotFoundException;
import com.movieticket.booking.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie create(MovieRequest req){
        Movie movie = new Movie();
        movie.setTitle(req.getTitle());
        movie.setGenre(req.getGenre());
        movie.setLanguage(req.getLanguage());
        movie.setDurationInMinutes(req.getDurationMins());
        return movieRepository.save(movie);
    }

    public List<Movie> getAll(){
        return movieRepository.findAll();
    }

    public Movie getById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found! "+id));
    }

    public Movie update(Long id, MovieRequest req) {
        Movie movie = getById(id);
        movie.setTitle(req.getTitle());
        movie.setGenre(req.getGenre());
        movie.setLanguage(req.getLanguage());
        movie.setDurationInMinutes(req.getDurationMins());
        return movieRepository.save(movie);
    }

    public void delete(Long id){
        movieRepository.deleteById(id);
    }

}
