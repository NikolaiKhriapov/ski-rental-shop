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
@RequestMapping("/admin/info-clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllClients(Model model) {
        model.addAttribute("allClients", clientService.showAllClients());
        return "client/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewClient(Model model) {
        model.addAttribute("newClient", new Client());
        return "client/add_new";
    }

    @PostMapping()
    public String addNewClientToDB(@ModelAttribute("newClient") @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/add_new";
        }
        clientService.addNewClientToDB(client);
        return "redirect:/admin/info-clients";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneClient(@PathVariable("id") Long id, Model model) {
        model.addAttribute("clientToUpdate", clientService.showOneClientById(id));
        return "client/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateClient(@PathVariable("id") Long id,
                               @ModelAttribute("clientToUpdate") @Valid Client updatedClient, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/edit";
        }
        clientService.updateClientById(id, updatedClient);
        return "redirect:/admin/info-clients";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable("id") Long id) {
        clientService.deleteClientById(id);
        return "redirect:/admin/info-clients";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showClientsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("clientsBySearch", clientService.showClientsBySearch(search));
        model.addAttribute("search", search);
        return "client/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllClientsByParameter(@RequestParam("parameter") String parameter,
                                            @RequestParam("sortDirection") String sortDirection,
                                            Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allClients", clientService.sortAllClientsByParameter(parameter, sortDirection));
        return "client/show_all";
    }
}
