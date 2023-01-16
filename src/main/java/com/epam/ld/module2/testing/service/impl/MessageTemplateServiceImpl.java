package com.epam.ld.module2.testing.service.impl;

import com.epam.ld.module2.testing.exception.MessageTemplateNotFoundException;
import com.epam.ld.module2.testing.model.MessageTemplate;
import com.epam.ld.module2.testing.repository.MessageTemplateRepository;
import com.epam.ld.module2.testing.service.MessageTemplateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageTemplateServiceImpl implements MessageTemplateService {

  public static final String MESSAGE_TEMPLATE_NOT_FOUND = "Message template with code %s not found";
  private final MessageTemplateRepository repository;

  @Override
  public List<MessageTemplate> findAll() {
    return (List<MessageTemplate>) repository.findAll();
  }

  @Override
  public MessageTemplate findByCode(String code) {
    return repository.findByCode(code).orElseThrow(() -> new MessageTemplateNotFoundException(
        String.format(MESSAGE_TEMPLATE_NOT_FOUND, code)));
  }
}
