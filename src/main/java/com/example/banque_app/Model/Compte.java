package com.example.banque_app.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    private BigDecimal solde;

    private BigDecimal decouvertAutorise = BigDecimal.ZERO;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}