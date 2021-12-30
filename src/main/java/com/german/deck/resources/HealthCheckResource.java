package com.german.deck.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class HealthCheckResource {

    final Logger logger = LoggerFactory.getLogger(HealthCheckResource.class);

    @RequestMapping("/ping")
    public ResponseEntity<String> ping() {
        logger.info("Received ping message");
        return ResponseEntity.ok("pong");
    }

    @RequestMapping("/")
    public ResponseEntity<String> ping2(){
    	logger.info("Received ping message from root path");
	return ResponseEntity.ok("ping\n");
    }
}
