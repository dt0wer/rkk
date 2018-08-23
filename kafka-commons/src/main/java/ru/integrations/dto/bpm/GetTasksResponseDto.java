package ru.integrations.dto.bpm;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;
import ru.integrations.dto.bpm.common.TaskDto;

import java.util.List;

@Data
@AllArgsConstructor
public class GetTasksResponseDto implements Message {

    private List<TaskDto> tasks;

    public GetTasksResponseDto() {
    }
}
