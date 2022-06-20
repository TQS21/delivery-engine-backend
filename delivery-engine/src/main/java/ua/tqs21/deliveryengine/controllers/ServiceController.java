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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import ua.tqs21.deliveryengine.dto.ServicePostDTO;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.services.ServiceService;

@RestController
@RequestMapping("/shop")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @Operation(summary = "Register a new Shop")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Created Shop",
                    content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Service.class))),
                    @ApiResponse(responseCode = "400", description = "Email already in use",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/")
    public Service postService(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User information", required = true, content = @Content(schema = @Schema(implementation = ServicePostDTO.class)))
        @RequestBody ServicePostDTO service) throws URISyntaxException, ParseException, IOException {
        return serviceService.createServiceFromDTO(service);
    }

    @Operation(summary = "Get All Shops")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Shop List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Service.class))))})
    @GetMapping("/")
    public List<Service> getServices() {
        return serviceService.getAll();
    }

    @Operation(summary = "Get Shop by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Shop with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Service.class))),
                    @ApiResponse(responseCode = "404", description = "Shop Not Found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public Service getService(
        @Parameter(name = "id", description = "Admin's Id")
        @PathVariable("id") int id) {
        return serviceService.getById(id);
    }

    @PutMapping("/{id}")
    public Service updateService(@PathVariable int id, @RequestBody ServicePostDTO service) {
        return serviceService.update(id, service);
    }
}
