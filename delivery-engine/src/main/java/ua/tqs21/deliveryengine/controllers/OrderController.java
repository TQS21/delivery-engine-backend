package ua.tqs21.deliveryengine.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.json.simple.parser.ParseException;
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

import ua.tqs21.deliveryengine.dto.OrderPostDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.services.OrderService;

@RestController
@RequestMapping("/delivery")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @Operation(summary = "Create a new Order")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Created Order",
                    content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Order.class)))
    })
    @PostMapping("/")
    public Order postOrderFromDto(
        
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order information", required = true, content = @Content(schema = @Schema(implementation = OrderPostDTO.class)))
        @RequestBody OrderPostDTO order) throws URISyntaxException, ParseException, IOException {
        return orderService.createOrderFromDTO(order);
    }

    @Operation(summary = "Get All Orders")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Order List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Order.class))))})
    @GetMapping("/")
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Get Order by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Order with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order Not Found",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public Order getOrder(
        @Parameter(name = "id", description = "Order's Id")
        @PathVariable int id) {
        return this.orderService.getOrderById(id);
    }

    @Operation(summary = "Get All Active Orders")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Active Order List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Order.class))))})
    @GetMapping("/active")
    public List<Order> getActiveOrders() {
        return this.orderService.getActiveOrders();
    }


    @Operation(summary = "Get All Nearby Orders (30km Range)")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Nearby Order List",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Order.class)))),
                            @ApiResponse(responseCode = "400", description = "Invalid Address",
                            content = @Content(mediaType = "application/json"))
                })
    @PostMapping("/nearby")
    public List<Order> getNearbyOrders(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Current Rider Address", required = true, content = @Content(schema = @Schema(implementation = Address.class)))
        @RequestBody Address source) throws Exception {
        return orderService.getNearbyOrders(source);
    }


    @Operation(summary = "Accept Order of Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Order with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order Not Found",
                    content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid Order status transition",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    @PostMapping("/{id}/accept")
    public Order acceptOrder(
        @Parameter(name = "id", description = "Order's Id")
        @PathVariable("id")  int id) {
        return this.orderService.accept(id);
    }


    @Operation(summary = "Collect Order of Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Order with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order Not Found",
                    content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid Order status transition",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    @PostMapping("/{id}/collect")
    public Order collectOrder(
        @Parameter(name = "id", description = "Order's Id")
        @PathVariable("id")  int id) {
        return this.orderService.collect(id);
    }


    @Operation(summary = "Deliver Order of Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Order with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order Not Found",
                    content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid Order status transition",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    @PostMapping("/{id}/deliver")
    public Order deliverOrder(
        @Parameter(name = "id", description = "Order's Id")
        @PathVariable("id")  int id) {
        return this.orderService.deliver(id);
    }


    @Operation(summary = "Cancel Order of Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Order with Id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class))),
                    @ApiResponse(responseCode = "404", description = "Order Not Found",
                    content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Invalid Order status transition",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order.class)))
    })
    @PostMapping("/{id}/cancel")
    public Order cancelOrder(
        @Parameter(name = "id", description = "Order's Id")
        @PathVariable("id")  int id) {
        return this.orderService.cancel(id);
    }
}
