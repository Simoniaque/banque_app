package com.example.banque_app.Repository;

// Repository: ClientRepository

import com.example.banque_app.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // Interface pour les opérations CRUD sur les entités Client
}
