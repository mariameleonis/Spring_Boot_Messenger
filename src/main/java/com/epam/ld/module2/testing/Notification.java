package com.epam.ld.module2.testing;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Notification {

  private String id;
  private String type;
  private Set<String> recipients;
  private String subject;
  private String body;

}
