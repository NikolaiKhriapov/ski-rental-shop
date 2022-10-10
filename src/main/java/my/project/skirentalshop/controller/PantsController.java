package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Pants;
import my.project.skirentalshop.service.PantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/pants")
public class PantsController {

    private final PantsService pantsService;

    @Autowired
    public PantsController(PantsService pantsService) {
        this.pantsService = pantsService;
    }

    //----show all----
    @GetMapping
    public String showAllPants(Model model) {
        model.addAttribute("allPants", pantsService.showAllPants());
        return "pants/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewPants(Model model) {
        model.addAttribute("newPants", new Pants());
        return "pants/add_new";
    }

    @PostMapping()
    public String addNewPantsToDB(@ModelAttribute("newPants") @Valid Pants pants,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pants/add_new";
        }
        pantsService.addNewPantsToDB(pants);
        return "redirect:/admin/info-equipment/pants";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOnePants(@PathVariable("id") Long id, Model model) {
        model.addAttribute("pantsToUpdate", pantsService.showOnePantsById(id));
        return "pants/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updatePants(@PathVariable("id") Long id,
                              @ModelAttribute("pantsToUpdate") @Valid Pants updatedPants,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pants/edit";
        }
        pantsService.updatePantsById(id, updatedPants);
        return "redirect:/admin/info-equipment/pants";
    }

    //------delete-----
    @DeleteMapping("/{id}")
    public String deletePants(@PathVariable("id") Long id) {
        pantsService.deletePantsById(id);
        return "redirect:/admin/info-equipment/pants";
    }

    //------ search-----
    @GetMapping("/search")
    public String showPantsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("pantsBySearch", pantsService.showPantsBySearch(search));
        return "pants/search";
    }

    //------- sort------
    @GetMapping("/sort")
    public String sortAllPantsByParameter(@RequestParam("parameter") String parameter,
                                          @RequestParam("sortDirection") String sortDirection,
                                          Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allPants", pantsService.sortAllPantsByParameter(parameter, sortDirection));
        return "pants/show_all";
    }
}
