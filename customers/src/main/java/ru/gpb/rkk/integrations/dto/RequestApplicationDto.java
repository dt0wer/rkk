package ru.gpb.rkk.integrations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.gpb.rkk.integrations.commons.Message;

@Data
@AllArgsConstructor
public class RequestApplicationDto implements Message {

    private String applicationId;

}
