package com.fraudsystem.fraud.DTO;

import lombok.Data;

@Data
public class PredictionResponse {
    private Integer transaction_id;
    private String model;
    private Integer prediction;
    private Double probability;
    private String error;
}
