package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Helmet;
import my.project.skirentalshop.service.HelmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/info-equipment/helmet")
public class HelmetController {

    private final HelmetService helmetService;

    @Autowired
    public HelmetController(HelmetService helmetService) {
        this.helmetService = helmetService;
    }

    // ----- show all -----
    @GetMapping()
    public String showAllHelmets(Model model) {
        model.addAttribute("allHelmet", helmetService.showAllHelmets());
        return "admin/helmet/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewHelmet(Model model) {
        model.addAttribute("newHelmet", new Helmet());
        return "admin/helmet/add_new";
    }

    @PostMapping()
    public String addNewHelmetToDB(@ModelAttribute("newHelmet") @Valid Helmet helmet,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/helmet/add_new";
        }
        helmetService.addNewHelmetToDB(helmet);
        return "redirect:/admin/info-equipment/helmet";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneHelmet(@PathVariable("id") Long id, Model model) {
        model.addAttribute("helmetToUpdate", helmetService.showOneHelmetById(id));
        return "admin/helmet/edit";
    }

    @PatchMapping("/edit/{id}")
    public String updateHelmet(@PathVariable("id") Long id,
                               @ModelAttribute("helmetToUpdate") @Valid Helmet updatedHelmet,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/helmet/edit";
        }
        helmetService.updateHelmetById(id, updatedHelmet);
        return "redirect:/admin/info-equipment/helmet";
    }

    // ---------- delete --------
    @DeleteMapping("/{id}")
    public String deleteHelmet(@PathVariable("id") Long id) {
        helmetService.deleteHelmetById(id);
        return "redirect:/admin/info-equipment/helmet";
    }

    //------------search---------
    @GetMapping("/search")
    public String showHelmetsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("helmetBySearch", helmetService.showHelmetsBySearch(search));
        model.addAttribute("search", search);
        return "admin/helmet/search";
    }

    //-----------sort-------------
    @GetMapping("/sort")
    public String sortAllHelmetsByParameter(@RequestParam("parameter") String parameter,
                                            @RequestParam("sortDirection") String sortDirection,
                                            Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allHelmet", helmetService.sortAllHelmetsByParameter(parameter, sortDirection));
        return "admin/helmet/show_all";
    }
}
