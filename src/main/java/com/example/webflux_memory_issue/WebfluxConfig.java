package com.example.webflux_memory_issue;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebfluxConfig implements WebFluxConfigurer {

  @Override
  public void configureHttpMessageCodecs(final ServerCodecConfigurer configurer) {
    configurer.defaultCodecs().maxInMemorySize(-1);
  }
}
