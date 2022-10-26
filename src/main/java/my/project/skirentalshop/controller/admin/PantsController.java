package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Pants;
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
    private final String typeOfEquipment = PANTS.name().toLowerCase().replace('_', '-');

    @Autowired
    public PantsController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    //----show all----
    @GetMapping
    @SuppressWarnings("unchecked")
    public String showAllPants(Model model) {
        model.addAttribute("allEquipment", (List<Pants>) equipmentService.showAllEquipment(PANTS));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewPants(Model model) {
        model.addAttribute("newEquipment", new Pants());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewPantsToDB(@ModelAttribute("newEquipment") @Valid Pants pants,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) pants, PANTS);
        return "redirect:/admin/info-equipment/pants";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOnePants(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updatePants(@PathVariable("equipmentId") Long equipmentId,
                              @ModelAttribute("equipmentToUpdate") @Valid Pants updatedPants,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedPants, PANTS);
        return "redirect:/admin/info-equipment/pants";
    }

    //------delete-----
    @DeleteMapping("/{equipmentId}")
    public String deletePants(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/pants";
    }

    //------ search-----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showPantsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<Pants>) equipmentService.showEquipmentBySearch(search, PANTS));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    //------- sort------
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllPantsByParameter(@RequestParam("parameter") String parameter,
                                          @RequestParam("sortDirection") String sortDirection,
                                          Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<Pants>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, PANTS));
        return "admin/equipment/show_all";
    }
}
