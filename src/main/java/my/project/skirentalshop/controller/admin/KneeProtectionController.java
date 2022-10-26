package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.KneeProtection;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.KNEE_PROTECTION;

@Controller
@RequestMapping("/admin/info-equipment/knee-protection")
public class KneeProtectionController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;

    @Autowired
    public KneeProtectionController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllKneeProtection(Model model) {
        model.addAttribute("typeOfEquipment", "knee-protection");
        model.addAttribute("allKneeProtection", (List<KneeProtection>) equipmentService.showAllEquipment(KNEE_PROTECTION));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewKneeProtection(Model model) {
        model.addAttribute("newKneeProtection", new KneeProtection());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewKneeProtectionToDB(@ModelAttribute("newKneeProtection") @Valid KneeProtection kneeProtection,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) kneeProtection, KNEE_PROTECTION);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneKneeProtection(@PathVariable("id") Long id, Model model) {
        model.addAttribute("kneeProtectionToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateKneeProtection(@PathVariable("id") Long id,
                                       @ModelAttribute("kneeProtectionToUpdate") @Valid KneeProtection updatedKneeProtection,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedKneeProtection, KNEE_PROTECTION);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteKneeProtection(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/knee-protection";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showKneeProtectionBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("kneeProtectionBySearch", equipmentService.showEquipmentBySearch(search, KNEE_PROTECTION));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllKneeProtectionByParameter(@RequestParam("parameter") String parameter,
                                                   @RequestParam("sortDirection") String sortDirection,
                                                   Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allKneeProtection", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, KNEE_PROTECTION));
        return "admin/equipment/show_all";
    }
}
