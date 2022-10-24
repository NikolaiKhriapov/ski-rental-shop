package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.PantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PantsService {

    private final PantsRepository pantsRepository;

    @Autowired
    public PantsService(PantsRepository pantsRepository) {
        this.pantsRepository = pantsRepository;
    }

    // ----- show all -----
    public List<Pants> showAllPants() {
        return pantsRepository.findAllByOrderById();
    }

    //------add new-----
    public void addNewPantsToDB(Pants pants) {
        pantsRepository.save(pants);
    }

    // ----- edit -----
    public Pants showOnePantsById(Long id) {
        return pantsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Pants with id = " + id + "not found!"));
    }

    public void updatePantsById(Long id, Pants updatedPants) {
        Pants pantsToBeUpdated = showOnePantsById(id);

        pantsToBeUpdated.setName(updatedPants.getName());
        pantsToBeUpdated.setCondition(updatedPants.getCondition());
        pantsToBeUpdated.setSize(updatedPants.getSize());

        pantsRepository.save(pantsToBeUpdated);
    }

    //------delete------
    public void deletePantsById(Long id) {
        pantsRepository.deleteById(id);
    }

    // ----- search -----
    public List<Pants> showPantsBySearch(String search) {
        return pantsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<Pants> sortAllPantsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return pantsRepository.findAll(sort);
    }
}
