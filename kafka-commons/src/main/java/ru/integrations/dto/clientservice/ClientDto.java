package ru.integrations.dto.clientservice;

import lombok.Data;

import java.util.Date;

@Data
public class ClientDto {
    private Long clientId;

    private String name;

    private String surname;

    private String patronymic;

    private Date birthDate;

    private String documentType;

    private String documentSeries;

    private String documentNumber;
}
