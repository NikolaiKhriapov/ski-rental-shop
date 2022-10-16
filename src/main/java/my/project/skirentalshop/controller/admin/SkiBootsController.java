package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.SkiBoots;
import my.project.skirentalshop.service.SkiBootsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/ski-boots")
public class SkiBootsController {

    private final SkiBootsService skiBootsService;

    @Autowired
    public SkiBootsController(SkiBootsService skiBootsService) {
        this.skiBootsService = skiBootsService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllSkiBoots(Model model) {
        model.addAttribute("allSkiBoots", skiBootsService.showAllSkiBoots());
        return "admin/ski_boots/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSkiBoots(Model model) {
        model.addAttribute("newSkiBoots", new SkiBoots());
        return "admin/ski_boots/add_new";
    }

    @PostMapping()
    public String addNewSkiBootsToDB(@ModelAttribute("newSkiBoots") @Valid SkiBoots skiBoots,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/ski_boots/add_new";
        }
        skiBootsService.addNewSkiBootsToDB(skiBoots);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSkiBoots(@PathVariable("id") Long id, Model model) {
        model.addAttribute("skiBootsToUpdate", skiBootsService.showOneSkiBootsById(id));
        return "admin/ski_boots/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateSkiBoots(@PathVariable("id") Long id,
                                 @ModelAttribute("skiBootsToUpdate") @Valid SkiBoots updatedSkiBoots,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/ski_boots/edit";
        }
        skiBootsService.updateSkiBootsById(id, updatedSkiBoots);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSkiBoots(@PathVariable("id") Long id) {
        skiBootsService.deleteSkiBootsById(id);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSkiBootsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("skiBootsBySearch", skiBootsService.showSkiBootsBySearch(search));
        model.addAttribute("search", search);
        return "admin/ski_boots/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSkiBootsByParameter(@RequestParam("parameter") String parameter,
                                             @RequestParam("sortDirection") String sortDirection,
                                             Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSkiBoots", skiBootsService.sortAllSkiBootsByParameter(parameter, sortDirection));
        return "admin/ski_boots/show_all";
    }
}
