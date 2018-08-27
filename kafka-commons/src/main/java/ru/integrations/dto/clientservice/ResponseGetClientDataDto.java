package ru.integrations.dto.clientservice;

import lombok.Data;
import ru.integrations.commons.Message;

@Data
public class ResponseGetClientDataDto implements Message {
    private String sourceSystemName;

    private String sourceRequestId;

    private ClientData clientData;

    private Error error;

}
