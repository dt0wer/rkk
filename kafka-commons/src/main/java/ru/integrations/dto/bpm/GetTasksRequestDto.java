package ru.integrations.dto.bpm;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class GetTasksRequestDto implements Message {

    private String instanceId ;

    public GetTasksRequestDto() {
    }
}
