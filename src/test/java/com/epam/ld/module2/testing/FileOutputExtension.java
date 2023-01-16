package com.epam.ld.module2.testing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

public class FileOutputExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final String FILE_NAME = "src/test/test_execution.log";
  private static final String START_TIME = "start time";

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    Store store = getStore(context);
    store.put(START_TIME, LocalDateTime.now());
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Store store = getStore(context);
    LocalDateTime startTime = store.remove(START_TIME, LocalDateTime.class);
    LocalDateTime endTime = LocalDateTime.now();

    String testName = context.getRequiredTestMethod().getName();
    String executionTime = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " - " + endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    String testStatus = context.getExecutionException().isPresent() ? "FAILED" : "SUCCESS";
    String logLine = testName + " | " + executionTime + " | " + testStatus + System.lineSeparator();

    try {
      Path filePath = Paths.get(FILE_NAME);
      if (!Files.exists(filePath)) {
        Files.createFile(filePath);
      }
      Files.writeString(filePath, logLine, StandardOpenOption.APPEND);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Store getStore(ExtensionContext context) {
    return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
  }
}