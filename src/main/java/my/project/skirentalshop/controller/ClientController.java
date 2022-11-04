package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Client;
import my.project.skirentalshop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // ----- show all -----
    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfClients", clientService.showAllClients());
        return "admin/client/clients";
    }

    // ----- add new -----
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("client", new Client());
        return "admin/client/clients";
    }

    @PostMapping
    public String create(@ModelAttribute("client") @Valid Client client,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "admin/client/clients";
        }
        clientService.addNewClientToDB(client);
        return "redirect:/admin/clients";
    }

    // ----- edit -----
    @GetMapping("/{clientId}")
    public String showOne(@PathVariable("clientId") Long clientId, Model model) {
        model.addAttribute("action", "update");
        model.addAttribute("client", clientService.showOneClientById(clientId));
        return "admin/client/clients";
    }

    @PatchMapping("/{clientId}")
    public String update(@PathVariable("clientId") Long clientId,
                         @ModelAttribute("client") @Valid Client updatedClient,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "update");
            return "admin/client/clients";
        }
        clientService.updateClientById(clientId, updatedClient);
        return "redirect:/admin/clients";
    }

    // ----- delete -----
    @DeleteMapping("/{clientId}")
    public String delete(@PathVariable("clientId") Long clientId) {
        clientService.deleteClientById(clientId);
        return "redirect:/admin/clients";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showAllBySearch(@RequestParam("search") String search,
                                  Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfClients", clientService.showClientsBySearch(search));
        model.addAttribute("search", search);
        return "admin/client/clients";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllByParameter(@RequestParam("parameter") String parameter,
                                     @RequestParam("sortDirection") String sortDirection,
                                     Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfClients", clientService.sortAllClientsByParameter(parameter, sortDirection));
        return "admin/client/clients";
    }
}