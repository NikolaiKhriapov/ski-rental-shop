package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Helmet;
import my.project.skirentalshop.model.Snowboard;
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

    @Autowired
    public HelmetController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllHelmets(Model model) {
        model.addAttribute("typeOfEquipment", "helmet");
        model.addAttribute("allHelmets", (List<Helmet>) equipmentService.showAllEquipment(HELMET));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewHelmet(Model model) {
        model.addAttribute("newHelmet", new Helmet());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewHelmetToDB(@ModelAttribute("newHelmet") @Valid Helmet helmet,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) helmet, HELMET);
        return "redirect:/admin/info-equipment/helmet";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneHelmet(@PathVariable("id") Long id, Model model) {
        model.addAttribute("helmetToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateHelmet(@PathVariable("id") Long id,
                               @ModelAttribute("helmetToUpdate") @Valid Helmet updatedHelmet,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedHelmet, HELMET);
        return "redirect:/admin/info-equipment/helmet";
    }

    // ---------- delete --------
    @DeleteMapping("/{id}")
    public String deleteHelmet(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/helmet";
    }

    //------------search---------
    @GetMapping("/search")
    public String showHelmetsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("helmetBySearch", equipmentService.showEquipmentBySearch(search, HELMET));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    //-----------sort-------------
    @GetMapping("/sort")
    public String sortAllHelmetsByParameter(@RequestParam("parameter") String parameter,
                                            @RequestParam("sortDirection") String sortDirection,
                                            Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allHelmets", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, HELMET));
        return "admin/equipment/show_all";
    }
}
