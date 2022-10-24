package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.SnowboardBootsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnowboardBootsService {

    private final SnowboardBootsRepository snowboardBootsRepository;

    @Autowired
    public SnowboardBootsService(SnowboardBootsRepository snowboardBootsRepository) {
        this.snowboardBootsRepository = snowboardBootsRepository;
    }

    // ----- show all -----
    public List<SnowboardBoots> showAllSnowboardBoots() {
        return snowboardBootsRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewSnowboardBootsToDB(SnowboardBoots snowboardBoots) {
        snowboardBootsRepository.save(snowboardBoots);
    }

    // ----- edit -----
    public SnowboardBoots showOneSnowboardBootsById(Long id) {
        return snowboardBootsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("snowboardBoots with id = " + id + " not found!"));
    }

    public void updateSnowboardBootsById(Long id, SnowboardBoots updatedSnowboardBoots) {
        SnowboardBoots snowboardBootsToBeUpdated = showOneSnowboardBootsById(id);

        snowboardBootsToBeUpdated.setName(updatedSnowboardBoots.getName());
        snowboardBootsToBeUpdated.setCondition(updatedSnowboardBoots.getCondition());
        snowboardBootsToBeUpdated.setSize(updatedSnowboardBoots.getSize());
        snowboardBootsToBeUpdated.setStiffness(updatedSnowboardBoots.getStiffness());

        snowboardBootsRepository.save(snowboardBootsToBeUpdated);
    }

    // ----- delete -----
    public void deleteSnowboardBootsById(Long id) {
        snowboardBootsRepository.deleteById(id);
    }

    // ----- search -----
    public List<SnowboardBoots> showSnowboardBootsBySearch(String search) {
        return snowboardBootsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<SnowboardBoots> sortAllSnowboardBootsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return snowboardBootsRepository.findAll(sort);
    }
}
