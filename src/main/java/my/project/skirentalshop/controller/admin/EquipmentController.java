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
@RequestMapping("/admin/info-equipment/{typeOfEquipment}")
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
        model.addAttribute("allEquipment", equipmentService.showAllEquipment(convertToEnumField(typeOfEquipment)));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewEquipment(Model model) {
        model.addAttribute("newEquipment", new Equipment());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    public String addNewEquipmentToDB(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                      @ModelAttribute("newEquipment") @Valid Equipment oneEquipment,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB(oneEquipment, convertToEnumField(typeOfEquipment));
        return "redirect:/admin/info-equipment/" + typeOfEquipment;
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneEquipment(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    public String updateOneEquipment(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                     @PathVariable("equipmentId") Long equipmentId,
                                     @ModelAttribute("equipmentToUpdate") @Valid Equipment updatedEquipment,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, updatedEquipment, convertToEnumField(typeOfEquipment));
        return "redirect:/admin/info-equipment/" + typeOfEquipment;
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteOneEquipment(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                     @PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/" + typeOfEquipment;
    }

    // ----- search -----
    @GetMapping("/search")
    public String showAllEquipmentBySearch(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                           @RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                equipmentService.showEquipmentBySearch(search, convertToEnumField(typeOfEquipment)));
        model.addAttribute("search", search);
        return "admin/equipment/show_all";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllEquipmentByParameter(@PathVariable("typeOfEquipment") String typeOfEquipment,
                                              @RequestParam("parameter") String parameter,
                                              @RequestParam("sortDirection") String sortDirection,
                                              Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, convertToEnumField(typeOfEquipment)));
        return "admin/equipment/show_all";
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
