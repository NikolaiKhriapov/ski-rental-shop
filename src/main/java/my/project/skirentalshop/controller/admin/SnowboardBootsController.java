package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
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
    private final String typeOfEquipment = SNOWBOARD_BOOTS.name().toLowerCase().replace('_', '-');

    @Autowired
    public SnowboardBootsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSnowboardBoots(Model model) {
        model.addAttribute("allEquipment", (List<SnowboardBoots>) equipmentService.showAllEquipment(SNOWBOARD_BOOTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSnowboardBoots(Model model) {
        model.addAttribute("newEquipment", new SnowboardBoots());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewSnowboardBootsToDB(@ModelAttribute("newEquipment") @Valid SnowboardBoots snowboardBoots,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) snowboardBoots, SNOWBOARD_BOOTS);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneSnowboardBoots(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateSnowboardBoots(@PathVariable("equipmentId") Long equipmentId,
                                       @ModelAttribute("equipmentToUpdate") @Valid SnowboardBoots updatedSnowboardBoots,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedSnowboardBoots, SNOWBOARD_BOOTS);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteSnowboardBoots(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/snowboard-boots";
    }

    // ----- search -----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showSnowboardBootsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<SnowboardBoots>) equipmentService.showEquipmentBySearch(search, SNOWBOARD_BOOTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllSnowboardBootsByParameter(@RequestParam("parameter") String parameter,
                                                   @RequestParam("sortDirection") String sortDirection,
                                                   Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<SnowboardBoots>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SNOWBOARD_BOOTS));
        return "admin/equipment/show_all";
    }
}
