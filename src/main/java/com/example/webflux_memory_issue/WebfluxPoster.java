package com.example.webflux_memory_issue;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebfluxPoster {

  private static final Duration WEBCLIENT_TIMEOUT = Duration.ofMinutes(1);

  private final WebClient fileServerWebClient;

  @Autowired
  public WebfluxPoster(final WebClient.Builder webClientBuilder) {
    this.fileServerWebClient = webClientBuilder.baseUrl("http://localhost:8080").build();
  }

  public void get() {
    fileServerWebClient.get().uri("/dead-letter")
        .retrieve()
        .toBodilessEntity()
        .block(WEBCLIENT_TIMEOUT);
  }

  public void writeJson(final Object object) {
    fileServerWebClient.put()
        .uri("/dead-letter")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(object)
        .retrieve()
        .toBodilessEntity()
        .block(WEBCLIENT_TIMEOUT);
  }
}
