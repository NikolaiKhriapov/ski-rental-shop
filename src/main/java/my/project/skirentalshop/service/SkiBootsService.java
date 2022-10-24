package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.SkiBootsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkiBootsService {

    private final SkiBootsRepository skiBootsRepository;

    @Autowired
    public SkiBootsService(SkiBootsRepository skiBootsRepository) {
        this.skiBootsRepository = skiBootsRepository;
    }

    // ----- show all -----
    public List<SkiBoots> showAllSkiBoots() {
        return skiBootsRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewSkiBootsToDB(SkiBoots skiBoots) {
        skiBootsRepository.save(skiBoots);
    }

    // ----- edit -----
    public SkiBoots showOneSkiBootsById(Long id) {
        return skiBootsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("SkiBoots with id = " + id + " not found!"));
    }

    public void updateSkiBootsById(Long id, SkiBoots updatedSkiBoots) {
        SkiBoots skiBootsToBeUpdated = showOneSkiBootsById(id);

        skiBootsToBeUpdated.setName(updatedSkiBoots.getName());
        skiBootsToBeUpdated.setCondition(updatedSkiBoots.getCondition());
        skiBootsToBeUpdated.setSize(updatedSkiBoots.getSize());
        skiBootsToBeUpdated.setStiffness(updatedSkiBoots.getStiffness());

        skiBootsRepository.save(skiBootsToBeUpdated);
    }

    // ----- delete -----
    public void deleteSkiBootsById(Long id) {
        skiBootsRepository.deleteById(id);
    }

    // ----- search -----
    public List<SkiBoots> showSkiBootsBySearch(String search) {
        return skiBootsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<SkiBoots> sortAllSkiBootsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return skiBootsRepository.findAll(sort);
    }
}
