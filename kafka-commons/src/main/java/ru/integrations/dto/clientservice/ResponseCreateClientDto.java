package ru.integrations.dto.clientservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;
@Data
@AllArgsConstructor
public class ResponseCreateClientDto implements Message {
    private ClientDto response;
}
