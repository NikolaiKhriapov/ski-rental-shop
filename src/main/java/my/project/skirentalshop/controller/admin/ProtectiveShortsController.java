package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.ProtectiveShorts;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.PROTECTIVE_SHORTS;

@Controller
@RequestMapping("/admin/info-equipment/protective-shorts")
public class ProtectiveShortsController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;
    private final String typeOfEquipment = PROTECTIVE_SHORTS.name().toLowerCase().replace('_', '-');

    @Autowired
    public ProtectiveShortsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllProtectiveShorts(Model model) {
        model.addAttribute("allEquipment", (List<ProtectiveShorts>) equipmentService.showAllEquipment(PROTECTIVE_SHORTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewProtectiveShorts(Model model) {
        model.addAttribute("newEquipment", new ProtectiveShorts());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewProtectiveShortsToDB(@ModelAttribute("newEquipment") @Valid ProtectiveShorts protectiveShorts,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) protectiveShorts, PROTECTIVE_SHORTS);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneProtectiveShorts(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateProtectiveShorts(@PathVariable("equipmentId") Long equipmentId,
                                         @ModelAttribute("equipmentToUpdate") @Valid ProtectiveShorts updatedProtectiveShorts,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedProtectiveShorts, PROTECTIVE_SHORTS);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteProtectiveShorts(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- search -----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showProtectiveShortsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<ProtectiveShorts>) equipmentService.showEquipmentBySearch(search, PROTECTIVE_SHORTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllProtectiveShortsByParameter(@RequestParam("parameter") String parameter,
                                                     @RequestParam("sortDirection") String sortDirection,
                                                     Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<ProtectiveShorts>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, PROTECTIVE_SHORTS));
        return "admin/equipment/show_all";
    }
}
