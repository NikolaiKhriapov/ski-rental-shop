package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.model.SnowboardBoots;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.*;

@Controller
@RequestMapping("/admin/info-equipment/snowboard-boots")
public class SnowboardBootsController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;

    @Autowired
    public SnowboardBootsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSnowboardBoots(Model model) {
        model.addAttribute("typeOfEquipment", "snowboard-boots");
        model.addAttribute("allSnowboardBoots", (List<SnowboardBoots>) equipmentService.showAllEquipment(SNOWBOARD_BOOTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSnowboardBoots(Model model) {
        model.addAttribute("newSnowboardBoots", new SnowboardBoots());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewSnowboardBootsToDB(@ModelAttribute("newSnowboardBoots") @Valid SnowboardBoots snowboardBoots,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) snowboardBoots, SNOWBOARD_BOOTS);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSnowboardBoots(@PathVariable("id") Long id, Model model) {
        model.addAttribute("snowboardBootsToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateSnowboardBoots(@PathVariable("id") Long id,
                                       @ModelAttribute("snowboardBootsToUpdate") @Valid SnowboardBoots updatedSnowboardBoots,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedSnowboardBoots, SNOWBOARD_BOOTS);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSnowboardBoots(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSnowboardBootsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("snowboardBootsBySearch", equipmentService.showEquipmentBySearch(search, SNOWBOARD_BOOTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSnowboardBootsByParameter(@RequestParam("parameter") String parameter,
                                                   @RequestParam("sortDirection") String sortDirection,
                                                   Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSnowboardBoots", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SNOWBOARD_BOOTS));
        return "admin/equipment/show_all";
    }
}
