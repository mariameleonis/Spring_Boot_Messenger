package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.exception.TemplateException;
import com.epam.ld.module2.testing.model.MessageTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.val;

public class TemplateUtils {

  private static final String TEMPLATE_REGEX = "\\$\\{([^}]+)\\}";

  private TemplateUtils() {

  }

  public static Message generateMessage(MessageTemplate template, Map<String, String> templateValues)
      throws TemplateException, IOException {
    val subjectTemplateContent = getContent("templates/subjects/" + template.getSubjectTemplate());
    val bodyTemplateContent = getContent("templates/" + template.getBodyTemplate());
    val subject = overrideTemplateContent(subjectTemplateContent, templateValues);
    val body = overrideTemplateContent(bodyTemplateContent, templateValues);
    return new Message(subject, body);
  }

  private static String overrideTemplateContent(String templateContent,
      Map<String, String> templateValues) throws TemplateException {
    var result = templateContent;
    val pattern = Pattern.compile(TEMPLATE_REGEX);
    val matcher = pattern.matcher(templateContent);
    while (matcher.find()) {
      val placeHolder = matcher.group(1);
      if (!templateValues.containsKey(placeHolder)) {
        throw new TemplateException("No value provided for placeholder: " + placeHolder);
      }
      result = result.replaceFirst(Pattern.quote(matcher.group()), templateValues.get(placeHolder));
    }
    return result;
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

  static String getContent(String fileName) throws IOException {
    String content;
    try (val is = TemplateUtils.class.getClassLoader().getResourceAsStream(fileName)) {
      content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    return content;
  }

  public record Message(String subject, String body) { }

}
