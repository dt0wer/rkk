package ru.gpb.rkk.clientservice.service.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gpb.rkk.clientservice.domain.ClientEntity;
import ru.gpb.rkk.clientservice.dao.ClientDao;
import ru.gpb.rkk.clientservice.service.ClientService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.text.MessageFormat;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {
    private  ClientDao clientDao;

    public ClientServiceImpl(@Autowired ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public ClientEntity save(ClientEntity clientEntity) {
        return clientDao.update(clientEntity);
    }

    @Override
    public List<Long> getClientIdsByNameBirthDateAndIdentityDoc(String firstName, String lastName, String patronymicName, String documentType, String documentSeries, String documentNumber, Date birthDate) {
        List<ClientEntity> clients = clientDao.getClientsByNameBirthDateAndIdentityDoc(firstName, lastName, patronymicName,
                documentType, documentSeries, documentNumber, birthDate);
        if (clients.size()>1) {
            String message = MessageFormat.format("There are several records for a client {0} {1} {2} with attributes date of birth: {3}, document type: {4},  document series: {5}, document number: {}.",
                    lastName, firstName, patronymicName, birthDate, documentType, documentSeries, documentNumber);
            log.warn(message);
            clients.stream().forEach(client -> log.warn(client.toString()));
        }
        return clients.stream().map(client -> client.getClientId()).collect(Collectors.toList());
    }

    @Override
    public ClientEntity getClientById(Long id) {
        return clientDao.getById(id);
    }
}
