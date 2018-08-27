package ru.gpb.rkk.clientservice.dao;

import ru.gpb.rkk.clientservice.domain.ClientEntity;

import java.util.Date;
import java.util.List;

public interface ClientDao {
    ClientEntity create (ClientEntity clientEntity);
    ClientEntity getById (Long id);
    ClientEntity update (ClientEntity clientEntity);
    List<ClientEntity> getClientsByNameBirthDateAndIdentityDoc(String firstName,
                                                               String lastName,
                                                               String patronymicName,
                                                               String documentType,
                                                               String documentSeries,
                                                               String documentNumber,
                                                               Date birthDate);
}
