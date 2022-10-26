package my.project.skirentalshop.controller.admin;

import my.project.skirentalshop.model.Equipment;
import my.project.skirentalshop.model.Snowboard;
import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static my.project.skirentalshop.model.enums.TypesOfEquipment.SNOWBOARD;

@Controller
@RequestMapping("/admin/info-equipment/snowboard")
public class SnowboardController<T extends Equipment> {

    private final EquipmentService<T> equipmentService;

    @Autowired
    public SnowboardController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSnowboards(Model model) {
        model.addAttribute("typeOfEquipment", "snowboard");
        model.addAttribute("allSnowboards", (List<Snowboard>) equipmentService.showAllEquipment(SNOWBOARD));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSnowboard(Model model) {
        model.addAttribute("newSnowboard", new Snowboard());
        return "admin/equipment/add_new";
    }

    @PostMapping()
    @SuppressWarnings("unchecked")
    public String addNewSnowboardToDB(@ModelAttribute("newSnowboard") @Valid Snowboard snowboard,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) snowboard, SNOWBOARD);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- edit -----
    @GetMapping("/edit/{id}")
    public String showOneSnowboard(@PathVariable("id") Long id, Model model) {
        model.addAttribute("snowboardToUpdate", equipmentService.showOneEquipmentById(id));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{id}")
    @SuppressWarnings("unchecked")
    public String updateSnowboard(@PathVariable("id") Long id,
                                  @ModelAttribute("snowboardToUpdate") @Valid Snowboard updatedSnowboard,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(id, (T) updatedSnowboard, SNOWBOARD);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- delete -----
    @DeleteMapping("/{id}")
    public String deleteSnowboard(@PathVariable("id") Long id) {
        equipmentService.deleteEquipmentById(id);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- search -----
    @GetMapping("/search")
    public String showSnowboardsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("snowboardsBySearch", equipmentService.showEquipmentBySearch(search, SNOWBOARD));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    public String sortAllSnowboardsByParameter(@RequestParam("parameter") String parameter,
                                               @RequestParam("sortDirection") String sortDirection,
                                               Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allSnowboards", equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SNOWBOARD));
        return "admin/equipment/show_all";
    }
}
