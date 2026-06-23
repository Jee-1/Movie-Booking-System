package com.movieticket.booking.service;

import com.movieticket.booking.dto.ShowRequest;
import com.movieticket.booking.entity.Movie;
import com.movieticket.booking.entity.Screen;
import com.movieticket.booking.entity.Seat;
import com.movieticket.booking.entity.SeatBooking;
import com.movieticket.booking.entity.SeatStatus;
import com.movieticket.booking.entity.Show;
import com.movieticket.booking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final SeatBookingRepository seatBookingRepository;

    public ShowService(ShowRepository showRepository, MovieRepository movieRepository,
                       ScreenRepository screenRepository, SeatRepository seatRepository,
                       SeatBookingRepository seatBookingRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.screenRepository = screenRepository;
        this.seatRepository = seatRepository;
        this.seatBookingRepository = seatBookingRepository;
    }

    @Transactional
    public Show create(ShowRequest req) {
        Movie movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found: " + req.getMovieId()));
        Screen screen = screenRepository.findById(req.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found: " + req.getScreenId()));
        Show show = new Show();
        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(req.getShowTime());
        show.setBasePrice(req.getBasePrice());
        Show savedShow = showRepository.save(show);

        List<Seat> seats = seatRepository.findByScreenId(screen.getId());
        List<SeatBooking> seatBookings = new ArrayList<>();
        for (Seat seat : seats) {
            SeatBooking sb = new SeatBooking();
            sb.setShow(savedShow);
            sb.setSeat(seat);
            sb.setStatus(SeatStatus.AVAILABLE);
            seatBookings.add(sb);
        }
        seatBookingRepository.saveAll(seatBookings);

        return savedShow;
    }

    public List<Show> getAll() {
        return showRepository.findAll();
    }

    public Show getById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found: " + id));
    }

    @Transactional
    public Show update(Long id, ShowRequest req) {
        Show show = getById(id);
        Movie movie = movieRepository.findById(req.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found: " + req.getMovieId()));
        Screen screen = screenRepository.findById(req.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found: " + req.getScreenId()));

        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(req.getShowTime());
        show.setBasePrice(req.getBasePrice());
        return showRepository.save(show);
    }

    public void delete(Long id) {
        showRepository.deleteById(id);
    }
}
