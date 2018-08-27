package ru.gpb.rkk.clientservice.service;

import ru.gpb.rkk.clientservice.domain.ClientEntity;

import java.util.Date;
import java.util.List;

public interface ClientService {
    public ClientEntity save(ClientEntity clientEntity);
    public List<Long> getClientIdsByNameBirthDateAndIdentityDoc(String firstName,
                                                                String lastName,
                                                                String patronymicName,
                                                                String documentType,
                                                                String documentSeries,
                                                                String documentNumber,
                                                                Date birthDate);
    public ClientEntity getClientById(Long id);
}
