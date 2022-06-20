package ua.tqs21.deliveryengine.controllers;

import java.util.List;

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
import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.services.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Operation(summary = "Get All ADMIN users")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Admin List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Admin.class))))})
    @GetMapping("/")
    public List<Admin> findAllAdmins() {
        return adminService.getAdmins();
    }

    @Operation(summary = "Register a new Admin")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Created Admin",
                    content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Admin.class))),
                    @ApiResponse(responseCode = "400", description = "Email already in use",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/")
    public Admin postAdmin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User information", required = true, content = @Content(schema = @Schema(implementation = UserDTO.class)))
        @RequestBody UserDTO userDTO
    ) {
        return this.adminService.saveAdminFromUser(userDTO);
    }


    @Operation(summary = "Get Admin by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Admin with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Admin.class))),
                    @ApiResponse(responseCode = "404", description = "Admin Not Found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public Admin findAdminById(
        @Parameter(name = "id", description = "Admin's Id")
        @PathVariable("id")  int id
    ) {
        return adminService.getAdminById(id);   
    }


    @Operation(summary = "Update an Admin")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Admin updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Admin.class))),
                    @ApiResponse(responseCode = "404", description = "Admin not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/")
    public Admin updateAdmin(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User information", required = true, content = @Content(schema = @Schema(implementation = UserDTO.class)))
        @RequestBody UserDTO admin
    ) {
        return adminService.updateAdmin(admin);
    }


    @Operation(summary = "Delete an Admin by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Admin deleted",
                    content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Admin not found",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public String deleteAdmin(
        @Parameter(name = "id", description = "Admin's Id")
        @PathVariable("id") int id) {
        return adminService.deleteAdmin(id);
    }
}
