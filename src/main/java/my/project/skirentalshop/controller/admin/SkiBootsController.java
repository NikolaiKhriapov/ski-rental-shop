package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.SkiBoots;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.SKI_BOOTS;

@Controller
@RequestMapping("/admin/info-equipment/ski-boots")
public class SkiBootsController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;

    @Autowired
    public SkiBootsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSkiBoots(Model model) {
        model.addAttribute("typeOfEquipment", "ski-boots");
        model.addAttribute("allSkiBoots", (List<SkiBoots>) equipmentService.showAllEquipment(SKI_BOOTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSkiBoots(Model model) {
        model.addAttribute("newSkiBoots", new SkiBoots());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewSkiBootsToDB(@ModelAttribute("newSkiBoots") @Valid SkiBoots skiBoots,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) skiBoots, SKI_BOOTS);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSkiBoots(@PathVariable("id") Long id, Model model) {
        model.addAttribute("skiBootsToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateSkiBoots(@PathVariable("id") Long id,
                                 @ModelAttribute("skiBootsToUpdate") @Valid SkiBoots updatedSkiBoots,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedSkiBoots, SKI_BOOTS);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSkiBoots(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSkiBootsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("skiBootsBySearch", equipmentService.showEquipmentBySearch(search, SKI_BOOTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSkiBootsByParameter(@RequestParam("parameter") String parameter,
                                             @RequestParam("sortDirection") String sortDirection,
                                             Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSkiBoots", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SKI_BOOTS));
        return "admin/equipment/show_all";
    }
}
