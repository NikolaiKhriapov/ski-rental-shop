package my.project.skirentalshop.controller.admin;

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
        return "admin/client/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewClient(Model model) {
        model.addAttribute("newClient", new Client());
        return "admin/client/add_new";
    }

    @PostMapping()
    public String addNewClientToDB(@ModelAttribute("newClient") @Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/add_new";
        }
        clientService.addNewClientToDB(client);
        return "redirect:/admin/info-clients";
    }

    // ----- edit -----
    @GetMapping("/edit/{clientId}")
    public String showOneClient(@PathVariable("clientId") Long clientId, Model model) {
        model.addAttribute("clientToUpdate", clientService.showOneClientById(clientId));
        return "admin/client/edit";
    }

    @PatchMapping("/edit/{clientId}")
    public String updateClient(@PathVariable("clientId") Long clientId,
                               @ModelAttribute("clientToUpdate") @Valid Client updatedClient,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/client/edit";
        }
        clientService.updateClientById(clientId, updatedClient);
        return "redirect:/admin/info-clients";
    }

    // ----- delete -----
    @DeleteMapping("/{clientId}")
    public String deleteClient(@PathVariable("clientId") Long clientId) {
        clientService.deleteClientById(clientId);
        return "redirect:/admin/info-clients";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showClientsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("clientsBySearch", clientService.showClientsBySearch(search));
        model.addAttribute("search", search);
        return "admin/client/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllClientsByParameter(@RequestParam("parameter") String parameter,
                                            @RequestParam("sortDirection") String sortDirection,
                                            Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allClients", clientService.sortAllClientsByParameter(parameter, sortDirection));
        return "admin/client/show_all";
    }
}
