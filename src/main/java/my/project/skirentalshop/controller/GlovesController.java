package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Gloves;
import my.project.skirentalshop.service.GlovesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/gloves")
public class GlovesController {

    private final GlovesService glovesService;

    @Autowired
    public GlovesController(GlovesService glovesService) {
        this.glovesService = glovesService;
    }

    // ----- show all -----
    @GetMapping
    public String showAllGloves(Model model) {
        model.addAttribute("allGloves", glovesService.showAllGloves());
        return "gloves/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewGloves(Model model) {
        model.addAttribute("newGloves", new Gloves());
        return "gloves/add_new";
    }

    @PostMapping()
    public String addNewGlovesToDB(@ModelAttribute("newGloves") @Valid Gloves gloves,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "gloves/add_new";
        }
        glovesService.addNewGlovesToDB(gloves);
        return "redirect:/admin/info-equipment/gloves";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneGloves(@PathVariable("id") Long id, Model model) {
        model.addAttribute("glovesToUpdate", glovesService.showOneGlovesById(id));
        return "gloves/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateGloves(@PathVariable("id") Long id,
                               @ModelAttribute("glovesToUpdate") @Valid Gloves updatedGloves,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "gloves/edit";
        }
        glovesService.updateGlovesById(id, updatedGloves);
        return "redirect:/admin/info-equipment/gloves";
    }

    //------delete-----
    @DeleteMapping("/{id}")
    public String deleteGloves(@PathVariable("id") Long id) {
        glovesService.deleteGlovesById(id);
        return "redirect:/admin/info-equipment/gloves";
    }

    //------ search-----
    @GetMapping("/search")
    public String showGlovesBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("glovesBySearch", glovesService.showGlovesBySearch(search));
        model.addAttribute("search", search);
        return "gloves/search";
    }

    //------- sort------
    @GetMapping("/sort")
    public String sortAllGlovesByParameter(@RequestParam("parameter") String parameter,
                                           @RequestParam("sortDirection") String sortDirection,
                                           Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allGloves", glovesService.sortAllGlovesByParameter(parameter, sortDirection));
        return "gloves/show_all";
    }
}
