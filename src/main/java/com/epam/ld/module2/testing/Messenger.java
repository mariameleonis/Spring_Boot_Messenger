package com.epam.ld.module2.testing;

import static com.epam.ld.module2.testing.TemplateUtils.*;

import com.epam.ld.module2.testing.TemplateUtils.Message;
import com.epam.ld.module2.testing.exception.TemplateException;
import com.epam.ld.module2.testing.model.MessageTemplate;
import com.epam.ld.module2.testing.service.MessageTemplateService;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Messenger {

  public static final String DEFAULT_CLIENT_ADDRESS = "default@email.com";
  public static final String DEFAULT_TEMPLATE = "DEFAULT";
  private final MailServer mailServer;
  private final MessageTemplateService messageTemplateService;

  public void sendNotification(String... args) throws TemplateException, IOException {
    int length = args.length;
    if (length != 0 && length != 2) {
      throw new RuntimeException("Invalid arguments count");
    }
    MessageTemplate template;
    Map<String, String> templateValues;

    if (length == 0) {
      template = readTemplate();
      templateValues = readTemplateValues();
    } else {
      template = messageTemplateService.findByCode(DEFAULT_TEMPLATE);
      templateValues = readTemplateValues(args[0]);
    }

    if (template == null) {
      return;
    }

    val message = generateMessage(template, templateValues);

    if (message == null) {
      return;
    }

    if (length == 2) {
      writeMessageToFile(message, args[1]);
    }

    val client = getDefaultClient();

    mailServer.send(getNotification(client, template, message));
  }

  private MessageTemplate readTemplate() {
    val templates = messageTemplateService.findAll().stream()
        .collect(Collectors.toMap(MessageTemplate::getId, Function.identity()));
    displayMessageTemplates(templates);
    val templateId = readMessageTemplateId();
    return templates.get(templateId);
  }

  private void displayMessageTemplates(Map<Long, MessageTemplate> templates) {
    val joiner = new StringJoiner("\n");
    for (Map.Entry<Long, MessageTemplate> template : templates.entrySet()) {
      joiner.add(template.getKey() + " - " + template.getValue().getCode());
    }
    log.info("Existed message templates:\n" + joiner);
  }

  private Client getDefaultClient() {
    return Client.builder()
        .adresses(Set.of(DEFAULT_CLIENT_ADDRESS))
        .build();
  }

  private Notification getNotification(Client client, MessageTemplate template, Message message) {
    return Notification.builder()
        .id(UUID.randomUUID().toString())
        .type(template.getCode())
        .recipients(client.getAdresses())
        .body(message.body())
        .subject(message.subject())
        .build();
  }

}
