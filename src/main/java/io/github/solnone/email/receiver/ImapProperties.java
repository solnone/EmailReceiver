package io.github.solnone.email.receiver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "imap")
public class ImapProperties {

    private String url;

    private int interval = 5000;

    private boolean jsoup = false;
}
