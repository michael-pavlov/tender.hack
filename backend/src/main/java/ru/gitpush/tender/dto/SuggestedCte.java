package ru.gitpush.tender.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SuggestedCte {
    private String img;
    private String name;
    private String reason;
    private BigDecimal price;
    private Long cte;
}
