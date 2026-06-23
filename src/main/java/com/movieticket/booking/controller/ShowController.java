package com.movieticket.booking.controller;

import com.movieticket.booking.dto.ShowRequest;
import com.movieticket.booking.entity.Show;
import com.movieticket.booking.service.ShowService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping
    public Show create(@RequestBody ShowRequest req) {
        return showService.create(req);
    }

    @GetMapping
    public List<Show> getAll() {
        return showService.getAll();
    }

    @GetMapping("/{id}")
    public Show getById(@PathVariable Long id) {
        return showService.getById(id);
    }

    @PutMapping("/{id}")
    public Show update(@PathVariable Long id, @RequestBody ShowRequest req) {
        return showService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        showService.delete(id);
    }
}
