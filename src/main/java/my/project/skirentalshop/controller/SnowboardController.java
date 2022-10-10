package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.service.SnowboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/snowboard")
public class SnowboardController {

    private final SnowboardService snowboardService;

    @Autowired
    public SnowboardController(SnowboardService snowboardService) {
        this.snowboardService = snowboardService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllSnowboards(Model model) {
        model.addAttribute("allSnowboards", snowboardService.showAllSnowboards());
        return "snowboard/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSnowboard(Model model) {
        model.addAttribute("newSnowboard", new Snowboard());
        return "snowboard/add_new";
    }

    @PostMapping()
    public String addNewSnowboardToDB(@ModelAttribute("newSnowboard") @Valid Snowboard snowboard,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "snowboard/add_new";
        }
        snowboardService.addNewSnowboardToDB(snowboard);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSnowboard(@PathVariable("id") Long id, Model model) {
        model.addAttribute("snowboardToUpdate", snowboardService.showOneSnowboardById(id));
        return "snowboard/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateSnowboard(@PathVariable("id") Long id,
                                  @ModelAttribute("snowboardToUpdate") @Valid Snowboard updatedSnowboard,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "snowboard/edit";
        }
        snowboardService.updateSnowboardById(id, updatedSnowboard);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSnowboard(@PathVariable("id") Long id) {
        snowboardService.deleteSnowboardById(id);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSnowboardsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("snowboardsBySearch", snowboardService.showSnowboardsBySearch(search));
        return "snowboard/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSnowboardsByParameter(@RequestParam("parameter") String parameter,
                                               @RequestParam("sortDirection") String sortDirection,
                                               Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSnowboards", snowboardService.sortAllSnowboardsByParameter(parameter, sortDirection));
        return "snowboard/show_all";
    }
}
