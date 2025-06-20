package com.example.banque_app.Controller;


import com.example.banque_app.Model.Client;
import com.example.banque_app.Service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String getClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "ListeClient";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        return "CreerClient";
    }

    @PostMapping("/create")
    public String createClient(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return "redirect:/clients";
    }

    @GetMapping("/{id}")
    public String getClient(@PathVariable Long id, Model model) {
        Optional<Client> client = clientService.getClientById(id);
        if (client.isPresent()) {
            model.addAttribute("client", client.get());
            return "DetailClient";
        } else {
            return "redirect:/clients";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "redirect:/clients";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
