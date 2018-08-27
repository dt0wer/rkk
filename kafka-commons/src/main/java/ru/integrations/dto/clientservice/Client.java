package ru.integrations.dto.clientservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Client {
    private Long clientId;

    public Client() {
    }
}
