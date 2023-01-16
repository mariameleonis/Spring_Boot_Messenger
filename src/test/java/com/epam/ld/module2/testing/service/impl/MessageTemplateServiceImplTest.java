package com.epam.ld.module2.testing.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.ld.module2.testing.model.MessageTemplate;
import com.epam.ld.module2.testing.repository.MessageTemplateRepository;
import java.util.Collections;
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
}