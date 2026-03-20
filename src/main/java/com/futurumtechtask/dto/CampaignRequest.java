package com.futurumtechtask.dto;

import com.futurumtechtask.enums.Status;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CampaignRequest {

    @NotBlank
    private String name;

    @NotEmpty
    private List<String> keywords;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal bidAmount;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal campaignFund;

    @NotNull
    private Status status;

    private String town;

    @NotNull
    @Min(1)
    private Integer radius;
}
