/*
package com.example.banque_app.Controller;


import com.example.banque_app.Model.Compte;
import com.example.banque_app.Repository.ClientRepository;
import com.example.banque_app.Service.ClientService;
import com.example.banque_app.Service.CompteService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
@RequestMapping("/comptes")
public class CompteController {
    private final CompteService compteService;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    public CompteController(CompteService compteService, ClientRepository clientRepository, ClientService clientService) {
        this.compteService = compteService;
        this.clientRepository = clientRepository;
        this.clientService = clientService;
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

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
*/