package ua.tqs21.deliveryengine.controllers;

import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.services.RiderService;

@RestController
@RequestMapping("/courier")
public class RiderController {
    @Autowired
    private RiderService riderService;

    @GetMapping("/")
    public List<Rider> findAllRiders() {
        return riderService.getRiders();
    }

    @GetMapping("/{id}")
    public Rider findRiderById(@PathVariable("id")  int id) {
        return riderService.getRiderById(id);   
    }

    @PostMapping("/")
    public Rider postRider(@RequestBody RiderPostDTO rider) {
        return riderService.saveRiderFromDto(rider);
    }

    @PutMapping("/")
    public Rider updateRider(@RequestBody RiderPostDTO rider) {
        return riderService.updateRider(rider);
    }

    @DeleteMapping("/{id}")
    public String deleteRider(@PathVariable("id") int id) {
        return riderService.deleteRider(id);
    }

    @GetMapping("/listen")
    public void listen() {
        throw new NotYetImplementedException();
    }
}
