package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Jacket;
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

    @Autowired
    public JacketController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllJackets(Model model) {
        model.addAttribute("typeOfEquipment", "jacket");
        model.addAttribute("allJackets", (List<Jacket>) equipmentService.showAllEquipment(JACKET));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewJacket(Model model) {
        model.addAttribute("newJacket", new Jacket());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewJacketToDB(@ModelAttribute("newJacket") @Valid Jacket jacket,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) jacket, JACKET);
        return "redirect:/admin/info-equipment/jacket";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneJacket(@PathVariable("id") Long id, Model model) {
        model.addAttribute("jacketToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateJacket(@PathVariable("id") Long id,
                               @ModelAttribute("jacketToUpdate") @Valid Jacket updatedJacket,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedJacket, JACKET);
        return "redirect:/admin/info-equipment/jacket";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteJacket(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/jacket";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showJacketsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("jacketsBySearch", equipmentService.showEquipmentBySearch(search, JACKET));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllJacketsByParameter(@RequestParam("parameter") String parameter,
                                            @RequestParam("sortDirection") String sortDirection,
                                            Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allJackets", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, JACKET));
        return "admin/equipment/show_all";
    }
}
