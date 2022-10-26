package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Helmet;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.HELMET;

@Controller
@RequestMapping("/admin/info-equipment/helmet")
public class HelmetController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;
    private final String typeOfEquipment = HELMET.name().toLowerCase().replace('_', '-');

    @Autowired
    public HelmetController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllHelmets(Model model) {
        model.addAttribute("allEquipment", (List<Helmet>) equipmentService.showAllEquipment(HELMET));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewHelmet(Model model) {
        model.addAttribute("newEquipment", new Helmet());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewHelmetToDB(@ModelAttribute("newEquipment") @Valid Helmet helmet,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) helmet, HELMET);
        return "redirect:/admin/info-equipment/helmet";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneHelmet(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateHelmet(@PathVariable("equipmentId") Long equipmentId,
                               @ModelAttribute("equipmentToUpdate") @Valid Helmet updatedHelmet,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedHelmet, HELMET);
        return "redirect:/admin/info-equipment/helmet";
    }

    // ---------- delete --------
    @DeleteMapping("/{equipmentId}")
    public String deleteHelmet(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/helmet";
    }

    //------------search---------
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showHelmetsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<Helmet>) equipmentService.showEquipmentBySearch(search, HELMET));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    //-----------sort-------------
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllHelmetsByParameter(@RequestParam("parameter") String parameter,
                                            @RequestParam("sortDirection") String sortDirection,
                                            Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allHelmets",
                (List<Helmet>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, HELMET));
        return "admin/equipment/show_all";
    }
}
