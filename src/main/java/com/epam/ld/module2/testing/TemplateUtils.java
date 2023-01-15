package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.model.MessageTemplate;
import java.util.Collections;
import java.util.Map;

public class TemplateUtils {

  private TemplateUtils() {

  }

  public static Message generateMessage(MessageTemplate template, Map<String, String> templateValues) {
    return null;
  }

  public static Map<String, String> readTemplateValues() {
    return Collections.emptyMap();
  }

  public static Map<String, String> readTemplateValues(String filename) {
    return Collections.emptyMap();
  }

  public static Long readMessageTemplateId() {
    return null;
  }

  public static void writeMessageToFile(Message message, String filename) { }

    public record Message(String subject, String body) { }

}
