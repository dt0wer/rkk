package ru.integrations.dto.bpm;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.integrations.commons.Message;

@Data
@AllArgsConstructor
public class StartProcessRequestDto implements Message {

    private String processKey;

    public StartProcessRequestDto() {
    }
}
