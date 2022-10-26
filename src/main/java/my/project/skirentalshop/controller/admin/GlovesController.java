package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Gloves;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.GLOVES;

@Controller
@RequestMapping("/admin/info-equipment/gloves")
public class GlovesController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;
    private final String typeOfEquipment = GLOVES.name().toLowerCase().replace('_', '-');

    @Autowired
    public GlovesController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllGloves(Model model) {
        model.addAttribute("allEquipment", (List<Gloves>) equipmentService.showAllEquipment(GLOVES));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewGloves(Model model) {
        model.addAttribute("newEquipment", new Gloves());
        return "admin/equipment/add_new";
    }

    @PostMapping("add-new")
    @SuppressWarnings("unchecked")
    public String addNewGlovesToDB(@ModelAttribute("newEquipment") @Valid Gloves gloves,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) gloves, GLOVES);
        return "redirect:/admin/info-equipment/gloves";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneGloves(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateGloves(@PathVariable("equipmentId") Long equipmentId,
                               @ModelAttribute("equipmentToUpdate") @Valid Gloves updatedGloves,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedGloves, GLOVES);
        return "redirect:/admin/info-equipment/gloves";
    }

    //------delete-----
    @DeleteMapping("/{equipmentId}")
    public String deleteGloves(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/gloves";
    }

    //------ search-----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showGlovesBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<Gloves>) equipmentService.showEquipmentBySearch(search, GLOVES));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    //------- sort------
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllGlovesByParameter(@RequestParam("parameter") String parameter,
                                           @RequestParam("sortDirection") String sortDirection,
                                           Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<Gloves>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, GLOVES));
        return "admin/equipment/show_all";
    }
}
