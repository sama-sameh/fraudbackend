package com.fraudsystem.fraud.Service;

import com.fraudsystem.fraud.DTO.PredictionResponse;
import com.fraudsystem.fraud.DTO.TransactionDTO;
import com.fraudsystem.fraud.Entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class MLService {
    private final TransactionService transactionService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String ML_BASE = "http://localhost:8000";
    @Autowired
    public MLService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    public PredictionResponse predictXGBoost(TransactionDTO tx) {
        String url = ML_BASE + "/predict/xgboost";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TransactionDTO> entity = new HttpEntity<>(tx, headers);
        ResponseEntity<PredictionResponse> resp =
                restTemplate.postForEntity(url, entity, PredictionResponse.class);
        return resp.getBody();
    }
    public List<PredictionResponse> predict(int[] transactionsId){
        List<Transaction> tx = new ArrayList<Transaction>();
        for (int id : transactionsId) {
            tx.add(transactionService.findById(id));
        }
        List<TransactionDTO> dto = new ArrayList<TransactionDTO>();
        for (Transaction t : tx) {
            dto.add(transactionService.toFeatures(t));
        }
        List<PredictionResponse> response = new ArrayList<PredictionResponse>();
       for (TransactionDTO d : dto) {
           response.add(predictXGBoost(d));
       }
       return response;
    }
}
