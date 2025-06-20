package com.example.banque_app.Service;

import com.example.banque_app.Model.Compte;
import com.example.banque_app.Repository.CompteRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CompteService {
    private final CompteRepository compteRepository;

    public CompteService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    public Iterable<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    public Optional<Compte> getCompteById(Long id) {
        return compteRepository.findById(id);
    }

    public Compte saveCompte(Compte compte) {
        if (compte.getDecouvertAutorise() == null) {
            compte.setDecouvertAutorise(BigDecimal.ZERO);
        }
        return compteRepository.save(compte);
    }

    public void deleteCompte(Long id) {
        compteRepository.deleteById(id);
    }

    public boolean retirer(Long id, BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) return false;
        Optional<Compte> compteOpt = compteRepository.findById(id);
        if (compteOpt.isPresent()) {
            Compte compte = compteOpt.get();
            BigDecimal soldeApresRetrait = compte.getSolde().subtract(montant);
            if (soldeApresRetrait.compareTo(compte.getDecouvertAutorise().negate()) >= 0) {
                compte.setSolde(soldeApresRetrait);
                compteRepository.save(compte);
                return true;
            }
        }
        return false;
    }

    public boolean deposer(Long id, BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) return false;
        Optional<Compte> compteOpt = compteRepository.findById(id);
        if (compteOpt.isPresent()) {
            Compte compte = compteOpt.get();
            compte.setSolde(compte.getSolde().add(montant));
            compteRepository.save(compte);
            return true;
        }
        return false;
    }

    public boolean virementInterne(Long idSource, Long idDestinataire, BigDecimal montant) {
        if (idSource.equals(idDestinataire) || montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        Optional<Compte> sourceOpt = compteRepository.findById(idSource);
        Optional<Compte> destOpt = compteRepository.findById(idDestinataire);

        if (sourceOpt.isPresent() && destOpt.isPresent()) {
            Compte source = sourceOpt.get();
            Compte dest = destOpt.get();

            // Vérifie que le client est le même
            if (source.getClient().getId().equals(dest.getClient().getId())) {
                if (retirer(idSource, montant)) {
                    return deposer(idDestinataire, montant);
                }
            }
        }

        return false;
    }

    public boolean virementExterne(Long idSource, Long idDestinataire, BigDecimal montant, boolean autorisationBanquier) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) return false;

        Optional<Compte> sourceOpt = compteRepository.findById(idSource);
        Optional<Compte> destOpt = compteRepository.findById(idDestinataire);

        if (sourceOpt.isPresent() && destOpt.isPresent()) {
            Compte source = sourceOpt.get();
            Compte dest = destOpt.get();

            // Vérification si le client source est un banquier
            if (source.getClient() != null && source.getClient().isEstBanquier() && autorisationBanquier) {
                if (retirer(idSource, montant)) {
                    return deposer(idDestinataire, montant);
                }
            }
        }

        return false;
    }
}