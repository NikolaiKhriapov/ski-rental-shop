package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Gloves;
import my.project.skirentalshop.model.Snowboard;
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

    @Autowired
    public GlovesController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping
    @SuppressWarnings("unchecked")
    public String showAllGloves(Model model) {
        model.addAttribute("typeOfEquipment", "gloves");
        model.addAttribute("allGloves", (List<Gloves>) equipmentService.showAllEquipment(GLOVES));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewGloves(Model model) {
        model.addAttribute("newGloves", new Gloves());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewGlovesToDB(@ModelAttribute("newGloves") @Valid Gloves gloves,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) gloves, GLOVES);
        return "redirect:/admin/info-equipment/gloves";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneGloves(@PathVariable("id") Long id, Model model) {
        model.addAttribute("glovesToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateGloves(@PathVariable("id") Long id,
                               @ModelAttribute("glovesToUpdate") @Valid Gloves updatedGloves,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedGloves, GLOVES);
        return "redirect:/admin/info-equipment/gloves";
    }

    //------delete-----
    @DeleteMapping("/{id}")
    public String deleteGloves(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/gloves";
    }

    //------ search-----
    @GetMapping("/search")
    public String showGlovesBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("glovesBySearch", equipmentService.showEquipmentBySearch(search, GLOVES));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    //------- sort------
    @GetMapping("/sort")
    public String sortAllGlovesByParameter(@RequestParam("parameter") String parameter,
                                           @RequestParam("sortDirection") String sortDirection,
                                           Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allGloves", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, GLOVES));
        return "admin/equipment/show_all";
    }
}
