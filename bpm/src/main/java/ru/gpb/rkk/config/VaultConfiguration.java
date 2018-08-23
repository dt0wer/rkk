package ru.gpb.rkk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 *
 * Vault properties. Example contains sample of read encrypted data from vault server.
 *
 * Encrypted information MUST be add to vault storage backend (we use consul)
 *
 * Vault command for this example:
 *
 * vault write secret/customers customers.username=demouser customers.password=demopassword
 *
 */
@Component
@ConfigurationProperties("bpm")
public class VaultConfiguration {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}