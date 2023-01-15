package com.epam.ld.module2.testing;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.ld.module2.testing.model.MessageTemplate;
import java.util.Map;
import lombok.val;
import org.junit.jupiter.api.Test;

class TemplateUtilsTest {

  @Test
  void generateMessage_shouldNeverReturnNull() {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result);
  }

  @Test
  void generateMessage_messageSubjectShouldNeverBeNull() {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result.subject());
  }

  @Test
  void generateMessage_messageSubjectShouldNeverBeBlank() {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertFalse(result.subject().isBlank());
  }

  @Test
  void generateMessage_messageBodyShouldNeverBeNull() {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertNotNull(result.body());
  }

  @Test
  void generateMessage_messageBodyShouldNeverBeBlank() {
    val template = MessageTemplate.builder().build();
    val values = Map.of(
        "name", "Mariya");
    val result = TemplateUtils.generateMessage(template, values);
    assertFalse(result.body().isBlank());
  }
}