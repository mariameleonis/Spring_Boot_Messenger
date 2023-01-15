package com.epam.ld.module2.testing;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Client {
  private Set<String> adresses;
}
