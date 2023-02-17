package com.example.webflux_memory_issue;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.unit.DataSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemoryIssueBigFirstTest {

  private static final Logger LOG = LoggerFactory.getLogger(MemoryIssueBigFirstTest.class);

  @Autowired ExampleResource resource;

  @Test
  void memoryUseTest() throws InterruptedException {
    System.gc();
    LOG.info("Initial: memory={}", currentMemUsed());

    resource.big();
    Thread.sleep(1000);
    System.gc();

    LOG.info("First time: memory={}", currentMemUsed());

    final Random random = new Random();
    for (int i = 0; i < 10; i++) {
      final boolean big = random.nextBoolean();
      if (big) {
        resource.big();
      } else {
        resource.small();
      }
      Thread.sleep(1000);
      System.gc();
      LOG.info("Loop {}, called {}, memory={}", i, big ? "big one" : "small one", currentMemUsed());
    }
  }

  private String currentMemUsed() {
    return DataSize.ofBytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()).toMegabytes() + " MB";
  }
}
