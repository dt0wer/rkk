package ru.integrations.dto.clientservice;

import lombok.Data;

import java.util.Date;

@Data
public class RequestClient {

    private String name;

    private String surname;

    private String patronymic;

    private Date birthdate;

    private String documentType;

    private String documentSeries;

    private String documentNumber;
}
