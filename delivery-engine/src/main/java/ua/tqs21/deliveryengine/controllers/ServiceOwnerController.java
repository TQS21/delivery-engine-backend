package ua.tqs21.deliveryengine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.services.ServiceOwnerService;

@RestController
@RequestMapping("/owner")
public class ServiceOwnerController {

    @Autowired
    private ServiceOwnerService serviceOwnerService;

    @Operation(summary = "Get All Shop Owners users")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Shop Owners List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ServiceOwner.class))))})
    @GetMapping("/")
    public List<ServiceOwner> findAllOwners() {
        return serviceOwnerService.getServiceOwners();
    }

    @Operation(summary = "Register a new Shop Owner")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Created Shop Owner",
                    content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ServiceOwner.class))),
                    @ApiResponse(responseCode = "400", description = "Email already in use",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/")
    public ServiceOwner postOwner(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User information", required = true, content = @Content(schema = @Schema(implementation = UserDTO.class)))
        @RequestBody UserDTO userDTO) {
        return this.serviceOwnerService.saveOwnerFromUser(userDTO);
    }

    @Operation(summary = "Get Shop Owner by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Admin with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceOwner.class))),
                    @ApiResponse(responseCode = "404", description = "Admin Not Found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ServiceOwner findAdminById(
        @Parameter(name = "id", description = "Shop Owner's Id")
        @PathVariable("id")  int id) {
        return serviceOwnerService.getServiceOwnerById(id);   
    }
}
