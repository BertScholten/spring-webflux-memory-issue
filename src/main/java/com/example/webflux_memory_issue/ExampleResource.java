package com.example.webflux_memory_issue;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ExampleResource {

  @Autowired WebfluxPoster poster;

  @GetMapping(value = "/get")
  public ResponseEntity<String> get() {
    poster.get();
    return ResponseEntity.ok("Get done");
  }

  @GetMapping(value = "/small")
  public ResponseEntity<String> small() {
    poster.writeJson(IntStream.range(1, 10).toArray());
    return ResponseEntity.ok("Small done");
  }

  @GetMapping(value = "/big")
  public ResponseEntity<String> big() {
    poster.writeJson(IntStream.range(1, 10000000).toArray());
    return ResponseEntity.ok("Big done");
  }

  @GetMapping(value = "/dead-letter")
  public ResponseEntity<String> deadLetter() {
    return ResponseEntity.ok("Ignored get");
  }

  @PutMapping(value = "/dead-letter")
  public ResponseEntity<String> deadLetter(@RequestBody final String body) {
    return ResponseEntity.ok("Ignored put");
  }

}
