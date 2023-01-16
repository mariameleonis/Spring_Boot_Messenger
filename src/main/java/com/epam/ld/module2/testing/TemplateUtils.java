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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class TemplateUtils {

  private static final String TEMPLATE_REGEX = "\\$\\{([^}]+)\\}";
  public static final String STOP_WORD = "done";

  private TemplateUtils() {

  }

  public static Message generateMessage(MessageTemplate template, Map<String, String> templateValues)
      throws TemplateException {
    val subjectTemplateContent = getContent("templates/subjects/" + template.getSubjectTemplate());
    val bodyTemplateContent = getContent("templates/" + template.getBodyTemplate());
    val subject = overrideTemplateContent(subjectTemplateContent, templateValues);
    val body = overrideTemplateContent(bodyTemplateContent, templateValues);
    return new Message(subject, body);
  }

  private static String overrideTemplateContent(String templateContent,
      Map<String, String> templateValues) throws TemplateException {
    Map<String, String> encodedValues = new HashMap<>();
    var result = templateContent;
    val pattern = Pattern.compile(TEMPLATE_REGEX);
    val matcher = pattern.matcher(templateContent);
    while (matcher.find()) {
      val placeHolder = matcher.group(1);
      if (!templateValues.containsKey(placeHolder)) {
        throw new TemplateException("No value provided for placeholder: " + placeHolder);
      }

      val value = templateValues.get(placeHolder);
      if (value.matches(TEMPLATE_REGEX)) {
        encodedValues.put(matcher.group(), Base64.getEncoder().encodeToString(value.getBytes()));
        continue;
      }
      result = result.replaceFirst(Pattern.quote(matcher.group()), templateValues.get(placeHolder));
    }
    if (!encodedValues.isEmpty()) {
      for (Map.Entry<String, String> entry : encodedValues.entrySet()) {
        result  = result.replace(entry.getKey(), new String(Base64.getDecoder().decode(entry.getValue())));
      }
    }
    return result;
  }

  public static Map<String, String> readTemplateValues() {
    val scanner = new Scanner(System.in);
    log.info("Enter the values in the format 'key=value'. Type 'done' to finish:");
    Map<String, String> values = new HashMap<>();
    String line;
    while (scanner.hasNextLine()) {
      line = scanner.nextLine();
      if (line.equals(STOP_WORD)) {
        break;
      }
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
    val scanner = new Scanner(System.in);
    log.info("Please, enter template id:");
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

  static String getContent(String fileName) throws TemplateNotFoundException {
    String content;
    try (val is = TemplateUtils.class.getClassLoader().getResourceAsStream(fileName)) {
      content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    } catch (NullPointerException | IOException e) {
      throw new TemplateNotFoundException("Template associated with file " + fileName + "cannot be found.");
    }

    return content;
  }

  public record Message(String subject, String body) { }

}
