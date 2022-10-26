package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Ski;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.SKI;

@Controller
@RequestMapping("/admin/info-equipment/ski")
public class SkiController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;
    private final String typeOfEquipment = SKI.name().toLowerCase().replace('_', '-');

    @Autowired
    public SkiController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSki(Model model) {
        model.addAttribute("allEquipment", (List<Ski>) equipmentService.showAllEquipment(SKI));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSki(Model model) {
        model.addAttribute("newEquipment", new Ski());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewSkiToDB(@ModelAttribute("newEquipment") @Valid Ski ski,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) ski, SKI);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneSki(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateSki(@PathVariable("equipmentId") Long equipmentId,
                       @ModelAttribute("equipmentToUpdate") @Valid Ski updatedSki,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedSki, SKI);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteSki(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- search -----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showSkiBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<Ski>) equipmentService.showEquipmentBySearch(search, SKI));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllSkiByParameter(@RequestParam("parameter") String parameter,
                                        @RequestParam("sortDirection") String sortDirection,
                                        Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<Ski>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SKI));
        return "admin/equipment/show_all";
    }
}
