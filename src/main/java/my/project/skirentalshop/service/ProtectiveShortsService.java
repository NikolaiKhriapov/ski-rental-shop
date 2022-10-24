package my.project.skirentalshop.service;

import my.project.skirentalshop.model.*;
import my.project.skirentalshop.repository.ProtectiveShortsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProtectiveShortsService {

    private final ProtectiveShortsRepository protectiveShortsRepository;

    @Autowired
    public ProtectiveShortsService(ProtectiveShortsRepository protectiveShortsRepository) {
        this.protectiveShortsRepository = protectiveShortsRepository;
    }

    // ----- show all -----
    public List<ProtectiveShorts> showAllProtectiveShorts() {
        return protectiveShortsRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewProtectiveShortsToDB(ProtectiveShorts protectiveShorts) {
        protectiveShortsRepository.save(protectiveShorts);
    }

    // ----- edit -----
    public ProtectiveShorts showOneProtectiveShortsById(Long id) {
        return protectiveShortsRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("ProtectiveShorts with id = " + id + " not found!"));
    }

    public void updateProtectiveShortsById(Long id, ProtectiveShorts updatedProtectiveShorts) {
        ProtectiveShorts protectiveShortsToBeUpdated = showOneProtectiveShortsById(id);

        protectiveShortsToBeUpdated.setName(updatedProtectiveShorts.getName());
        protectiveShortsToBeUpdated.setCondition(updatedProtectiveShorts.getCondition());
        protectiveShortsToBeUpdated.setSize(updatedProtectiveShorts.getSize());

        protectiveShortsRepository.save(protectiveShortsToBeUpdated);
    }

    // ----- delete -----
    public void deleteProtectiveShortsById(Long id) {
        protectiveShortsRepository.deleteById(id);
    }

    // ----- search -----
    public List<ProtectiveShorts> showProtectiveShortsBySearch(String search) {
        return protectiveShortsRepository.findAllByNameContainingIgnoreCase(search);
    }

    // ----- sort -----
    public List<ProtectiveShorts> sortAllProtectiveShortsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return protectiveShortsRepository.findAll(sort);
    }
}
