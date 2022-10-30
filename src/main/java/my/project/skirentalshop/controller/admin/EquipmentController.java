package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.enums.TypesOfEquipment;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.*;

@Controller
@RequestMapping("/admin/equipment/{typeOfEquipment}")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(@PathVariable("typeOfEquipment") String typeOfEquipment, Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    public String showAllEquipment(@PathVariable("typeOfEquipment") String typeOfEquipment, Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("listOfEquipment", equipmentService.showAllEquipment(convertToEnumField(typeOfEquipment)));
        return "admin/equipment/equipment";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewEquipment(Model model) {
        model.addAttribute("action", "create");
        model.addAttribute("equipment", new Equipment());
        return "admin/equipment/equipment";
    }

    @PostMapping("/add-new")
    public String addNewEquipmentToDB(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                      @ModelAttribute("equipment") @Valid Equipment oneEquipment,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "create");
            return "admin/equipment/equipment";
        }
        equipmentService.addNewEquipmentToDB(oneEquipment, convertToEnumField(typeOfEquipment));
        return "redirect:/admin/equipment/" + typeOfEquipment;
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneEquipment(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("action", "update");
        model.addAttribute("equipment", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/equipment";
    }

    @PatchMapping("/edit/{equipmentId}")
    public String updateOneEquipment(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                     @PathVariable("equipmentId") Long equipmentId,
                                     @ModelAttribute("equipment") @Valid Equipment updatedEquipment,
                                     BindingResult bindingResult,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "update");
            return "admin/equipment/equipment";
        }
        equipmentService.updateEquipmentById(equipmentId, updatedEquipment, convertToEnumField(typeOfEquipment));
        return "redirect:/admin/equipment/" + typeOfEquipment;
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteOneEquipment(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                     @PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/equipment/" + typeOfEquipment;
    }

    // ----- search -----
    @GetMapping("/search")
    public String showAllEquipmentBySearch(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                           @RequestParam("search") String search,
                                           Model model) {
        model.addAttribute("action", "search");
        model.addAttribute("listOfEquipment",
                equipmentService.showEquipmentBySearch(search, convertToEnumField(typeOfEquipment)));
        model.addAttribute("search", search);
        return "admin/equipment/equipment";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortEquipmentByParameter(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                           @RequestParam("parameter") String parameter,
                                           @RequestParam("sortDirection") String sortDirection,
                                           Model model) {
        model.addAttribute("action", "showAll");
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("listOfEquipment",
                equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, convertToEnumField(typeOfEquipment)));
        return "admin/equipment/equipment";
    }

    private TypesOfEquipment convertToEnumField(String typeOfEquipment) {
        switch (typeOfEquipment.toUpperCase().replace('-', '_')) {
            case "SNOWBOARD" -> {
                return SNOWBOARD;
            }
            case "SNOWBOARD_BOOTS" -> {
                return SNOWBOARD_BOOTS;
            }
            case "SKI" -> {
                return SKI;
            }
            case "SKI_BOOTS" -> {
                return SKI_BOOTS;
            }
            case "HELMET" -> {
                return HELMET;
            }
            case "JACKET" -> {
                return JACKET;
            }
            case "GLOVES" -> {
                return GLOVES;
            }
            case "PANTS" -> {
                return PANTS;
            }
            case "PROTECTIVE_SHORTS" -> {
                return PROTECTIVE_SHORTS;
            }
            case "KNEE_PROTECTION" -> {
                return KNEE_PROTECTION;
            }
            default -> throw new IllegalArgumentException("Equipment " + typeOfEquipment + " not found!");
        }
    }
}
