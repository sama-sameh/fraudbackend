package com.fraudsystem.fraud.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuspectCountPerDayDTO {
    private LocalDate date;
    private Long count;
}
