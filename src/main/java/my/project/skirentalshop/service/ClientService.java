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

    public void updateClientById(Long id, Client updatedClient) {
        Client clientToBeUpdated = showOneClientById(id);

        clientToBeUpdated.setName(updatedClient.getName());
        clientToBeUpdated.setPhone1(updatedClient.getPhone1());
        clientToBeUpdated.setPhone2(updatedClient.getPhone2());

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

    // ----- show client by email -----
    public Client showOneClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    // ----- update user (CLIENT) info -----
    public void updateUserInfo(Client clientToBeUpdated, RegistrationRequest registrationRequest) {

        clientToBeUpdated.setName(registrationRequest.getName() + ' ' + registrationRequest.getSurname());
        clientToBeUpdated.setPhone1(registrationRequest.getPhone1());
        clientToBeUpdated.setPhone2(registrationRequest.getPhone2());
        clientToBeUpdated.setEmail(registrationRequest.getEmail());

        clientRepository.save(clientToBeUpdated);
    }
}
