package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Jacket;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.JACKET;

@Controller
@RequestMapping("/admin/info-equipment/jacket")
public class JacketController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;
    private final String typeOfEquipment = JACKET.name().toLowerCase().replace('_', '-');

    @Autowired
    public JacketController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllJackets(Model model) {
        model.addAttribute("allEquipment", (List<Jacket>) equipmentService.showAllEquipment(JACKET));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewJacket(Model model) {
        model.addAttribute("newEquipment", new Jacket());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewJacketToDB(@ModelAttribute("newEquipment") @Valid Jacket jacket,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) jacket, JACKET);
        return "redirect:/admin/info-equipment/jacket";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneJacket(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateJacket(@PathVariable("equipmentId") Long equipmentId,
                               @ModelAttribute("equipmentToUpdate") @Valid Jacket updatedJacket,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedJacket, JACKET);
        return "redirect:/admin/info-equipment/jacket";
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteJacket(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/jacket";
    }

    // ----- search -----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showJacketsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<Jacket>) equipmentService.showEquipmentBySearch(search, JACKET));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllJacketsByParameter(@RequestParam("parameter") String parameter,
                                            @RequestParam("sortDirection") String sortDirection,
                                            Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allJackets",
                (List<Jacket>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, JACKET));
        return "admin/equipment/show_all";
    }
}
