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
    private final String typeOfEquipment = SNOWBOARD.name().toLowerCase().replace('_', '-');

    @Autowired
    public SnowboardController(EquipmentService<T> equipmentService) {
        this.equipmentService = equipmentService;
    }

    @ModelAttribute
    public void addToModel(Model model) {
        model.addAttribute("typeOfEquipment", typeOfEquipment);
    }

    // ----- show all -----
    @GetMapping()
    @SuppressWarnings("unchecked")
    public String showAllSnowboards(Model model) {
        model.addAttribute("allEquipment", (List<Snowboard>) equipmentService.showAllEquipment(SNOWBOARD));
        return "admin/equipment/show_all";
    }

    // ----- add new -----
    @GetMapping("/add-new")
    public String createNewSnowboard(Model model) {
        model.addAttribute("newEquipment", new Snowboard());
        return "admin/equipment/add_new";
    }

    @PostMapping("/add-new")
    @SuppressWarnings("unchecked")
    public String addNewSnowboardToDB(@ModelAttribute("newEquipment") @Valid Snowboard snowboard,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/add_new";
        }
        equipmentService.addNewEquipmentToDB((T) snowboard, SNOWBOARD);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- edit -----
    @GetMapping("/edit/{equipmentId}")
    public String showOneSnowboard(@PathVariable("equipmentId") Long equipmentId, Model model) {
        model.addAttribute("equipmentToUpdate", equipmentService.showOneEquipmentById(equipmentId));
        return "admin/equipment/edit";
    }

    @PatchMapping("/edit/{equipmentId}")
    @SuppressWarnings("unchecked")
    public String updateSnowboard(@PathVariable("equipmentId") Long equipmentId,
                                  @ModelAttribute("equipmentToUpdate") @Valid Snowboard updatedSnowboard,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/equipment/edit";
        }
        equipmentService.updateEquipmentById(equipmentId, (T) updatedSnowboard, SNOWBOARD);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- delete -----
    @DeleteMapping("/{equipmentId}")
    public String deleteSnowboard(@PathVariable("equipmentId") Long equipmentId) {
        equipmentService.deleteEquipmentById(equipmentId);
        return "redirect:/admin/info-equipment/snowboard";
    }

    // ----- search -----
    @GetMapping("/search")
    @SuppressWarnings("unchecked")
    public String showSnowboardsBySearch(@RequestParam("search") String search, Model model) {
        model.addAttribute("equipmentBySearch",
                (List<Snowboard>) equipmentService.showEquipmentBySearch(search, SNOWBOARD));
        model.addAttribute("search", search);
        return "admin/equipment/search";
    }

    // ----- sort -----
    @GetMapping("/sort")
    @SuppressWarnings("unchecked")
    public String sortAllSnowboardsByParameter(@RequestParam("parameter") String parameter,
                                               @RequestParam("sortDirection") String sortDirection,
                                               Model model) {
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("allEquipment",
                (List<Snowboard>) equipmentService.sortAllEquipmentByParameter(parameter, sortDirection, SNOWBOARD));
        return "admin/equipment/show_all";
    }
}
