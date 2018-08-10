package ru.integrations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class RequestApplicationDto implements Message {

    private String applicationId;

    public RequestApplicationDto() {
    }
}
