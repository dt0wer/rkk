package ru.integrations.dto.clientservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class RequestGetClientDataDto implements Message {

    private String sourceSystemName;

    private String sourceRequestId;

    private RequestClient requestClient;

    public RequestGetClientDataDto() {
    }
}
