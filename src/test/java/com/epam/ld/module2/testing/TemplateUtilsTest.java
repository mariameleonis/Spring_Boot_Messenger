package com.epam.ld.module2.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import ch.qos.logback.classic.Logger;
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
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
@ExtendWith(FileOutputExtension.class)
@Tag("branch-1")
class TemplateUtilsTest {

  @Test
  @Tag("feature-3")
  void generateMessage_shouldNeverReturnNull() throws TemplateException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .bodyTemplate(subjectTemplate)
        .build();

    val values = Map.of(
        "name", "${tag}");

    val content = "Hello, ${name}!";

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(content);
      assertNotNull(TemplateUtils.generateMessage(template, values));
    }
  }

  @Test
  @Tag("feature-3")
  void generateMessage_messageSubjectShouldNeverBeNull() throws TemplateException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .bodyTemplate(subjectTemplate)
        .build();

    val values = Map.of(
        "name", "${tag}");

    val content = "Hello, ${name}!";

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(content);
      assertNotNull(TemplateUtils.generateMessage(template, values).subject());
    }
  }

  @Test
  @Tag("feature-3")
  void generateMessage_messageSubjectShouldNeverBeBlank() throws TemplateException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .bodyTemplate(subjectTemplate)
        .build();

    val values = Map.of(
        "name", "${tag}");

    val content = "Hello, ${name}!";

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(content);
      assertFalse(TemplateUtils.generateMessage(template, values).subject().isBlank());
    }
  }

  @Test
  @Tag("feature-3")
  void generateMessage_messageBodyShouldNeverBeNull() throws TemplateException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .bodyTemplate(subjectTemplate)
        .build();

    val values = Map.of(
        "name", "${tag}");

    val content = "Hello, ${name}!";

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(content);
      assertNotNull(TemplateUtils.generateMessage(template, values).body());
    }
  }

  @Test
  @Tag("feature-3")
  void generateMessage_messageBodyShouldNeverBeBlank() throws TemplateException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .bodyTemplate(subjectTemplate)
        .build();

    val values = Map.of(
        "name", "${tag}");

    val content = "Hello, ${name}!";

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(content);
      assertFalse(TemplateUtils.generateMessage(template, values).body().isBlank());
    }
  }

  @Test
  @Tag("feature-3")
  void generateMessage_shouldReturnSubjectTemplateContentIfNoPlaceholders()
      throws TemplateException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .bodyTemplate(subjectTemplate)
        .build();

    val values = new HashMap<String, String>();

    val expected = "template content";

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(expected);
      assertEquals(expected, TemplateUtils.generateMessage(template, values).subject());
    }
  }

  @Test
  @Tag("feature-3")
  void generateMessage_shouldOverrideSubjectTemplateWithProvidedValue()
      throws TemplateException {
    val subjectTemplate = "subject.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(subjectTemplate)
        .bodyTemplate(subjectTemplate)
        .build();

    val values = Map.of(
        "name", "Mariya");

    val expectedContent = "Hello, ${name}!";
    val expectedSubject = "Hello, Mariya!";
    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class, Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent(anyString())).thenReturn(expectedContent);
      assertEquals(expectedSubject, TemplateUtils.generateMessage(template, values).subject());
    }
  }

  @Test
  @Tag("ExceptionRule")
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
  @Tag("UnitTest")
  void generateMessage_shouldOverrideBodyTemplateWithProvidedValue()
      throws TemplateException {
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
  @Tag("UnitTest")
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
  @Tag("ExceptionRule")
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
  @Tag("UnitTest")
  void readTemplateValues_shouldReadValidUserInputAndReturnMap() {
    System.setIn(new ByteArrayInputStream("key1=value1\nkey2=value2\n".getBytes()));

    Map<String, String> result = TemplateUtils.readTemplateValues();

    assertEquals("value1", result.get("key1"));
    assertEquals("value2", result.get("key2"));

    System.setIn(System.in);
  }

  @Test
  void readTemplateValues_shouldReadValidUserInputUntilStopWordEntered() {
    System.setIn(new ByteArrayInputStream("key1=value1\nkey2=value2\ndone\n".getBytes()));

    Map<String, String> result = TemplateUtils.readTemplateValues();

    assertEquals(2, result.size());

    System.setIn(System.in);
  }

  @Test
  @Tag("UnitTest")
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
  @Tag("UnitTest")
  void readMessageTemplateId_shouldReadFromConsoleAndReturnUserChoice() {
    System.setIn(new ByteArrayInputStream("3".getBytes()));
    val result = TemplateUtils.readMessageTemplateId();
    assertEquals(3L, result);
    System.setIn(System.in);
  }

  @Test
  @DisabledIfEnvironmentVariable(named = "PROCESSOR_ARCHITECTURE", matches = ".*32.*")
  void readMessageTemplateId_shouldLogErrorIfInputIsInvalid() {
    System.setIn(new ByteArrayInputStream("invalid\n123".getBytes()));
    Logger log = (Logger) LoggerFactory.getLogger(TemplateUtils.class);
    val testLogAppender = new TestLogAppender();
    log.addAppender(testLogAppender);
    testLogAppender.start();

    TemplateUtils.readMessageTemplateId();

    val lastLoggedEvent = testLogAppender.getLastLoggedEvent();

    assertEquals("Invalid input. Please enter a valid long value.", lastLoggedEvent.getMessage());

    System.setIn(System.in);
  }

  @Test
  @Tag("UnitTest")
  @DisabledIfSystemProperty(named = "os.version", matches = ".*10.*")
  void readMessageTemplateId_shouldReadFromConsoleUntilValidValueEntered() {
    System.setIn(new ByteArrayInputStream("abc\n123\n".getBytes()));
    val result = TemplateUtils.readMessageTemplateId();
    assertEquals(123L, result);
    System.setIn(System.in);
  }

  @TempDir
  File folder;
  @Tag("TempDir")
  @Test
  void writeMessageToFile_WithValidInput_ShouldCreateFile() throws IOException {
    val filename = folder.getAbsolutePath() + "/test.txt";
    val message = mock(Message.class);

    TemplateUtils.writeMessageToFile(message, filename);

    val file = new File(filename);

    assertTrue(file.exists());
  }

  @Test
  @DisabledOnJre(JRE.JAVA_13)
  @Tag("UnitTest")
  void writeMessageToFile_WithValidInput_ShouldCreateFileWithMessageSubjectAndBody() throws IOException {
    val subject = "Test Subject";
    val body = "Test Body";
    val filename = folder.getAbsolutePath() + "/test.txt";
    val message = new Message(subject, body);

    TemplateUtils.writeMessageToFile(message, filename);

    val file = new File(filename);

    val expectedContent = """
    Subject: Test Subject
    Body: Test Body""";

    val content = new String(Files.readAllBytes(file.toPath()));

    assertEquals(expectedContent.trim(), content.trim());
  }

  @ParameterizedTest
  @MethodSource("generateMessageArguments")
  @DisabledOnOs(OS.WINDOWS)
  @Tag("Parametrized")
  void testGenerateMessage_parametrizedTemplateValues(String subjectTemplate, String bodyTemplate,
      Map<String, String> templateValues, String expectedSubject, String expectedBody)
      throws TemplateException {

    val templateFile = "test.txt";

    val template = MessageTemplate.builder()
        .subjectTemplate(templateFile)
        .bodyTemplate(templateFile)
        .build();

    try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class,
        Mockito.CALLS_REAL_METHODS)) {
      mocked.when(() -> TemplateUtils.getContent("templates/subjects/" + template.getSubjectTemplate()))
          .thenReturn(subjectTemplate);
      mocked.when(() -> TemplateUtils.getContent("templates/" + template.getBodyTemplate()))
          .thenReturn(bodyTemplate);
     val message = TemplateUtils.generateMessage(template, templateValues);
      assertEquals(expectedSubject, message.subject());
      assertEquals(expectedBody, message.body());
    }
  }

  static Stream<Arguments> generateMessageArguments() {
    return Stream.of(
        Arguments.of("Hello, ${name}!", "Dear ${name}", Map.of("name", "Mariya"), "Hello, Mariya!", "Dear Mariya"),
        Arguments.of("Hello, ${name}!", "Dear ${name}", Map.of("name", "${tag}"), "Hello, ${tag}!", "Dear ${tag}"),
        Arguments.of("Hello, Friend", "Dear ${name}", Map.of("name", "12345"), "Hello, Friend", "Dear 12345"));
  }

  @TestFactory
  @DisabledOnOs({OS.AIX, OS.LINUX, OS.SOLARIS})
  @Tag("DynamicTest")
  Stream<DynamicTest> generateMessageTest_dynamicTest() {
    return Stream.of(
        Arguments.of("Hello, ${name}!", "Dear ${name}", Map.of("name", "Mariya"), "Hello, Mariya!", "Dear Mariya"),
        Arguments.of("Hello, ${name}!", "Dear ${name}", Map.of("name", "${tag}"), "Hello, ${tag}!", "Dear ${tag}"),
        Arguments.of("Hello, ${name}", "Dear ${name} ${surname}", Map.of("name", "Mariya", "surname", "Russakova"), "Hello, Mariya", "Dear Mariya Russakova"),
            Arguments.of("Hello, ${name}", "Dear ${name} ${surname}", Map.of("name", "${Mariya}", "surname", "Russakova"), "Hello, ${Mariya}", "Dear ${Mariya} Russakova"),
        Arguments.of("Hello, Friend", "Dear ${name}", Map.of("name", "12345"), "Hello, Friend", "Dear 12345"))
    .map(arguments -> {
      val templateFile = "test.txt";

      val template = MessageTemplate.builder()
          .subjectTemplate(templateFile)
          .bodyTemplate(templateFile)
          .build();

      val templateValues = (Map<String, String>) arguments.get()[2];
      val subjectTemplate = (String) arguments.get()[0];
      val bodyTemplate = (String) arguments.get()[1];
      val expectedSubject = (String) arguments.get()[3];
      val expectedBody = (String) arguments.get()[4];

      return DynamicTest.dynamicTest("Test generateMessage: " + subjectTemplate + " , " + bodyTemplate, () -> {

        try (MockedStatic<TemplateUtils> mocked = mockStatic(TemplateUtils.class,
            Mockito.CALLS_REAL_METHODS)) {
          mocked.when(() -> TemplateUtils.getContent(
                  "templates/subjects/" + template.getSubjectTemplate()))
              .thenReturn(subjectTemplate);
          mocked.when(() -> TemplateUtils.getContent("templates/" + template.getBodyTemplate()))
              .thenReturn(bodyTemplate);
          val message = TemplateUtils.generateMessage(template, templateValues);
          assertEquals(expectedSubject, message.subject());
          assertEquals(expectedBody, message.body());
        }

      });
    });
  }

}