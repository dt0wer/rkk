package ru.integrations.dto.clientservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class ResponseGetClientIdDto implements Message {
    private Long response;

    public ResponseGetClientIdDto() {
    }
}
