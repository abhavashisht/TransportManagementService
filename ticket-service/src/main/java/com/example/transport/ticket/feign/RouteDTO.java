package com.example.transport.ticket.feign;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class RouteDTO {
    @Id
    private Long id;
    private String source;
    private String destination;
    private VehicleDTO vehicle;
}