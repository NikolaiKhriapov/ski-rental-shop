package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.SnowboardBoots;
import my.project.skirentalshop.service.SnowboardBootsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/snowboard-boots")
public class SnowboardBootsController {

    private final SnowboardBootsService snowboardBootsService;

    @Autowired
    public SnowboardBootsController(SnowboardBootsService snowboardBootsService) {
        this.snowboardBootsService = snowboardBootsService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllSnowboardBoots(Model model) {
        model.addAttribute("allSnowboardBoots", snowboardBootsService.showAllSnowboardBoots());
        return "admin/snowboard_boots/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSnowboardBoots(Model model) {
        model.addAttribute("newSnowboardBoots", new SnowboardBoots());
        return "admin/snowboard_boots/add_new";
    }

    @PostMapping()
    public String addNewSnowboardBootsToDB(@ModelAttribute("newSnowboardBoots") @Valid SnowboardBoots snowboardBoots,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/snowboard_boots/add_new";
        }
        snowboardBootsService.addNewSnowboardBootsToDB(snowboardBoots);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSnowboardBoots(@PathVariable("id") Long id, Model model) {
        model.addAttribute("snowboardBootsToUpdate", snowboardBootsService.showOneSnowboardBootsById(id));
        return "admin/snowboard_boots/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateSnowboardBoots(@PathVariable("id") Long id,
                                       @ModelAttribute("snowboardBootsToUpdate") @Valid SnowboardBoots updatedSnowboardBoots,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/snowboard_boots/edit";
        }
        snowboardBootsService.updateSnowboardBootsById(id, updatedSnowboardBoots);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSnowboardBoots(@PathVariable("id") Long id) {
        snowboardBootsService.deleteSnowboardBootsById(id);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSnowboardBootsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("snowboardBootsBySearch", snowboardBootsService.showSnowboardBootsBySearch(search));
        model.addAttribute("search", search);
        return "admin/snowboard_boots/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSnowboardBootsByParameter(@RequestParam("parameter") String parameter,
                                                   @RequestParam("sortDirection") String sortDirection,
                                                   Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSnowboardBoots", snowboardBootsService.sortAllSnowboardBootsByParameter(parameter, sortDirection));
        return "admin/snowboard_boots/show_all";
    }
}
