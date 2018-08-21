package ru.integrations.dto.clientservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class RequestGetClientIdDto implements Message {

    private String applicationId;

    private ClientDto request;

    public RequestGetClientIdDto() {
    }
}
