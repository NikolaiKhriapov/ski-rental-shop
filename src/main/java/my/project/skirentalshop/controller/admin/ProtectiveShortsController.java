package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.ProtectiveShorts;
import my.project.skirentalshop.model.Snowboard;
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

    @Autowired
    public ProtectiveShortsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllProtectiveShorts(Model model) {
        model.addAttribute("typeOfEquipment", "protective-shorts");
        model.addAttribute("allProtectiveShorts", (List<ProtectiveShorts>) equipmentService.showAllEquipment(PROTECTIVE_SHORTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewProtectiveShorts(Model model) {
        model.addAttribute("newProtectiveShorts", new ProtectiveShorts());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewProtectiveShortsToDB(@ModelAttribute("newProtectiveShorts") @Valid ProtectiveShorts protectiveShorts,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) protectiveShorts, PROTECTIVE_SHORTS);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneProtectiveShorts(@PathVariable("id") Long id, Model model) {
        model.addAttribute("protectiveShortsToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateProtectiveShorts(@PathVariable("id") Long id,
                                         @ModelAttribute("protectiveShortsToUpdate") @Valid ProtectiveShorts updatedProtectiveShorts,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedProtectiveShorts, PROTECTIVE_SHORTS);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteProtectiveShorts(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/protective-shorts";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showProtectiveShortsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("protectiveShortsBySearch", equipmentService.showEquipmentBySearch(search, PROTECTIVE_SHORTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllProtectiveShortsByParameter(@RequestParam("parameter") String parameter,
                                                     @RequestParam("sortDirection") String sortDirection,
                                                     Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allProtectiveShorts", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, PROTECTIVE_SHORTS));
        return "admin/equipment/show_all";
    }
}
