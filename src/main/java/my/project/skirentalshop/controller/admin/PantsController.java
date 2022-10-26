package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Pants;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.PANTS;

@Controller
@RequestMapping("/admin/info-equipment/pants")
public class PantsController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;

    @Autowired
    public PantsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    //----show all----
    @GetMapping
    @SuppressWarnings("unchecked")
    public String showAllPants(Model model) {
        model.addAttribute("typeOfEquipment", "pants");
        model.addAttribute("allPants", (List<Pants>) equipmentService.showAllEquipment(PANTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewPants(Model model) {
        model.addAttribute("newPants", new Pants());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewPantsToDB(@ModelAttribute("newPants") @Valid Pants pants,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) pants, PANTS);
        return "redirect:/admin/info-equipment/pants";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOnePants(@PathVariable("id") Long id, Model model) {
        model.addAttribute("pantsToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updatePants(@PathVariable("id") Long id,
                              @ModelAttribute("pantsToUpdate") @Valid Pants updatedPants,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedPants, PANTS);
        return "redirect:/admin/info-equipment/pants";
    }

    //------delete-----
    @DeleteMapping("/{id}")
    public String deletePants(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/pants";
    }

    //------ search-----
    @GetMapping("/search")
    public String showPantsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("pantsBySearch", equipmentService.showEquipmentBySearch(search, PANTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    //------- sort------
    @GetMapping("/sort")
    public String sortAllPantsByParameter(@RequestParam("parameter") String parameter,
                                          @RequestParam("sortDirection") String sortDirection,
                                          Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allPants", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, PANTS));
        return "admin/equipment/show_all";
    }
}
