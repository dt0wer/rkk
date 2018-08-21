package ru.gpb.rkk.clientservice.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gpb.rkk.clientservice.domain.Client;
import ru.gpb.rkk.clientservice.repository.ClientRepository;
import ru.gpb.rkk.clientservice.service.ClientService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> findAll() {
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Long getClientIdByNameBirthDateAndIdentityDoc(String firstName, String lastName, String patronymicName, String DocumentType, String documentSeries, String documentNumber, Date birthDate) {
        Client client = clientRepository.getClientIdByNameBirthDateAndIdentityDoc(firstName, lastName, patronymicName,
                documentNumber, documentSeries, documentNumber, birthDate);
        return client !=null ? client.getClientId() : null;
    }

    @Override
    public Client getCliendById(Long id) {
        Optional<Client> res = clientRepository.findById(id);
        return res.isPresent()? res.get() : null;
    }
}
