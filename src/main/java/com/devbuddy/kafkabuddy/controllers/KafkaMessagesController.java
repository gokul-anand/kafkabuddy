package com.devbuddy.kafkabuddy.controllers;

import com.devbuddy.kafkabuddy.services.KafkaListenerService;
import com.devbuddy.kafkabuddy.services.KafkaPublishingService;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/kafkabuddy")
public class KafkaMessagesController {

	@Autowired
	public KafkaPublishingService kafkaPublishingService;

	@Autowired
	public KafkaListenerService kafkaListenerService;

	@PostMapping("/publishMessage")
	public ResponseEntity<HttpStatus> publishMessaage(@RequestParam(value = "topic") String topic, @RequestBody String message, @RequestHeader Map<String, String> headers) {
		kafkaPublishingService.publishMessage(topic,message, headers);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/readMessage")
	public ResponseEntity<String> readMessage(@RequestParam(value = "topic") String topic){
		ConsumerRecord<String, String> consumerRecord=null;
		consumerRecord = kafkaListenerService.readMessage(topic);

		HttpHeaders httpHeaders = new HttpHeaders();
		for(Header header : consumerRecord.headers()){
			httpHeaders.add("x-orginial-"+header.key(), new String(header.value()));
		}

		return new ResponseEntity<>(consumerRecord.value(), httpHeaders, HttpStatus.OK);
	}
}
