package com.epam.ld.module2.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.epam.ld.module2.testing.TemplateUtils.Message;
import com.epam.ld.module2.testing.exception.TemplateException;
import com.epam.ld.module2.testing.exception.TemplateNotFoundException;
import com.epam.ld.module2.testing.model.MessageTemplate;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

@ExtendWith(MockitoExtension.class)
class TemplateUtilsTest {

  @Test
  void generateMessage_shouldNeverReturnNull() throws TemplateException, IOException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result);
  }

  @Test
  void generateMessage_messageSubjectShouldNeverBeNull() throws TemplateException, IOException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result.subject());
  }

  @Test
  void generateMessage_messageSubjectShouldNeverBeBlank() throws TemplateException, IOException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertFalse(result.subject().isBlank());
  }

  @Test
  void generateMessage_messageBodyShouldNeverBeNull() throws TemplateException, IOException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result.body());
  }

  @Test
  void generateMessage_messageBodyShouldNeverBeBlank() throws TemplateException, IOException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertFalse(result.body().isBlank());
  }

  @Test
  void generateMessage_shouldReturnSubjectTemplateContentIfNoPlaceholders()
      throws TemplateException, IOException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .build();

    val values = new HashMap<String, String>();
    val fileName = "templates/subjects/" + subjectTemplate;
    val expected = "template content";

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(fileName)).thenReturn(expected);
      assertEquals(expected, TemplateUtils.generateMessage(template, values).subject());
    }
  }

  @Test
  void generateMessage_shouldOverrideSubjectTemplateWithProvidedValue()
      throws TemplateException, IOException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .build();

    val values = Map.of(
        "name", "Mariya");
    val fileName = "templates/subjects/" + subjectTemplate;
    val expectedContent = "Hello, ${name}!";
    val expectedSubject = "Hello, Mariya!";
    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(fileName)).thenReturn(expectedContent);
      assertEquals(expectedSubject, TemplateUtils.generateMessage(template, values).subject());
    }
  }

  @Test
  void generateMessage_shouldThrowTemplateExceptionWhenTemplateValueNotFound() {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .build();

    val values = new HashMap<String, String>();
    val fileName = "templates/subjects/" + subjectTemplate;
    val expectedContent = "Hello, ${name}!";
    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(fileName)).thenReturn(expectedContent);
      assertThrows(TemplateException.class, () -> TemplateUtils.generateMessage(template, values));
    }
  }

  @Test
  void generateMessage_shouldOverrideBodyTemplateWithProvidedValue()
      throws TemplateException, IOException {
    val templateFile = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(templateFile)
        .bodyTemplate(templateFile)
        .build();

    val values = Map.of(
        "name", "Mariya");

    val expectedContent = "Hello, ${name}!";
    val expectedBody = "Hello, Mariya!";
    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class,
        Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(expectedContent);
      assertEquals(expectedBody, TemplateUtils.generateMessage(template, values).body());
    }
  }

  @Test
  void generateMessage_shouldOverrideTemplateWithProvidedValue()
      throws TemplateException, IOException {
    val tempFileName = "test_template_05878543215896.txt";
    val subjectTemplate = new File("target/classes/templates/subjects/"
            + tempFileName);
    val bodyTemplate = new File("target/classes/templates/"
            + tempFileName);
    val content = "Hello, ${name}!";

    try (val subjectWriter = new FileWriter(subjectTemplate)) {
      subjectWriter.write(content);
    }

    try (val bodyWriter = new FileWriter(bodyTemplate)) {
      bodyWriter.write(content);
    }

    val template = MessageTemplate.builder()
        .subjectTemplate(tempFileName)
        .bodyTemplate(tempFileName)
        .build();

    val values = Map.of(
        "name", "Mariya");

    val expected = "Hello, Mariya!";
    val expectedMessage = new Message(expected, expected);
    val message = TemplateUtils.generateMessage(template, values);

    Files.delete(subjectTemplate.toPath());
    Files.delete(bodyTemplate.toPath());

    assertEquals(expectedMessage, message);
  }

  @Test
  void generateMessage_shouldThrowTemplateNotFoundException() {
    val tempFileName = "test_template_05878543215896.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(tempFileName)
        .bodyTemplate(tempFileName)
        .build();

    val values = new HashMap<String, String>();

    assertThrows(
        TemplateNotFoundException.class, () -> TemplateUtils.generateMessage(template, values));

  }

  @Test
  void readTemplateValues_shouldReadValidUserInputAndReturnMap() {
    System.setIn(new ByteArrayInputStream("key1=value1\nkey2=value2\n".getBytes()));

    Map<String, String> result = TemplateUtils.readTemplateValues();

    assertEquals("value1", result.get("key1"));
    assertEquals("value2", result.get("key2"));

    System.setIn(System.in);
  }

  @Test
  void readTemplateValues_shouldReadFromFileAndReturnMap() throws IOException {
    String fileName = null;
    Path path = null;
    try {
      path = Files.createTempFile("myTempFile", ".txt");
      fileName = path.toFile().getAbsolutePath();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String content = """
        key1=value1
        key2=value2
        """;

    try (val writer = new FileWriter(path.toFile())) {
      writer.write(content);
    }

    Map<String, String> result = TemplateUtils.readTemplateValues(fileName);

    assertEquals("value1", result.get("key1"));
    assertEquals("value2", result.get("key2"));
  }

  @Test
  void readMessageTemplateId_shouldNeverReturnNull() {
    val result = TemplateUtils.readMessageTemplateId();
    assertNotNull(result);
  }

  @Test
  void readMessageTemplateId_shouldReadFromConsoleAndReturnUserChoice() {
    System.setIn(new ByteArrayInputStream("3".getBytes()));
    val result = TemplateUtils.readMessageTemplateId();
    assertEquals(3L, result);
    System.setIn(System.in);
  }

  @Test
  void readMessageTemplateId_shouldLogErrorIfInputIsInvalid() {
    System.setIn(new ByteArrayInputStream("invalid".getBytes()));
    Logger log = (Logger) LoggerFactory.getLogger(TemplateUtils.class);
    val testLogAppender = new TestLogAppender();
    log.addAppender(testLogAppender);
    testLogAppender.start();

    TemplateUtils.readMessageTemplateId();

    val lastLoggedEvent = testLogAppender.getLastLoggedEvent();

    assertEquals("Invalid input. Please enter a valid long value.", lastLoggedEvent.getMessage());

    System.setIn(System.in);
  }

}