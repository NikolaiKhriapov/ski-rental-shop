package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Ski;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.SKI;
import static my.project.skirentalshop.model.enums.TypesOfEquipment.SNOWBOARD;

@Controller
@RequestMapping("/admin/info-equipment/ski")
public class SkiController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;

    @Autowired
    public SkiController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSki(Model model) {
        model.addAttribute("typeOfEquipment", "ski");
        model.addAttribute("allSki", (List<Ski>) equipmentService.showAllEquipment(SKI));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSki(Model model) {
        model.addAttribute("newSki", new Ski());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewSkiToDB(@ModelAttribute("newSki") @Valid Ski ski,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) ski, SKI);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSki(@PathVariable("id") Long id, Model model) {
        model.addAttribute("skiToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String edit(@PathVariable("id") Long id,
                       @ModelAttribute("skiToUpdate") @Valid Ski updatedSki,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedSki, SKI);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSki(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/ski";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSkiBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("skiBySearch", equipmentService.showEquipmentBySearch(search, SKI));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSkiByParameter(@RequestParam("parameter") String parameter,
                                        @RequestParam("sortDirection") String sortDirection,
                                        Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSki", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SKI));
        return "admin/equipment/show_all";
    }
}
