package ru.gpb.rkk.clientservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gpb.rkk.clientservice.domain.Client;

import java.util.Date;


@Repository
public interface ClientRepository extends  CrudRepository<Client, Long> {
    @Query("SELECT c FROM client c WHERE c.name=:name and c.surname=:surname and patronymic=:patronymic and " +
            "document_type:=document_type and document_series=:document_series and " +
            "document_number=:document_number and birth_date=:birth_date")
    Client getClientIdByNameBirthDateAndIdentityDoc(String firstName,
                                                         String lastName,
                                                         String patronymicName,
                                                         String DocumentType,
                                                         String documentSeries,
                                                         String documentNumber,
                                                         Date birthDate);
}
