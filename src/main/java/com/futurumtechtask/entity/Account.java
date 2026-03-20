package com.futurumtechtask.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(
        name = "account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        }
)
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private BigDecimal emeraldBalance;

    @Column(nullable = false)
    private String username;

    private String password;
}
