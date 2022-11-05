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
import java.util.Objects;

@Controller
@RequestMapping("/admin/equipment/{type}")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
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
        model.addAttribute("listOfEquipment",
                equipmentService.showAllEquipment(TypesOfEquipment.convertToEnumField(type)));
        return "admin/equipment/equipment";
    }

    // ----- add new -----
    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("equipment", new Equipment());
        return "admin/equipment/equipment";
    }

    @PostMapping
    public String create(@PathVariable("type") String type,
                         @ModelAttribute("equipment") @Valid Equipment oneEquipment,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "admin/equipment/equipment";
        }
        equipmentService.addNewEquipmentToDB(oneEquipment, TypesOfEquipment.convertToEnumField(type));
        return "redirect:/admin/equipment/" + type;
    }

    // ----- edit -----
    @GetMapping("/{equipmentId}")
    public String showOne(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("action", "update");
        model.addAttribute("equipment", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/equipment";
    }

    @PatchMapping("/{equipmentId}")
    public String update(@PathVariable("type") String type,
                         @PathVariable("equipmentId") Long equipmentId,
                         @ModelAttribute("equipment") @Valid Equipment updatedEquipment,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "update");
            return "admin/equipment/equipment";
        }
        equipmentService.updateEquipmentById(
                equipmentId, updatedEquipment, Objects.requireNonNull(TypesOfEquipment.convertToEnumField(type))
        );
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
        model.addAttribute("listOfEquipment",
                equipmentService.showEquipmentBySearch(search, TypesOfEquipment.convertToEnumField(type)));
        model.addAttribute("search", search);
        return "admin/equipment/equipment";
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
                parameter, sortDirection, TypesOfEquipment.convertToEnumField(type))
        );
        return "admin/equipment/equipment";
    }
}
