package com.epam.ld.module2.testing.repository;

import com.epam.ld.module2.testing.model.MessageTemplate;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MessageTemplateRepository extends CrudRepository<MessageTemplate, Long> {

  Optional<MessageTemplate> findByCode(String code);
}
