package com.example.banque_app.Repository;

// Repository: CompteRepository

import com.example.banque_app.Model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte, Long> {
    // Interface pour les opérations CRUD sur les entités Compte
}
