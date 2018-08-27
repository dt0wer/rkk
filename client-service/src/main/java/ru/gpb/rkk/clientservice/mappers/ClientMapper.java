package ru.gpb.rkk.clientservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gpb.rkk.clientservice.domain.ClientEntity;
import ru.integrations.dto.clientservice.RequestClient;

@Mapper (componentModel="spring")
public interface ClientMapper {

    RequestClient clientEntityToRequestClient(ClientEntity clientEntity);

    @Mapping(target = "version", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    ClientEntity requestClientDtoToClientEntity(RequestClient requestClient);
}
