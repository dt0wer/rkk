package ru.gpb.rkk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response to client with data from vault
 */
@Data
@AllArgsConstructor
public class VaultDto {

    private String vaultUserName;
    private String vaultUserPassword;


}
