package com.example.transport.ticket.feign;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class VehicleDTO {
    @Id
    private Long id;
    private String type;
    private int capacity;
}