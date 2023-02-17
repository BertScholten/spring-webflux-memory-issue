package com.example.webflux_memory_issue;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

@Service
public class WebfluxPoster {

  private static final Logger LOG = LoggerFactory.getLogger(WebfluxPoster.class);
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
        .onStatus(HttpStatus::is5xxServerError, clientResponse -> clientResponse.bodyToMono(String.class).map(message -> {
          LOG.error("Error while posting with message: {}", message);
          return new RuntimeException();
        }))
        .onStatus(HttpStatus::is4xxClientError, cr -> Mono.error(new ResponseStatusException(cr.statusCode(), cr.statusCode().getReasonPhrase())))
        .toBodilessEntity()
        .block(WEBCLIENT_TIMEOUT);
  }
}
