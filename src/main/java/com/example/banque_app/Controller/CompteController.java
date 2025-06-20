package com.example.banque_app.Controller;

import com.example.banque_app.Model.Compte;
import com.example.banque_app.Service.ClientService;
import com.example.banque_app.Service.CompteService;
import com.example.banque_app.Service.LogService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/comptes")
public class CompteController {
    private final CompteService compteService;
    private final ClientService clientService;
    private final LogService logService;

    public CompteController(CompteService compteService, ClientService clientService, LogService logService) {
        this.compteService = compteService;
        this.clientService = clientService;
        this.logService = logService;
    }

    @GetMapping
    public String getComptes(Model model) {
        model.addAttribute("comptes", compteService.getAllComptes());
        model.addAttribute("clients", clientService.getAllClients());
        return "ListeCompte";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("compte", new Compte());
        model.addAttribute("clients", clientService.getAllClients());
        return "CreerCompte";
    }

    @PostMapping("/create")
    public String createCompte(@ModelAttribute Compte compte) {
        compteService.saveCompte(compte);
        return "redirect:/comptes";
    }

    @GetMapping("/{id}")
    public String getCompte(@PathVariable Long id, Model model) {
        Optional<Compte> compte = compteService.getCompteById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            return "DetailCompte";
        } else {
            return "redirect:/comptes";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCompte(@PathVariable Long id) {
        compteService.deleteCompte(id);
        return "redirect:/comptes";
    }

    @PostMapping("/{id}/retrait")
    public String retirer(@PathVariable Long id, @RequestParam BigDecimal montant, RedirectAttributes redirectAttributes) {
        boolean success = compteService.retirer(id, montant);
        if (success) {
            logService.enregistrerOperation("Retrait effectué", id, null, montant, true);
            redirectAttributes.addFlashAttribute("success", "Retrait réussi.");
        } else {
            logService.enregistrerOperation("Échec du retrait", id, null, montant, false);
            redirectAttributes.addFlashAttribute("error", "Échec du retrait : solde ou découvert insuffisant.");
        }
        return "redirect:/comptes/" + id;
    }

    @PostMapping("/{id}/virementInterne")
    public String virementInterne(@PathVariable Long id,
                                  @RequestParam Long destinataireId,
                                  @RequestParam BigDecimal montant,
                                  RedirectAttributes redirectAttributes) {
        boolean success = compteService.virementInterne(id, destinataireId, montant);

        if (success) {
            logService.enregistrerOperation("Virement interne", id, destinataireId, montant, true);
            redirectAttributes.addFlashAttribute("success", "Virement interne effectué avec succès.");
        } else {
            logService.enregistrerOperation("Échec du virement interne", id, destinataireId, montant, false);
            redirectAttributes.addFlashAttribute("error", "Virement refusé : les comptes doivent appartenir au même client et le solde doit être suffisant.");
        }

        return "redirect:/comptes";
    }

    @PostMapping("/{id}/virementExterne")
    public String virementExterne(@PathVariable Long id,
                                  @RequestParam Long destinataireId,
                                  @RequestParam BigDecimal montant,
                                  @RequestParam(defaultValue = "false") boolean autorisationBanquier,
                                  RedirectAttributes redirectAttributes) {

        boolean success = compteService.virementExterne(id, destinataireId, montant, autorisationBanquier);

        if (success) {
            logService.enregistrerOperation("Virement entre clients", id, destinataireId, montant, true);
            redirectAttributes.addFlashAttribute("success", "Virement externe effectué avec succès.");
        } else {
            logService.enregistrerOperation("Échec du virement externe", id, destinataireId, montant, false);
            redirectAttributes.addFlashAttribute("error", "Virement refusé : client non-banquier ou autorisation manquante.");
        }

        return "redirect:/comptes";
    }

    @PostMapping("/{id}/depot")
    public String deposer(@PathVariable Long id, @RequestParam BigDecimal montant, RedirectAttributes redirectAttributes) {
        boolean success = compteService.deposer(id, montant);
        if (success) {
            logService.enregistrerOperation("Dépôt effectué", id, null, montant, true);
            redirectAttributes.addFlashAttribute("success", "Dépôt réussi.");
        } else {
            logService.enregistrerOperation("Échec du dépôt", id, null, montant, false);
            redirectAttributes.addFlashAttribute("error", "Échec du dépôt.");
        }
        return "redirect:/comptes/" + id;
    }

    @GetMapping("/{id}/virementInterne/form")
    public String afficherFormulaireVirementInterne(@PathVariable Long id, Model model) {
        Optional<Compte> compte = compteService.getCompteById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            model.addAttribute("comptes", compteService.getAllComptes());
            return "VirementInterne";
        }
        return "redirect:/comptes";
    }

    @GetMapping("/{id}/virementExterne/form")
    public String afficherFormulaireVirementExterne(@PathVariable Long id, Model model) {
        Optional<Compte> compte = compteService.getCompteById(id);
        if (compte.isPresent()) {
            model.addAttribute("compte", compte.get());
            model.addAttribute("comptesExterne", compteService.getAllComptes());
            return "VirementExterne";
        }
        return "redirect:/comptes";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
