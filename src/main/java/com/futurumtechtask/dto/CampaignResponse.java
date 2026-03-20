package com.futurumtechtask.dto;

import com.futurumtechtask.enums.Status;

import java.math.BigDecimal;
import java.util.List;

public record CampaignResponse(
        Long id,
        String name,
        List<String> keywords,
        BigDecimal bidAmount,
        BigDecimal campaignFund,
        Status status,
        String town,
        Integer radius
) {}