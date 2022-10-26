package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.SkiBoots;
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
    private final String typeOfEquipment = SKI_BOOTS.name().toLowerCase().replace('_', '-');

    @Autowired
    public SkiBootsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSkiBoots(Model model) {
        model.addAttribute("allEquipment", (List<SkiBoots>) equipmentService.showAllEquipment(SKI_BOOTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSkiBoots(Model model) {
        model.addAttribute("newEquipment", new SkiBoots());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewSkiBootsToDB(@ModelAttribute("newEquipment") @Valid SkiBoots skiBoots,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) skiBoots, SKI_BOOTS);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneSkiBoots(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateSkiBoots(@PathVariable("equipmentId") Long equipmentId,
                                 @ModelAttribute("equipmentToUpdate") @Valid SkiBoots updatedSkiBoots,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedSkiBoots, SKI_BOOTS);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteSkiBoots(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/ski-boots";
    }

    // ----- search -----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showSkiBootsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<SkiBoots>) equipmentService.showEquipmentBySearch(search, SKI_BOOTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllSkiBootsByParameter(@RequestParam("parameter") String parameter,
                                             @RequestParam("sortDirection") String sortDirection,
                                             Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<SkiBoots>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SKI_BOOTS));
        return "admin/equipment/show_all";
    }
}
