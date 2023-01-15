package com.epam.ld.module2.testing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailServer {

  public void send(Notification notification) {
    String lineSeparator = System.lineSeparator();
    log.info(
        "Sending e-mail{}Subject: {}{}Body: {}{}{}{}{}{}{}{}Destination Addresses: {}",
        lineSeparator,
        notification.getSubject(),
        lineSeparator,
        lineSeparator,
        "<start body>",
        lineSeparator,
        notification.getBody(),
        lineSeparator,
        "<end body>",
        lineSeparator,
        lineSeparator,
        notification.getRecipients());
  }

}
