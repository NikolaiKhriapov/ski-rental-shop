package my.project.skirentalshop.controller;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/equipment/{type}")
public class EquipmentByTypeController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentByTypeController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(@PathVariable("type") String type, Model model) {
        model.addAttribute("typeOfEquipment", TypesOfEquipment.convertToEnumField(type));
    }

    // ----- show all -----
    @GetMapping
    public String showAll(@PathVariable("type") String type, Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfEquipment", equipmentService.showAllEquipment(type));
        return "admin/equipment/equipment_by_type";
    }

    // ----- add new -----
    @GetMapping("/new")
    public String create(@PathVariable("type") String type,
                         Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("equipment", equipmentService.createNewEquipmentByType(type));
        return "admin/equipment/equipment_by_type";
    }

    @PostMapping
    public String create(@PathVariable("type") String type,
                         @ModelAttribute("equipment") @Valid Equipment oneEquipment,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "admin/equipment/equipment_by_type";
        }
        equipmentService.addNewEquipmentToDB(oneEquipment);
        return "redirect:/admin/equipment/" + type;
    }

    // ----- edit -----
    @GetMapping("/{equipmentId}")
    public String showOne(@PathVariable("type") String type,
                          @PathVariable("equipmentId") Long equipmentId, Model model) {
        System.out.println("!!!!!" + equipmentService.showOneEquipmentById(equipmentId, type).toString());
        model.addAttribute("action", "update");
        model.addAttribute("equipment",
                equipmentService.showOneEquipmentById(equipmentId, type));
        return "admin/equipment/equipment_by_type";
    }

    @PatchMapping("/{equipmentId}")
    public String update(@PathVariable("type") String type,
                         @PathVariable("equipmentId") Long equipmentId,
                         @ModelAttribute("equipment") @Valid Equipment updatedEquipment,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "update");
            return "admin/equipment/equipment_by_type";
        }
        equipmentService.updateEquipmentById(equipmentId, updatedEquipment, type);
        return "redirect:/admin/equipment/" + type;
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String delete(@PathVariable("type") String type,
                         @PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/equipment/" + type;
    }

    // ----- search -----
    @GetMapping("/search")
    public String showAllBySearch(@PathVariable("type") String type,
                                  @RequestParam("search") String search,
                                  Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfEquipment", equipmentService.showEquipmentBySearch(search, type));
        model.addAttribute("search", search);
        return "admin/equipment/equipment_by_type";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllByParameter(@PathVariable("type") String type,
                                     @RequestParam("parameter") String parameter,
                                     @RequestParam("sortDirection") String sortDirection,
                                     Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfEquipment", equipmentService.sortAllEquipmentByParameter(
                parameter, sortDirection, type)
        );
        return "admin/equipment/equipment_by_type";
    }
}