package ru.gpb.rkk.clientservice.kafka;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.gpb.rkk.clientservice.domain.Client;
import ru.integrations.dto.clientservice.ClientDto;

@Mapper (componentModel = "cdi")
public interface ClientMapper {
    ClientDto clientToClientDto (Client client);
    Client clientDtoToClient (ClientDto clientDto);
}
