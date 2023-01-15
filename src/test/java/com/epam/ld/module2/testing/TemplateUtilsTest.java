package com.epam.ld.module2.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

import com.epam.ld.module2.testing.exception.TemplateException;
import com.epam.ld.module2.testing.model.MessageTemplate;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TemplateUtilsTest {

  @Test
  void generateMessage_shouldNeverReturnNull() throws TemplateException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result);
  }

  @Test
  void generateMessage_messageSubjectShouldNeverBeNull() throws TemplateException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result.subject());
  }

  @Test
  void generateMessage_messageSubjectShouldNeverBeBlank() throws TemplateException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertFalse(result.subject().isBlank());
  }

  @Test
  void generateMessage_messageBodyShouldNeverBeNull() throws TemplateException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result.body());
  }

  @Test
  void generateMessage_messageBodyShouldNeverBeBlank() throws TemplateException {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertFalse(result.body().isBlank());
  }

  @Test
  void generateMessage_shouldReturnSubjectTemplateContentIfNoPlaceholders()
      throws TemplateException {
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
  void generateMessage_shouldOverrideSubjectTemplateWithProvidedValue() throws TemplateException {
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
  void generateMessage_shouldOverrideBodyTemplateWithProvidedValue() throws TemplateException {
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
}