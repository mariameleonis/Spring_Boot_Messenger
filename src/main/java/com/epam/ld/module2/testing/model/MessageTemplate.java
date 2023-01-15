package com.epam.ld.module2.testing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message_template")
public class MessageTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "subject_template", nullable = false)
  private String subjectTemplate;

  @Column(name = "body_template", nullable = false)
  private String bodyTemplate;

}
