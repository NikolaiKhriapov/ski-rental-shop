package my.project.skirentalshop.service;

import my.project.skirentalshop.entity.Client;
import my.project.skirentalshop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ResourceBundle;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> showAllClients() {
        return clientRepository.findAllByOrderById();
    }

    public void addNewClientToDB(Client client) {
        clientRepository.save(client);
    }

    public Client showOneClientById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() ->
                new IllegalStateException(
                        getExceptionMessage("exception.client.id-not-found", clientId.toString())
                )
        );
    }

    public void updateClientById(Long clientId, Client updatedClientInfo) {
        Client clientToBeUpdated = showOneClientById(clientId);

        clientToBeUpdated.setName(updatedClientInfo.getName());
        clientToBeUpdated.setPhone1(updatedClientInfo.getPhone1());
        clientToBeUpdated.setPhone2(updatedClientInfo.getPhone2());

        clientRepository.save(clientToBeUpdated);
    }

    public void deleteClientById(Long clientId) {
        try {
            clientRepository.deleteById(clientId);
        } catch (DataIntegrityViolationException e) {
            String clientName = showOneClientById(clientId).getName();
            throw new DataIntegrityViolationException(
                    getExceptionMessage("exception.client.cannot-be-deleted", clientName)
            );
        }
    }

    public List<Client> showClientsBySearch(String search) {
        return clientRepository.findAllByNameContainingIgnoreCaseOrPhone1ContainingIgnoreCaseOrPhone2ContainingIgnoreCase(
                search, search, search);
    }

    public List<Client> sortAllClientsByParameter(String parameter, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(parameter).ascending() : Sort.by(parameter).descending();
        return clientRepository.findAll(sort);
    }

    public String getExceptionMessage(String propertyKey, String parameter) {
        return String.format(
                ResourceBundle
                        .getBundle("exception", LocaleContextHolder.getLocale())
                        .getString(propertyKey),
                parameter
        );
    }
}
