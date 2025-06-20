package com.example.banque_app.Service;

import com.example.banque_app.Model.OperationLog;
import com.example.banque_app.Repository.OperationLogRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class LogService {

    private final OperationLogRepository logRepository;

    public LogService(OperationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void enregistrerOperation(String typeOperation, Long idSource, Long idDestinataire, BigDecimal montant, boolean success) {
        String message = String.format(
                "%s - [%s] %s | Source: %s | Destinataire: %s | Montant: %.2f",
                LocalDateTime.now(),
                success ? "SUCCÈS" : "ÉCHEC",
                typeOperation,
                idSource != null ? idSource : "N/A",
                idDestinataire != null ? idDestinataire : "N/A",
                montant
        );

        System.out.println(message);

        OperationLog log = new OperationLog();
        log.setTypeOperation(typeOperation);
        log.setDateHeure(LocalDateTime.now());
        log.setCompteSourceId(idSource);
        log.setCompteDestinataireId(idDestinataire);
        log.setMontant(montant);
        log.setSuccess(success);

        logRepository.save(log);
    }
}
