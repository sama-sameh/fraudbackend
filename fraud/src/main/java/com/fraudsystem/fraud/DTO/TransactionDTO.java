package com.fraudsystem.fraud.DTO;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class TransactionDTO {
    @JsonProperty("transaction_id")
    private int transaction_id;

    @JsonProperty("Transaction_Amount")
    private double Transaction_Amount;

    @JsonProperty("Transaction_Type")
    private String Transaction_Type;

    @JsonProperty("Account_Balance")
    private double Account_Balance;

    @JsonProperty("Device_Type")
    private String Device_Type;

    @JsonProperty("Location")
    private String Location;

    @JsonProperty("Merchant_Category")
    private String Merchant_Category;

    @JsonProperty("IP_Address_Flag")
    private int IP_Address_Flag;

    @JsonProperty("Previous_Fraudulent_Activity")
    private int Previous_Fraudulent_Activity;

    @JsonProperty("Daily_Transaction_Count")
    private int Daily_Transaction_Count;

    @JsonProperty("Avg_Transaction_Amount_7d")
    private double Avg_Transaction_Amount_7d;

    @JsonProperty("Failed_Transaction_Count_7d")
    private int Failed_Transaction_Count_7d;

    @JsonProperty("Card_Type")
    private String Card_Type;

    @JsonProperty("Card_Age")
    private int Card_Age;

    @JsonProperty("Transaction_Distance")
    private double Transaction_Distance;

    @JsonProperty("Authentication_Method")
    private String Authentication_Method;

    @JsonProperty("Is_Weekend")
    private int Is_Weekend;

    @JsonProperty("Minute")
    private int Minute;

    @JsonProperty("Hour")
    private int Hour;

    @JsonProperty("Day")
    private int Day;

    @JsonProperty("Month")
    private int Month;

    @JsonProperty("Amount_Balance_Ratio")
    private double Amount_Balance_Ratio;

    @JsonProperty("high_transaction_amount")
    private int high_transaction_amount;

    @JsonProperty("Transactions_per_Hour")
    private int Transactions_per_Hour;

    @JsonProperty("Avg_Transactions_per_Hour_7d")
    private double Avg_Transactions_per_Hour_7d;

    @JsonProperty("Hour_Activity_Ratio")
    private double Hour_Activity_Ratio;
}