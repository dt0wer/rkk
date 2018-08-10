package ru.integrations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class ResponseApplicationDto implements Message {

    private String response;

    public ResponseApplicationDto() {
    }
}
