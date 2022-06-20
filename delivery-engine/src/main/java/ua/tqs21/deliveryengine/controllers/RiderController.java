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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.services.RiderService;

@RestController
@RequestMapping("/courier")
public class RiderController {
    @Autowired
    private RiderService riderService;

    @Operation(summary = "Get All Rider users")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Rider List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Rider.class))))})
    @GetMapping("/")
    public List<Rider> findAllRiders() {
        return riderService.getRiders();
    }


    @Operation(summary = "Get Rider by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Rider with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rider.class))),
                    @ApiResponse(responseCode = "404", description = "Rider Not Found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public Rider findRiderById(
        @Parameter(name = "id", description = "Rider's Id")
        @PathVariable("id")  int id) {
        return riderService.getRiderById(id);   
    }


    @Operation(summary = "Register a new Rider")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Created Rider",
                    content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Rider.class))),
                    @ApiResponse(responseCode = "400", description = "Email already in use",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/")
    public Rider postRider(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Rider information", required = true, content = @Content(schema = @Schema(implementation = RiderPostDTO.class)))
        @RequestBody RiderPostDTO rider) {
        return riderService.saveRiderFromDto(rider);
    }

    @Operation(summary = "Update a Rider")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Rider updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rider.class))),
                    @ApiResponse(responseCode = "404", description = "Rider not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/")
    public Rider updateRider(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Rider information", required = true, content = @Content(schema = @Schema(implementation = RiderPostDTO.class)))
        @RequestBody RiderPostDTO rider) {
        return riderService.updateRider(rider);
    }

    @DeleteMapping("/{id}")
    public String deleteRider(
        @Parameter(name = "id", description = "Rider's Id")
        @PathVariable("id") int id) {
        return riderService.deleteRider(id);
    }

    @GetMapping("/listen")
    public void listen() {
        throw new NotYetImplementedException();
    }
}
