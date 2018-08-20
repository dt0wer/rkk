package ru.gpb.rkk.clientservice.service;

import ru.gpb.rkk.clientservice.domain.Client;

import java.util.Date;
import java.util.List;

public interface ClientService {
    public List<Client> findAll();
    public Client save(Client client);
    public Long getClientIdByNameBirthDateAndIdentityDoc(String firstName,
                                                    String lastName,
                                                    String patronymicName,
                                                    String DocumentType,
                                                    String documentSeries,
                                                    String documentNumber,
                                                    Date birthDate);
}
