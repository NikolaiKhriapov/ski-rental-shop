package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Ski;
import my.project.skirentalshop.service.SkiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/ski")
public class SkiController {

    private final SkiService skiService;

    @Autowired
    public SkiController(SkiService skiService) {
        this.skiService = skiService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllSki(Model model) {
        model.addAttribute("allSki", skiService.showAllSki());
        return "ski/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSki(Model model) {
        model.addAttribute("newSki", new Ski());
        return "ski/add_new";
    }

    @PostMapping()
    public String addNewSkiToDB(@ModelAttribute("newSki") @Valid Ski ski,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ski/add_new";
        }
        skiService.addNewSkiToDB(ski);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSki(@PathVariable("id") Long id, Model model) {
        model.addAttribute("skiToUpdate", skiService.showOneSkiById(id));
        return "ski/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable("id") Long id,
                       @ModelAttribute("skiToUpdate") @Valid Ski updatedSki,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ski/edit";
        }
        skiService.updateSkiById(id, updatedSki);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSki(@PathVariable("id") Long id) {
        skiService.deleteSkiById(id);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSkiBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("skiBySearch", skiService.showSkiBySearch(search));
        model.addAttribute("search", search);
        return "ski/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSkiByParameter(@RequestParam("parameter") String parameter,
                                        @RequestParam("sortDirection") String sortDirection,
                                        Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSki", skiService.sortAllByParameter(parameter, sortDirection));
        return "ski/show_all";
    }
}
