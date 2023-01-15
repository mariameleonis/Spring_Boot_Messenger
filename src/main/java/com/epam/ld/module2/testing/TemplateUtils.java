package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.model.MessageTemplate;
import java.util.Collections;
import java.util.Map;
import lombok.val;

public class TemplateUtils {

  private TemplateUtils() {

  }

  public static Message generateMessage(MessageTemplate template, Map<String, String> templateValues) {
    val subject = getContent("templates/subjects/" + template.getSubjectTemplate());
    return new Message(subject, "body");
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

  static String getContent(String fileName) {
    return null;
  }

  public record Message(String subject, String body) { }

}
