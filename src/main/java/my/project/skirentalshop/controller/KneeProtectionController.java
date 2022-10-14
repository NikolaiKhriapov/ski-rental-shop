package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.KneeProtection;
import my.project.skirentalshop.service.KneeProtectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/knee-protection")
public class KneeProtectionController {

    private final KneeProtectionService kneeProtectionService;

    @Autowired
    public KneeProtectionController(KneeProtectionService kneeProtectionService) {
        this.kneeProtectionService = kneeProtectionService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllKneeProtection(Model model) {
        model.addAttribute("allKneeProtection", kneeProtectionService.showAllKneeProtection());
        return "knee_protection/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewKneeProtection(Model model) {
        model.addAttribute("newKneeProtection", new KneeProtection());
        return "knee_protection/add_new";
    }

    @PostMapping()
    public String addNewKneeProtectionToDB(@ModelAttribute("newKneeProtection") @Valid KneeProtection kneeProtection,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "knee_protection/add_new";
        }
        kneeProtectionService.addNewKneeProtectionToDB(kneeProtection);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneKneeProtection(@PathVariable("id") Long id, Model model) {
        model.addAttribute("kneeProtectionToUpdate", kneeProtectionService.showOneKneeProtectionById(id));
        return "knee_protection/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateKneeProtection(@PathVariable("id") Long id,
                                       @ModelAttribute("kneeProtectionToUpdate") @Valid KneeProtection updatedKneeProtection,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "knee_protection/edit";
        }
        kneeProtectionService.updateKneeProtectionById(id, updatedKneeProtection);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteKneeProtection(@PathVariable("id") Long id) {
        kneeProtectionService.deleteKneeProtectionById(id);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showKneeProtectionBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("kneeProtectionBySearch", kneeProtectionService.showKneeProtectionBySearch(search));
        model.addAttribute("search", search);
        return "knee_protection/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllKneeProtectionByParameter(@RequestParam("parameter") String parameter,
                                                   @RequestParam("sortDirection") String sortDirection,
                                                   Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allKneeProtection", kneeProtectionService.sortAllKneeProtectionByParameter(parameter, sortDirection));
        return "knee_protection/show_all";
    }
}
