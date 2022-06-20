package ua.tqs21.deliveryengine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "Get All users")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))))})
    @GetMapping("/")
    public List<User> findAllUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Get User by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public User findUserById(
        @Parameter(name = "id", description = "User's Id")
        @PathVariable("id")  int id) {
        return userService.getUserById(id);   
    }

    @Operation(summary = "Update a User")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/")
    public User updateUser(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User information", required = true, content = @Content(schema = @Schema(implementation = UserDTO.class)))    
        @RequestBody UserDTO user) {
        return userService.updateUser(user);
    }

    @Operation(summary = "Delete a User by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User deleted",
                    content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Admin not found",
                    content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public String deleteUser(
        @Parameter(name = "id", description = "User's Id")
        @PathVariable("id") int id) {
        return userService.deleteUser(id);
    }
}
