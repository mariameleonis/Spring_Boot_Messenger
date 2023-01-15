package com.epam.ld.module2.testing.service;

import com.epam.ld.module2.testing.model.MessageTemplate;
import java.util.List;

public interface MessageTemplateService {

  List<MessageTemplate> findAll();

  MessageTemplate findByType(String type);

}
