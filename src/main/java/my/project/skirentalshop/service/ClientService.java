package my.project.skirentalshop.service;

import my.project.skirentalshop.model.Client;
import my.project.skirentalshop.repository.ClientRepository;
import my.project.skirentalshop.security.registration.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // ----- show all -----
    public List<Client> showAllClients() {
        return clientRepository.findAllByOrderById();
    }

    // ----- add new -----
    public void addNewClientToDB(Client client) {
        clientRepository.save(client);
    }

    // ----- edit -----
    public Client showOneClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Client with id = " + id + " not found!"));
    }

    public void updateClientById(Long clientId, Client updatedClientInfo) {
        Client clientToBeUpdated = showOneClientById(clientId);

        clientToBeUpdated.setName(updatedClientInfo.getName());
        clientToBeUpdated.setPhone1(updatedClientInfo.getPhone1());
        clientToBeUpdated.setPhone2(updatedClientInfo.getPhone2());

        clientRepository.save(clientToBeUpdated);
    }

    // ----- delete -----
    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    // ----- search -----
    public List<Client> showClientsBySearch(String search) {
        return clientRepository.findAllByNameContainingIgnoreCaseOrPhone1ContainingIgnoreCaseOrPhone2ContainingIgnoreCase(
                search, search, search);
    }

    // ----- sort -----
    public List<Client> sortAllClientsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return clientRepository.findAll(sort);
    }


    ///TODO: remove
    // ----- ClientHomeController / update applicationUser info -----
    public void updateClientById(Long clientToBeUpdatedId, RegistrationRequest registrationRequest) {
        Client clientToBeUpdated = showOneClientById(clientToBeUpdatedId);

        clientToBeUpdated.setName(registrationRequest.getName());
        clientToBeUpdated.setPhone1(registrationRequest.getPhone1());
        clientToBeUpdated.setPhone2(registrationRequest.getPhone2());

        clientRepository.save(clientToBeUpdated);
    }
}
