package com.epam.ld.module2.testing.service.impl;

import com.epam.ld.module2.testing.model.MessageTemplate;
import com.epam.ld.module2.testing.repository.MessageTemplateRepository;
import com.epam.ld.module2.testing.service.MessageTemplateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl implements MessageTemplateService {

  private final MessageTemplateRepository repository;

  @Override
  public List<MessageTemplate> findAll() {
    return (List<MessageTemplate>) repository.findAll();
  }

  @Override
  public MessageTemplate findByCode(String code) {
    return repository.findByCode(code).orElse(null);
  }
}
