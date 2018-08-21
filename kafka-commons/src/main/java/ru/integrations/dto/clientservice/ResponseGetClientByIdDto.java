package ru.integrations.dto.clientservice;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class ResponseGetClientByIdDto {

    private ClientDto response;

    public ResponseGetClientByIdDto() {
    }
}
