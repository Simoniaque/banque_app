package com.example.banque_app.Controller;

import com.example.banque_app.Model.Compte;
import com.example.banque_app.Repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/comptes")
public class WS_CompteController {
    @Autowired
    private CompteRepository compteRepository;

    @GetMapping
    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compte> getCompteById(@PathVariable Long id) {
        return compteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Compte createCompte(@RequestBody Compte compte) {
        return compteRepository.save(compte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compte> updateCompte(@PathVariable Long id, @RequestBody Compte compteDetails) {
        return compteRepository.findById(id)
                .map(compte -> {
                    compte.setNumero(compteDetails.getNumero());
                    compte.setSolde(compteDetails.getSolde());
                    return ResponseEntity.ok(compteRepository.save(compte));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCompte(@PathVariable Long id) {
        return compteRepository.findById(id)
                .map(compte -> {
                    compteRepository.delete(compte);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
