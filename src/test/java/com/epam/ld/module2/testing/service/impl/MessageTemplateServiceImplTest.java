package com.epam.ld.module2.testing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.ld.module2.testing.exception.MessageTemplateNotFoundException;
import com.epam.ld.module2.testing.model.MessageTemplate;
import com.epam.ld.module2.testing.repository.MessageTemplateRepository;
import java.util.Collections;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageTemplateServiceImplTest {

  @Mock
  private MessageTemplateRepository repository;

  @InjectMocks
  private MessageTemplateServiceImpl service;

  @Test
  void findAll_shouldCallRepositoryFindAll() {
    service.findAll();
    verify(repository).findAll();
  }

  @Test
  void findAll_shouldReturnAListOfFoundMessageTemplates() {
    val messageTemplates = Collections.singletonList(MessageTemplate.builder().build());
    when(repository.findAll()).thenReturn(messageTemplates);
    val result = service.findAll();
    assertEquals(messageTemplates, result);
  }

  @Test
  void findByCode_shouldCallRepositoryFindByCode() {
    val code = "code";
    service.findByCode(code);
    verify(repository).findByCode(code);
  }

  @Test
  void findByCode_ifFound_shouldReturnMessageTemplate() {
    val messageTemplate = MessageTemplate.builder().build();
    val code = "code";
    when(repository.findByCode(code)).thenReturn(Optional.of(messageTemplate));
    val result = service.findByCode(code);
    assertEquals(messageTemplate, result);
  }

  @Test
  void findByCode_ifNotFound_shouldThrowsMessageTemplateNotFoundException() {
    val code = "code";
    when(repository.findByCode(code)).thenReturn(Optional.empty());
    assertThrows(MessageTemplateNotFoundException.class, () -> service.findByCode(code));
  }
}