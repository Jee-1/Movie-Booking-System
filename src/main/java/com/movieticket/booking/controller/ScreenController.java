package com.movieticket.booking.controller;

import com.movieticket.booking.dto.ScreenRequest;
import com.movieticket.booking.entity.Screen;
import com.movieticket.booking.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {

    @Autowired
    public ScreenService screenService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Screen create(@RequestBody ScreenRequest req){
        return screenService.create(req);
    }

    @GetMapping
    public List<Screen> getAll(){
        return screenService.getAll();
    }

    @GetMapping("/{id}")
    public Screen getById(@PathVariable Long id) {
        return screenService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        screenService.delete(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Screen update(@PathVariable Long id, @RequestBody ScreenRequest req){
        return screenService.update(id, req);
    }

}
