package ru.integrations.dto.bpm;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class CompleteTasksRequestDto implements Message {

    private String[] ids;

    public CompleteTasksRequestDto() {
    }
}
