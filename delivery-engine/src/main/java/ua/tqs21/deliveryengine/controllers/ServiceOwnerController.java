package ua.tqs21.deliveryengine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.services.ServiceOwnerService;

@RestController
@RequestMapping("/owner")
public class ServiceOwnerController {

    @Autowired
    private ServiceOwnerService serviceOwnerService;

    @GetMapping("/")
    public List<ServiceOwner> findAllOwners() {
        return serviceOwnerService.getServiceOwners();
    }

    @PostMapping("/")
    public ServiceOwner postOwner(@RequestBody UserDTO userDTO) {
        return this.serviceOwnerService.saveOwnerFromUser(userDTO);
    }

    @GetMapping("/{id}")
    public ServiceOwner findAdminById(@PathVariable("id")  int id) {
        return serviceOwnerService.getServiceOwnerById(id);   
    }
}
