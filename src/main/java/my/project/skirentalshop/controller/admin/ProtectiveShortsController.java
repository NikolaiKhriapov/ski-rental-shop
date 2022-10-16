package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.ProtectiveShorts;
import my.project.skirentalshop.service.ProtectiveShortsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/protective-shorts")
public class ProtectiveShortsController {

    private final ProtectiveShortsService protectiveShortsService;

    @Autowired
    public ProtectiveShortsController(ProtectiveShortsService protectiveShortsService) {
        this.protectiveShortsService = protectiveShortsService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllProtectiveShorts(Model model) {
        model.addAttribute("allProtectiveShorts", protectiveShortsService.showAllProtectiveShorts());
        return "admin/protective_shorts/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewProtectiveShorts(Model model) {
        model.addAttribute("newProtectiveShorts", new ProtectiveShorts());
        return "admin/protective_shorts/add_new";
    }

    @PostMapping()
    public String addNewProtectiveShortsToDB(@ModelAttribute("newProtectiveShorts") @Valid ProtectiveShorts protectiveShorts,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/protective_shorts/add_new";
        }
        protectiveShortsService.addNewProtectiveShortsToDB(protectiveShorts);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneProtectiveShorts(@PathVariable("id") Long id, Model model) {
        model.addAttribute("protectiveShortsToUpdate", protectiveShortsService.showOneProtectiveShortsById(id));
        return "admin/protective_shorts/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateProtectiveShorts(@PathVariable("id") Long id,
                                         @ModelAttribute("protectiveShortsToUpdate") @Valid ProtectiveShorts updatedProtectiveShorts,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/protective_shorts/edit";
        }
        protectiveShortsService.updateProtectiveShortsById(id, updatedProtectiveShorts);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteProtectiveShorts(@PathVariable("id") Long id) {
        protectiveShortsService.deleteProtectiveShortsById(id);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showProtectiveShortsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("protectiveShortsBySearch", protectiveShortsService.showProtectiveShortsBySearch(search));
        model.addAttribute("search", search);
        return "admin/protective_shorts/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllProtectiveShortsByParameter(@RequestParam("parameter") String parameter,
                                                     @RequestParam("sortDirection") String sortDirection,
                                                     Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allProtectiveShorts", protectiveShortsService.sortAllProtectiveShortsByParameter(parameter, sortDirection));
        return "admin/protective_shorts/show_all";
    }
}
