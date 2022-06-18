package ua.tqs21.deliveryengine.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs21.deliveryengine.dto.ServicePostDTO;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.services.ServiceService;

@RestController
@RequestMapping("/shop")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @PostMapping("/")
    public Service postService(@RequestBody ServicePostDTO service) throws URISyntaxException, ParseException, IOException {
        return serviceService.createServiceFromDTO(service);
    }

    @GetMapping("/")
    public List<Service> getServices() {
        return serviceService.getAll();
    }

    @GetMapping("/{id}")
    public Service getService(@PathVariable("id") int id) {
        return serviceService.getById(id);
    }

    @PutMapping("/{id}")
    public Service updateService(@PathVariable int id, @RequestBody ServicePostDTO service) {
        return serviceService.update(id, service);
    }
}
