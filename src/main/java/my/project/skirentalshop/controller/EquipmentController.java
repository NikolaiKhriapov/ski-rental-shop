package my.project.skirentalshop.controller;

import my.project.skirentalshop.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static my.project.skirentalshop.entity.enums.EquipmentCondition.BROKEN;
import static my.project.skirentalshop.entity.enums.EquipmentCondition.SERVICE;

@Controller
@RequestMapping("/admin/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    // ----- show broken equipment -----
    @GetMapping("/broken")
    public String showBroken(Model model) {
        model.addAttribute("equipmentCondition", BROKEN);
        model.addAttribute("listOfEquipment", equipmentService.showEquipmentByCondition(BROKEN));
        return "equipment";
    }

    // ----- show equipment in service -----
    @GetMapping("/in-service")
    public String showInService(Model model) {
        model.addAttribute("equipmentCondition", SERVICE);
        model.addAttribute("listOfEquipment", equipmentService.showEquipmentByCondition(SERVICE));
        return "equipment";
    }
}
