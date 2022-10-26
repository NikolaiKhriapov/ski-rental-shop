package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.KneeProtection;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.KNEE_PROTECTION;
import static my.project.skirentalshop.model.enums.TypesOfEquipment.SNOWBOARD;

@Controller
@RequestMapping("/admin/info-equipment/knee-protection")
public class KneeProtectionController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;
    private final String typeOfEquipment = KNEE_PROTECTION.name().toLowerCase().replace('_', '-');

    @Autowired
    public KneeProtectionController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllKneeProtection(Model model) {
        model.addAttribute("allEquipment", (List<KneeProtection>) equipmentService.showAllEquipment(KNEE_PROTECTION));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewKneeProtection(Model model) {
        model.addAttribute("newEquipment", new KneeProtection());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewKneeProtectionToDB(@ModelAttribute("newEquipment") @Valid KneeProtection kneeProtection,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) kneeProtection, KNEE_PROTECTION);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneKneeProtection(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateKneeProtection(@PathVariable("equipmentId") Long equipmentId,
                                       @ModelAttribute("equipmentToUpdate") @Valid KneeProtection updatedKneeProtection,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedKneeProtection, KNEE_PROTECTION);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteKneeProtection(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- search -----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showKneeProtectionBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<KneeProtection>) equipmentService.showEquipmentBySearch(search, KNEE_PROTECTION));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllKneeProtectionByParameter(@RequestParam("parameter") String parameter,
                                                   @RequestParam("sortDirection") String sortDirection,
                                                   Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<KneeProtection>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, KNEE_PROTECTION));
        return "admin/equipment/show_all";
    }
}
