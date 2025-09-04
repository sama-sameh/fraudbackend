package com.fraudsystem.fraud.Controller;

import com.fraudsystem.fraud.DTO.PredictionResponse;
import com.fraudsystem.fraud.DTO.TransactionDTO;
import com.fraudsystem.fraud.Service.MLService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prediction")
@CrossOrigin(origins = "http://localhost:4200")
public class PredictionController {
    private final MLService mlClient;
    public PredictionController(MLService mlClient) {
        this.mlClient = mlClient;
    }
    @PostMapping("/xgboost")
    public List<PredictionResponse> predictNaive(@RequestBody int[] transactionsId) {
        return mlClient.predict(transactionsId);
    }
}
