package com.epam.ld.module2.testing.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

import com.epam.ld.module2.testing.FileOutputExtension;
import com.epam.ld.module2.testing.repository.MessageTemplateRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageTemplateServiceImplTest {

  @Mock
  private MessageTemplateRepository repository;

  @InjectMocks
  private MessageTemplateServiceImpl service;

  @Test
  void findAll_shouldNotReturnEmptyList() {
    val result = service.findAll();
    assertFalse(result.isEmpty());
  }
}