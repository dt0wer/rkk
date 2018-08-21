package ru.integrations.dto.clientservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class RequestGetClientByIdDto implements Message {

    private String applicationId;

    private Long request;

    public RequestGetClientByIdDto() {
    }
}
