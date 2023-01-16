package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.exception.TemplateException;
import com.epam.ld.module2.testing.exception.TemplateNotFoundException;
import com.epam.ld.module2.testing.model.MessageTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
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
    val scanner = new Scanner(System.in);
    log.info("Enter the values in the format 'key=value':");
    Map<String, String> values = new HashMap<>();
    String line;
    while (scanner.hasNextLine()) {
      line = scanner.nextLine();
      String[] parts = line.split("=");
      values.put(parts[0], parts[1]);
    }
    return values;
  }

  public static Map<String, String> readTemplateValues(String filename) {
    Map<String, String> map = new HashMap<>();
    try (val br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split("=");
        map.put(parts[0], parts[1]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }

  public static Long readMessageTemplateId() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      try {
        return Long.parseLong(scanner.nextLine());
      } catch (NumberFormatException e) {
        log.error("Invalid input. Please enter a valid long value.");
      }
    }
  }

  public static void writeMessageToFile(Message message, String filename) throws IOException {
    val file = new File(filename);
    if (!file.exists()) {
      file.createNewFile();
    }
    try (val writer = new FileWriter(file, true)) {
      writer.write("Subject: " + message.subject + "\nBody: " + message.body);
    }
  }

  @SneakyThrows
  static String getContent(String fileName) {
    String content;
    try (val is = TemplateUtils.class.getClassLoader().getResourceAsStream(fileName)) {
      content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (NullPointerException e) {
      throw new TemplateNotFoundException("Template associated with file " + fileName + "cannot be found.");
    }

    return content;
  }

  public record Message(String subject, String body) { }

}
